package GoodRxApi;

import com.google.gson.Gson;
import GoodRxApi.JSONClasses.GoodRxResponse;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Created by sierra on 4/23/16.
 */
public class GoodRxApiCaller {
    public static String GOODRX_API_URL = "http://www.goodrx.com/mobile-api/v3/pill-identifier/";
    public String imprint;
    public String color;
    public String shape;


    public GoodRxApiCaller(String myImprint, String myColor, String myShape){
        imprint = myImprint;
        color = myColor;
        shape = myShape;
    }

    public GoodRxResponse sendApiRequest(int numResults){

        try{
            return makePost(imprint, color, shape, numResults);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }

    }

    public GoodRxResponse makePost(String imprint, String color, String shape, int numResults) throws Exception{
        Gson gson = new Gson();
        String urlToSend = GOODRX_API_URL + imprint + "?color=" + color + "&shape=" + shape + "&start=0&end=" + numResults;
        URL apiUrl = new URL(urlToSend);

        HttpURLConnection connection = (HttpURLConnection)apiUrl.openConnection();

        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        if(responseCode / 100 != 2) throw new IOException("API request returned code " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String line;
        StringBuilder response = new StringBuilder();

        while((line = in.readLine()) != null) {
            response.append(line + "\n");
        }
        in.close();
        connection.disconnect();
        System.out.println(response.toString());

        return(gson.fromJson(response.toString(), GoodRxResponse.class));
    }

}


