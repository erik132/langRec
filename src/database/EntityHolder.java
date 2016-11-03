package database;

import general.Globals;
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

        System.out.println(this.toString());
    }

    public void unifyNames(){
        int i,j;
        SentenceChain sentence;
        SentenceObject tempword;

        for(i=0; i< sentences.size(); i++){
            sentence = this.sentences.get(i);

            for(j=0; j<sentence.size(); j++){
                tempword = sentence.get(j);
                if(tempword.position.equals(Globals.ANY_NAME) && (j + 1) < sentence.size() && sentence.get(j+1).position.equals(Globals.ANY_NAME)){
                    tempword.name += " " + sentence.get(j+1).name;
                    sentence.remove(j+1);
                }
            }
        }
    }

    public String toString(){
        String result = "";

        for(SentenceChain sentence: this.sentences){
            result += sentence.toString() + "\n";
        }
        return result;
    }



}
