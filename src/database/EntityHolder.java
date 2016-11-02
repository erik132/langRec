package database;

import general.XmlHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erik on 2.11.2016.
 */
public class EntityHolder {

    List<SentenceChain> sentences;

    List<SentenceObject> words;

    XmlHolder xmlHolder;

    public EntityHolder(XmlHolder xmls){
        this.sentences = new ArrayList<SentenceChain>();
        this.words = new ArrayList<SentenceObject>();
        this.xmlHolder = xmls;
    }

    public void readXml(){
        sentences = this.xmlHolder.getSentences();

        for(int i=0; i< this.sentences.size(); i++){
            System.out.println(this.sentences.get(i).toString());
        }
    }



}
