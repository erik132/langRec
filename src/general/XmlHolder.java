package general;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import database.ChainLink;
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
    private String DEP_NAME = "dep";

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
        SentenceChain tempSentence;
        int i;
        NodeList words;
        NodeList sentenceComponents;
        NodeList deps;
        Node sentence;
        Node tokens;
        Node dependencyMain;


        NodeList sents = this.xmlDoc.getElementsByTagName("sentence");
        System.out.println(sents.getLength());
        for(i =0; i< sents.getLength(); i++){
            sentence = sents.item(i);
            sentenceComponents = sentence.getChildNodes();

            tokens = this.findNode(sentenceComponents, "tokens");
            if(tokens == null){
                continue;
            }
            words = tokens.getChildNodes();
            tempSentence = this.pickWords(words);
            sentences.add(tempSentence);

            dependencyMain = this.findNode(sentenceComponents, "dependencies");
            if(dependencyMain == null){
                continue;
            }
            deps = dependencyMain.getChildNodes();
            this.populateDeps(deps,tempSentence);

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
                name = element.getElementsByTagName("word").item(0).getTextContent();
                wordType = element.getElementsByTagName("NER").item(0).getTextContent();
                word = new SentenceObject(name, wordType);
                word.position = element.getElementsByTagName("POS").item(0).getTextContent();
                word.lemma = element.getElementsByTagName("lemma").item(0).getTextContent();
                sentence.addWord(word);
            }
        }

        return sentence;
    }

    protected void populateDeps(NodeList deps, SentenceChain sentence){
        int j;
        Node tempNode, tempnode2;
        ChainLink link;

        for(j=0; j <deps.getLength(); j++){
            tempNode = deps.item(j);
            if(tempNode.getNodeName() == this.DEP_NAME){
                Element element = (Element) tempNode;
                link = new ChainLink();
                tempnode2 = element.getElementsByTagName("governor").item(0);
                link.governor = tempnode2.getTextContent();
                link.governorId = Integer.parseInt(tempnode2.getAttributes().getNamedItem("idx").getNodeValue());

                tempnode2 = element.getElementsByTagName("dependent").item(0);
                link.target = tempnode2.getTextContent();
                link.targetId = Integer.parseInt(tempnode2.getAttributes().getNamedItem("idx").getNodeValue());
                sentence.addChainLink(link);
            }
        }

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
