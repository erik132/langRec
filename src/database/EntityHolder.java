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

    XmlHolder xmlHolder;

    public EntityHolder(XmlHolder xmls){
        this.sentences = new ArrayList<SentenceChain>();
        this.words = new ArrayList<SentenceObject>();
        this.xmlHolder = xmls;

        this.populateMovietypes();
    }

    private void populateMovietypes(){
        this.movieTypes = new ArrayList<String>();
        this.movieTypes.add(Globals.THRILLER);
        this.movieTypes.add(Globals.COMEDY);
        this.movieTypes.add(Globals.SCIENCE_FICTION);
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
                    j--;
                }
            }
        }
    }

    public void cleanupSentences(){

    }

    public void associateMovieTypes(){
        int i,j,k;
        SentenceChain sentence;
        SentenceObject tempword;
        boolean result = false;

        for(i=0; i< sentences.size(); i++) {
            sentence = this.sentences.get(i);

            for(j=0; j<sentence.size(); j++){
                tempword = sentence.get(j);
                if(tempword.position.equals(Globals.NOUN)){
                    for(String movieType: this.movieTypes){
                        String[] parts = movieType.split(" ");
                        result = true;
                        for(k=0; k<parts.length; k++){
                            if((j+k >= sentence.size()) || !sentence.get(j+k).lemma.equals(parts[k])){
                                result = false;
                            }
                        }
                        if(result) break;
                    }
                    if(result) tempword.wordType = Globals.MOVIE_TYPE;
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
