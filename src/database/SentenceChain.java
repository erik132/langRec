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

    public void cleanDeps(){
        int i;

        for(i=0; i<this.links.size(); i++){
            if(this.links.get(i).governorId == this.links.get(i).targetId){
                links.remove(i);
                i--;
            }
        }
    }

    public void unifyDeps(int word1, int word2, String newWord){
        word1++;
        word2++;

        for(ChainLink link: this.links){
            if(link.governorId == word1 || link.governorId == word2){
                link.governor = newWord;
            }

            if(link.targetId == word1 || link.targetId == word2){
                link.target = newWord;
            }

            if(link.governorId == word2){
                link.governorId = word1;
            }

            if(link.targetId == word2){
                link.targetId = word1;
            }

            if(link.governorId > word2){
                link.governorId--;
            }
            if(link.targetId > word2){
                link.targetId--;
            }
        }
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
