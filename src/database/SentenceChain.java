package database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erik on 2.11.2016.
 */
public class SentenceChain {

    List<SentenceObject> words;

    public SentenceChain(){
        this.words = new ArrayList<SentenceObject>();
    }

    public void addWord(SentenceObject word){
        this.words.add(word);
    }

    @Override
    public String toString(){
        String result = "";
        int i, size = this.words.size();


        for(i=0; i< size; i++){
            result += this.words.get(i).name + " ";
        }
        return result;
    }
}
