package general;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import database.SentenceChain;
import database.SentenceObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Created by Erik on 31.10.2016.
 */
public class XmlHolder {

    private Document xmlDoc;

    private String WORD_NAME = "token";

    public XmlHolder(String fileName) throws ParserConfigurationException, IOException, SAXException {
        File inputFile = new File(fileName);
        DocumentBuilderFactory dbFactory
                = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        xmlDoc = dBuilder.parse(inputFile);
        xmlDoc.normalize();
    }

    public String getRoot(){
        return xmlDoc.getDocumentElement().getNodeName();
    }

    public List<SentenceChain> getSentences(){
        List<SentenceChain> sentences = new ArrayList<SentenceChain>();
        int i;
        NodeList words;
        NodeList sentenceComponents;
        Node sentence;
        Node tokens;


        NodeList sents = this.xmlDoc.getElementsByTagName("sentence");
        for(i =0; i< sents.getLength()/2; i++){
            sentence = sents.item(i);
            sentenceComponents = sentence.getChildNodes();

            tokens = this.findNode(sentenceComponents, "tokens");

            words = tokens.getChildNodes();
            sentences.add(this.pickWords(words));

        }

        System.out.println("amount of sentences: " + sents.getLength());
        return sentences;
    }

    protected SentenceChain pickWords(NodeList words){
        SentenceChain sentence = new SentenceChain();
        int j;
        Node tempNode;
        SentenceObject word;
        String name;
        String wordType;

        for(j=0; j<words.getLength(); j++){
            tempNode = words.item(j);
            if(tempNode.getNodeName() == this.WORD_NAME){
                Element element = (Element) tempNode;
                name = element.getElementsByTagName("lemma").item(0).getTextContent();
                wordType = element.getElementsByTagName("NER").item(0).getTextContent();
                word = new SentenceObject(name, wordType);
                word.position = element.getElementsByTagName("POS").item(0).getTextContent();
                sentence.addWord(word);
            }
        }

        return sentence;
    }

    protected Node findNode(NodeList nodes,String node){
        int i, length = nodes.getLength();
        Node result = null;

        for(i=0; i< length; i++){
            if(nodes.item(i).getNodeName() == node){
                result = nodes.item(i);
            }
        }
        return result;
    }
}
