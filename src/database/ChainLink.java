package database;

/**
 * Created by Erik on 3.11.2016.
 */
public class ChainLink {

    public String governor;
    public String target;

    public int governorId;
    public int targetId;

    public int governorId(){
        return this.governorId - 1;
    }

    public int targetId(){
        return this.targetId - 1;
    }
}
