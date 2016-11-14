package database.triplets;

import general.Globals;

import java.util.ArrayList;

/**
 * Created by Erik on 14.11.2016.
 * Main type eg. PERSON, LOCATION, MOVIE is always locates at 0
 */
public class WordProps extends ArrayList<WordProp>{

    public String word;

    public WordProps(String word, String mainType){
        super();
        this.word = word;
        this.add(new WordProp(Globals.MAIN_TYPE,mainType));
    }

    public String toString(){
        String result;

        result = this.word + "\n";
        for(WordProp prop: this){
            result += "\t" + prop.name() + ": " + prop.value() + "\n";
        }

        return result;
    }
}
