package VisionApi;

import VisionApi.JSONClasses.AnnotateImageRequest;
import VisionApi.JSONClasses.ApiImage;
import VisionApi.JSONClasses.GoogleVisionRequest;
import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

/**
 * Created by john on 4/22/16.
 */
public class VisionApiCaller {
    public static final String VISION_API_URL = "https://vision.googleapis.com/v1/images:annotate";

    byte[] imageToUpload;

    public VisionApiCaller(byte[] image){
        imageToUpload = image;
    }

    public void sendApiRequest(){
        ApiImage image;
        AnnotateImageRequest imageRequestObj = new AnnotateImageRequest();
        GoogleVisionRequest apiRequestObj = new GoogleVisionRequest();
    }

    public void makePost(GoogleVisionRequest request) throws Exception{
        Gson gson = new Gson();

        URL apiUrl = new URL(VISION_API_URL);

        HttpURLConnection connection = (HttpURLConnection)apiUrl.openConnection();

        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
        writer.write(gson.toJson(request).getBytes());
        writer.flush();
        writer.close();

        int responseCode = connection.getResponseCode();

        if(responseCode % 100 != 2) throw new IOException("API request returned code " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String line;
        StringBuilder response = new StringBuilder();

        while((line = in.readLine()) != null){
            response.append(line + "\n");
        }
        in.close();
        connection.disconnect();

        System.out.println(response.toString());
    }

    public String base64Encode(){
        return Base64.getEncoder().encodeToString(imageToUpload);
    }
}
