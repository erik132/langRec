package database.triplets;

import general.Globals;

import java.util.ArrayList;

/**
 * Created by Erik on 14.11.2016.
 * Main type eg. PERSON, LOCATION, MOVIE is always locates at 0
 */
public class WordProps extends ArrayList<WordProp>{

    public String word;
    public int x;
    public int y;

    public WordProps(String word, String mainType, int x, int y){
        super();
        this.word = word;
        this.add(new WordProp(Globals.MAIN_TYPE,mainType));
        this.x = x;
        this.y = y;
    }

    public void consumeWordProps(WordProps other){
        for(WordProp prop: other){
            if(!this.contains(prop)){
                this.add(prop);
            }
        }
    }

    public boolean equals(WordProps other){
        if(other.word.equals(this.word)){
            return true;
        }
        return false;
    }

    public boolean equals(int x, int y){
        if(this.x == x && this.y == y){
            return true;
        }
        return false;
    }

    public String printRdf(){
        String result = "";

        for(WordProp prop: this){
            result += "rdf('" + this.word + "','" + prop.name() + "','" + prop.value() + "').\n";
        }

        return result;
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
