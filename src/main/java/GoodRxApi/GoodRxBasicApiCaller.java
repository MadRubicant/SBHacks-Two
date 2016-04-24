package GoodRxApi;

import com.google.gson.Gson;
import GoodRxApi.JSONClasses.GoodRxBasicResponse;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sierra on 4/23/16.
 */
public class GoodRxBasicApiCaller {
    public static String GOODRX_API_URL = "http://www.goodrx.com/mobile-api/v3/pill-imprint?search=";

    String imprint;

    public GoodRxBasicApiCaller(String text){
        imprint = text;
    }

    public GoodRxBasicResponse sendApiRequest(){

        try{
            return makePost(imprint);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }

    }

    public GoodRxBasicResponse makePost(String request) throws Exception{
        Gson gson = new Gson();
        String urlToSend = GOODRX_API_URL + request;
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

        return(gson.fromJson(response.toString(), GoodRxBasicResponse.class));
    }
}

