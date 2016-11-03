package database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erik on 2.11.2016.
 */
public class SentenceChain extends ArrayList<SentenceObject>{


    public SentenceChain(){

    }

    public void addWord(SentenceObject word){
        this.add(word);
    }

    @Override
    public String toString(){
        String result = "";

        for(SentenceObject word: this){
            result += word.name + "(" + word.wordType + ") ";
        }
        return result;
    }
}
