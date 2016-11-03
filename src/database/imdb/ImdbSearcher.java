package database.imdb;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Erik on 3.11.2016.
 */
public class ImdbSearcher {

    List<ImdbPerson> people;
    private final String USER_AGENT = "Mozilla/5.0";

    public ImdbSearcher(List<ImdbPerson> people){
        this.people = people;
    }

    public List<ImdbPerson> findData(){
        String url = "http://www.imdb.com/xml/find?json=1&nr=1&nm=on&q=:--:";
        String tempName;
        System.out.println("finding data for " + this.people.size() + " people");

        for(ImdbPerson person: people){
            tempName = person.name.replace(' ', '+');

            try {
                person.imdbResponse = this.makeRequest(url.replace(":--:",tempName));
                person.cleanResponses();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("request failed");
            }
        }
        return this.people;
    }

    protected String makeRequest(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    @Override
    public String toString(){
        String result = "";

        /*for(ImdbPerson person: this.people){
            result += "------------------------------------\n" + person.imdbResponse;

        }*/
        for(ImdbPerson person: this.people){
            result += person.toString();
        }

        return result;
    }
}
