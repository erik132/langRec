package database;

import com.sun.javafx.scene.control.GlobalMenuAdapter;
import database.imdb.ImdbPerson;
import database.imdb.ImdbSearcher;
import database.triplets.WordProp;
import database.triplets.WordProps;
import general.Globals;
import general.XmlHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erik on 2.11.2016.
 */
public class EntityHolder {

    List<SentenceChain> sentences;

    List<WordProps> wordTriplets;
    List<String> movieTypes;
    List<String> movieKeywords;

    XmlHolder xmlHolder;

    public EntityHolder(XmlHolder xmls){
        this.sentences = new ArrayList<SentenceChain>();
        this.wordTriplets = new ArrayList<>();
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
        this.movieKeywords.add(Globals.MOVIE_WORD);
        this.movieKeywords.add(Globals.STAR);

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
                    sentence.unifyDeps(j,j+1,tempword.name);
                    sentence.remove(j+1);
                    j--;
                }
            }
        }
    }

    public void cleanupSentences(){
        int i;

        for(SentenceChain sentence: this.sentences){
            for(i=0; i<sentence.size(); i++){
                if(sentence.get(i).wordType.equals("O")) {
                    sentence.remove(i);
                    i--;
                }
            }
        }
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
        positions.add(Globals.VERB3);
        positions.add(Globals.NOUN);
        this.associateType(Globals.INTERACTION_KEYWORD, this.movieKeywords, positions);
    }

    protected void associateType(String newType, List<String> matches, List<String> positions){
        int i,j,k,d;
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
                            if(result && k>0){
                                for(d=1; d<=k; d++){
                                    tempword.name += " " + sentence.get(j + 1).name;
                                    sentence.unifyDeps(j,j+1,tempword.name);
                                    sentence.remove(j + 1);
                                }
                            }
                        }
                        if(result) break;
                    }
                    if(result) tempword.wordType = newType;
                }
            }
        }
    }

    public void markMovieAreas(){
        int i, keyword;
        int minTarget = 9999, maxTarget = 0;
        ChainLink tempLink;


        for(SentenceChain sentence: this.sentences){
            keyword = -1;
            for(i=0; i<sentence.size(); i++){
                if(this.movieKeywords.contains(sentence.get(i).lemma) && sentence.get(i).wordType.equals(Globals.INTERACTION_KEYWORD)){
                    keyword = i;
                    break;
                }
            }
            if(keyword == -1){
                for(i=0; i<sentence.size(); i++){
                    if(this.movieTypes.contains(sentence.get(i).lemma) && sentence.get(i).wordType.equals(Globals.MOVIE_TYPE)){
                        keyword = i;
                        break;
                    }
                }
                if(keyword == -1){
                    continue;
                }
            }
            System.out.println("keyword is at: " + keyword);
            for(i=0; i<sentence.linkLength(); i++){
                tempLink = sentence.getLink(i);
                if(tempLink.governorId() == keyword ){
                    if(tempLink.targetId() < minTarget){
                        minTarget = tempLink.targetId();

                    }

                }
            }
            if(minTarget != 9999){
                sentence.get(minTarget).wordType = Globals.MOVIE;
                minTarget = 9999;
            }

        }
    }

    public void makeTriplets(){
        int i,j,k;
        SentenceObject tempword;
        SentenceChain sentence;
        ChainLink tempLink;
        WordProps movieProps;
        WordProps interprops;
        List<Integer> movies = new ArrayList<>();
        List<Integer> interactions = new ArrayList<>();

        for(k=0; k<this.sentences.size(); k++){
            sentence = this.sentences.get(k);
            for(i=0; i<sentence.size(); i++){
                tempword = sentence.get(i);
                if(!tempword.wordType.equals("O")){
                    if(tempword.wordType.equals(Globals.INTERACTION_KEYWORD) || tempword.wordType.equals(Globals.MOVIE_TYPE)) {
                        this.wordTriplets.add(new WordProps(tempword.lemma, tempword.wordType,k,i));


                    }else{
                        this.wordTriplets.add(new WordProps(tempword.name, tempword.wordType,k,i));
                    }
                }
            }
            for(i=0; i<sentence.size(); i++){
                tempword = sentence.get(i);
                if(!tempword.wordType.equals("O")) {
                    if(tempword.wordType.equals(Globals.INTERACTION_KEYWORD)){
                        for(j=0; j<sentence.linkLength(); j++){
                            tempLink = sentence.getLink(j);
                            if(tempLink.governorId() == i){
                                if(sentence.get(tempLink.targetId()).wordType.equals(Globals.MOVIE)){
                                    movies.add(tempLink.targetId());
                                }else{
                                    interactions.add(tempLink.targetId());
                                }
                            }
                        }
                        for(int movie: movies){
                            movieProps = this.getTriplet(k,movie);
                            for(int interaction: interactions){
                                interprops = this.getTriplet(k,interaction);
                                if(interprops != null && !sentence.get(interaction).wordType.equals(Globals.INTERACTION_KEYWORD)){
                                    if(sentence.get(interaction).wordType.equals(Globals.MOVIE_TYPE)){
                                        interprops.add(new WordProp(tempword.lemma, sentence.get(movie).name));
                                        movieProps.add(new WordProp(Globals.MOVIE_TYPE, sentence.get(interaction).name));
                                    }else {
                                        interprops.add(new WordProp(tempword.lemma, sentence.get(movie).name));
                                        movieProps.add(new WordProp(tempword.lemma, sentence.get(interaction).name));
                                    }
                                }
                            }
                        }
                        movies.clear();
                        interactions.clear();
                    }
                    if(tempword.wordType.equals(Globals.MOVIE_TYPE)){
                        for(j=0; j<sentence.linkLength(); j++){
                            tempLink = sentence.getLink(j);
                            if(tempLink.governorId() == i){
                                if(sentence.get(tempLink.targetId()).wordType.equals(Globals.MOVIE)){
                                    this.getTriplet(k,i).add(new WordProp(Globals.MOVIE_TYPE,tempLink.governor));
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    private WordProps getTriplet(int x, int y){
        for(WordProps triplet: this.wordTriplets){
            if(triplet.equals(x,y)){
                return triplet;
            }
        }
        return null;
    }


    public String printTriplets(){
        String result = "";

        for(WordProps wordProps: this.wordTriplets){
            result += wordProps.toString() + "\n";
        }

        return result;
    }

    public String printRdfTriplets(){
        String result = "";

        for(WordProps wordProps: this.wordTriplets){
            result += wordProps.printRdf() + "\n";
        }

        return result;
    }

    public void unifyTriplets(){
        int i,j;
        WordProps triplet;
        for(i=0; i<this.wordTriplets.size(); i++){
            triplet = this.wordTriplets.get(i);
            if(triplet.get(0).value().equals(Globals.INTERACTION_KEYWORD) || triplet.get(0).value().equals(Globals.MOVIE_TYPE)){
                this.wordTriplets.remove(i);
                i--;
            }
        }

        for(i=0; i<this.wordTriplets.size(); i++){
            triplet = this.wordTriplets.get(i);
            for(j= i+1; j<this.wordTriplets.size(); j++){
                if(triplet.equals(this.wordTriplets.get(j))){
                    triplet.consumeWordProps(this.wordTriplets.get(j));
                    this.wordTriplets.remove(j);
                    j--;
                }
            }
        }
    }

    public void searchForActors(){
        List<ImdbPerson> people = new ArrayList<>();
        int i,j;
        WordProps tempTriplet;

        for(WordProps triplet: this.wordTriplets) {
            if(triplet.get(0).value().equals(Globals.PERSON)) {
                people.add(new ImdbPerson(triplet.word, triplet.x, triplet.y));
            }
        }

        ImdbSearcher searcher = new ImdbSearcher(people);
        people = searcher.findData();
        for(ImdbPerson person: people){
            tempTriplet = this.getTriplet(person.x,person.y);
            for(i=0; i<person.roles.size(); i++){
                tempTriplet.add(new WordProp(Globals.IMDB_ROLE /*+ "_" + person.roles.get(i)*/,person.movies.get(i)));
            }

        }

    }

    public void cleanDoubleDeps(){
        for(SentenceChain sentence: this.sentences){
            sentence.cleanDeps();
        }
    }

    public String printSentences(){
        String result = "";

        for(SentenceChain sentence: this.sentences){
            result += sentence.toString() + "\n";
        }
        return result;
    }

    @Override
    public String toString(){
        String result = "";

        for(SentenceChain sentence: this.sentences){
            result += sentence.toString() + "\n";
            result += sentence.printLinks();
        }
        return result;
    }



}
