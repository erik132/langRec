package database.imdb;

import org.json.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erik on 3.11.2016.
 */
public class ImdbPerson {

    public String name;
    public String type;

    public String imdbResponse;
    public List<String> movies;
    public List<String> roles;

    public ImdbPerson(String name){
        this.name = name;
        this.movies = new ArrayList<>();
        this.roles = new ArrayList<>();
    }

    public void cleanResponses(){
        int i;
        JSONObject tempjson;
        String tempString[];

        /*this.imdbResponse = this.imdbResponse.replace("name_exact", "name_x");
        this.imdbResponse = this.imdbResponse.replace("name_popular", "name_x");
        this.imdbResponse = this.imdbResponse.replace("name_approx", "name_x");*/

        JSONObject obj = new JSONObject(this.imdbResponse);

        JSONArray arr = this.findArray(obj);

        for(i=0; i<arr.length(); i++){
            tempjson = arr.getJSONObject(i);
            if(tempjson.getString("name").equals(this.name)){
                tempString = tempjson.getString("description").split(",");
                if(tempString.length > 0) {
                    roles.add(tempString[0].trim());
                    movies.add(tempString[1].trim());
                }
            }
        }
    }

    private JSONArray findArray(JSONObject json){
        List<String> names = new ArrayList<>();
        JSONArray arr = null;
        names.add("name_exact");
        names.add("name_popular");
        names.add("name_approx");

        for(String name: names){
            try {
                arr = json.getJSONArray(name);
            }catch (Exception e){
                arr = null;
            }
            if(arr != null) break;
        }
        return arr;
    }

    @Override
    public String toString(){
        String result = "";
        int i;

        result += this.name + "------------------------------------------------\n";
        for(i=0; i<this.roles.size(); i++){
            result += this.roles.get(i) + " : " + this.movies + "\n";
        }


        return result;
    }
}
