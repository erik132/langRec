package general;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by Erik on 1.12.2016.
 */
public class XmlHolderInput {

    private Document xmlDoc;

    public XmlHolderInput(String fileName) throws ParserConfigurationException, IOException, SAXException {
        File inputFile = new File(fileName);
        DocumentBuilderFactory dbFactory
                = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        xmlDoc = dBuilder.parse(inputFile);
        xmlDoc.normalize();
    }

    public String getMsg(){
        return this.getElement(Globals.INPUT_MESSAGE);
    }

    public String getFriend1(){

        return this.getElement(Globals.INPUT_FRIEND1);
    }

    public String getFriend2(){

        return this.getElement(Globals.INPUT_FRIEND2);
    }

    protected String getElement(String elemName){
        int i;
        NodeList nodes = this.xmlDoc.getElementsByTagName(elemName);

        for(i=0; i<nodes.getLength(); i++){
            Node node = nodes.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE){
                return node.getTextContent();
            }
        }
        return "";
    }

}
