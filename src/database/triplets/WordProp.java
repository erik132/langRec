package database.triplets;

/**
 * Created by Erik on 14.11.2016.
 */
public class WordProp {

    private String name;
    private String value;

    public WordProp(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String name(){
        return this.name;
    }

    public String value(){
        return this.value;
    }

    @Override
    public boolean equals(Object other){
        WordProp tempProp;
        if(other instanceof WordProp){
            tempProp = (WordProp)other;
            return (tempProp.name().equals(this.name()) && tempProp.value().equals(this.value()));
        }
        return false;
    }
}
