package engine;

import database.EntityHolder;
import general.Globals;
import general.XmlHolder;
import general.XmlHolderInput;
import org.xml.sax.SAXException;
import stanford.SentenceParser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

/**
 * Created by Erik on 31.10.2016.
 */
public class MainEngine {

    private String inputFile;
    private String outXmlFile;
    private EntityHolder entityHolder;

    private String inputText;
    private String friend1;
    private String friend2;

    public MainEngine(String inputFile, String outXmlFile){
        this.inputFile = inputFile;
        this.outXmlFile = outXmlFile;
    }

    public void startParse(){

        SentenceParser stp = new SentenceParser();
        XmlHolder xmlHolder;

        this.readInputFile(this.inputFile);

        try {
            stp.parseText(this.inputText,this.outXmlFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Stanford parser burned down");
            System.exit(4);
        }



        xmlHolder = this.makeXmlHolder(this.outXmlFile);
        entityHolder = new EntityHolder(xmlHolder);
        entityHolder.readXml();
        entityHolder.unifyNames();
        entityHolder.associateKeywords();
        entityHolder.associateMovieTypes();
        entityHolder.cleanDoubleDeps();
        entityHolder.markMovieAreas();
        entityHolder.makeTriplets();
        entityHolder.unifyTriplets();
        System.out.println(entityHolder.toString());
        //entityHolder.searchForActors();
        //entityHolder.cleanupSentences();
        System.out.println(entityHolder.printSentences());
        System.out.println(entityHolder.printTriplets());

        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("otterinput.txt"), "utf-8"));
            writer.write(this.produceOtterInput(entityHolder.printRdfTriplets()));
            writer.close();
        }catch (Exception e){
            System.out.println("your writer is done for it m8.");
        }


    }

    protected XmlHolder makeXmlHolder(String xmlFile){
        XmlHolder xmlHolder = null;
        try {
            xmlHolder = new XmlHolder(xmlFile);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            System.out.println("xml parser configuration broken");
            System.exit(1);
        } catch (SAXException e) {
            e.printStackTrace();
            System.out.println("xml SAX exception. Wait what?");
            System.exit(2);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("xmlHolder IO excpetion. You do have the file right?");
            System.exit(3);
        }

        return xmlHolder;
    }

    protected void readInputFile(String xmlFile){
        XmlHolderInput xmlHolder = null;
        try {

            xmlHolder = new XmlHolderInput(xmlFile);
            this.inputText = xmlHolder.getMsg();
            this.friend1 = xmlHolder.getFriend1();
            this.friend2 = xmlHolder.getFriend2();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            System.out.println("xml parser configuration broken");
            System.exit(1);
        } catch (SAXException e) {
            e.printStackTrace();
            System.out.println("xml SAX exception. Wait what?");
            System.exit(2);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("xmlHolder IO excpetion. You do have the file right?");
            System.exit(3);
        }

    }

    protected String produceOtterInput(String rdfData){
        String template = this.getOtterTemplate();

        template = template.replace(Globals.TEMPLATE_DATA,rdfData);
        template = template.replace(Globals.TEMPLATE_FRIEND1,this.friend1);
        template = template.replace(Globals.TEMPLATE_FRIEND2,this.friend2);

        return template;
    }

    protected String getOtterTemplate(){
        String result = "";

        try {
            BufferedReader br = new BufferedReader(new FileReader(Globals.OTTER_TEMPLATE));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            result = sb.toString();

            br.close();
        }catch(Exception e){
            System.out.println("otter template could not be read");
            System.exit(4);
        }
        return result;
    }


}
