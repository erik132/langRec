package database;

/**
 * Created by Erik on 2.11.2016.
 */
public class SentenceObject {

    public String name;
    public String wordType;
    public String position;
    public String lemma;
    public boolean unified;

    public SentenceObject(String name, String wordType){
        this.name = name;
        this.wordType = wordType;
        this.unified = false;

    }

}
