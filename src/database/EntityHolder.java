package database;

import com.sun.javafx.scene.control.GlobalMenuAdapter;
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
    List<String> movieTypes;
    List<String> movieKeywords;

    XmlHolder xmlHolder;

    public EntityHolder(XmlHolder xmls){
        this.sentences = new ArrayList<SentenceChain>();
        this.words = new ArrayList<SentenceObject>();
        this.xmlHolder = xmls;

        this.populateMovietypes();
        this.populateMovieKeywords();
    }

    private void populateMovietypes(){
        this.movieTypes = new ArrayList<String>();
        this.movieTypes.add(Globals.THRILLER);
        this.movieTypes.add(Globals.COMEDY);
        this.movieTypes.add(Globals.SCIENCE_FICTION);
    }

    private void populateMovieKeywords(){
        this.movieKeywords = new ArrayList<String>();
        this.movieKeywords.add(Globals.DIRECT);
        this.movieKeywords.add(Globals.ACT);
        this.movieKeywords.add(Globals.PLAY);
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
                    tempword.unified = true;
                    sentence.remove(j+1);
                    j--;
                }
            }
        }
    }

    public void cleanupSentences(){

    }

    public void associateMovieTypes(){
        List<String> positions = new ArrayList<String>();
        positions.add(Globals.NOUN);
        this.associateType(Globals.MOVIE_TYPE, this.movieTypes, positions);
    }

    public void associateKeywords(){
        List<String> positions = new ArrayList<String>();
        positions.add(Globals.VERB1);
        positions.add(Globals.VERB2);
        this.associateType(Globals.INTERACTION_KEYWORD, this.movieKeywords, positions);
    }

    protected void associateType(String newType, List<String> matches, List<String> positions){
        int i,j,k;
        SentenceChain sentence;
        SentenceObject tempword;
        boolean result = false;
        
        for(i=0; i< sentences.size(); i++) {
            sentence = this.sentences.get(i);

            for(j=0; j<sentence.size(); j++){
                tempword = sentence.get(j);
                if(positions.contains(tempword.position)){
                    for(String compType: matches){
                        String[] parts = compType.split(" ");
                        result = true;
                        for(k=0; k<parts.length; k++){
                            if((j+k >= sentence.size()) || !sentence.get(j+k).lemma.equals(parts[k])){
                                result = false;
                            }
                        }
                        if(result) break;
                    }
                    if(result) tempword.wordType = newType;
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
