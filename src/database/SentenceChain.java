package database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erik on 2.11.2016.
 */
public class SentenceChain extends ArrayList<SentenceObject>{

    List<ChainLink> links;

    public SentenceChain(){
        links = new ArrayList<>();
    }

    public void addWord(SentenceObject word){
        this.add(word);
    }

    public void addChainLink(ChainLink link){
        this.links.add(link);
    }

    public int getLinkLength(){
        return this.links.size();
    }

    public void removeLink(int index){
        this.links.remove(index);
    }

    public String printLinks(){
        String result = "";

        for(ChainLink link: this.links){
            result += link.governor + " " + link.governorId + " -> " + link.target + " " + link.targetId + "\n";
        }
        return result;
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
