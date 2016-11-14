package engine;

import database.EntityHolder;
import general.XmlHolder;
import org.xml.sax.SAXException;
import stanford.SentenceParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by Erik on 31.10.2016.
 */
public class MainEngine {

    private String inputFile;
    private String outXmlFile;
    private EntityHolder entityHolder;



    public MainEngine(String inputFile, String outXmlFile){
        this.inputFile = inputFile;
        this.outXmlFile = outXmlFile;
    }

    public void startParse(){

        SentenceParser stp = new SentenceParser();
        XmlHolder xmlHolder;

        try {
            stp.parseText(this.inputFile,this.outXmlFile);
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
        System.out.println(entityHolder.toString());
        //entityHolder.searchForActors();
        //entityHolder.cleanupSentences();
        System.out.println(entityHolder.printSentences());
        System.out.println(entityHolder.printTriplets());



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

}
