package VisionApi;

import VisionApi.JSONClasses.*;
import com.google.gson.Gson;
import VisionApi.JSONClasses.AnnotateImageRequest;
import VisionApi.JSONClasses.ApiImage;
import VisionApi.JSONClasses.GoogleVisionRequest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public GoogleVisionResponse sendApiRequest(){
        GoogleVisionRequest requestObj = buildRequest();
        try {
            return makePost(requestObj);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Feature[] makeFeatures(){
        Feature[] ret = new Feature[3];
        for(int i = 0; i < ret.length; i++){
            ret[i] = new Feature();
        }

        ret[0].maxResults = 20;
        ret[0].type = "TEXT_DETECTION";

        ret[1].maxResults = 20;
        ret[1].type = "FACE_DETECTION";

        ret[2].maxResults = 20;
        ret[2].type = "IMAGE_PROPERTIES";

        return ret;
    }

    public GoogleVisionRequest buildRequest(){
        ApiImage image = new ApiImage();
        image.content = base64Encode();

        AnnotateImageRequest imageRequestObj = new AnnotateImageRequest();
        imageRequestObj.image = image;
        imageRequestObj.features = makeFeatures();

        GoogleVisionRequest apiRequestObj = new GoogleVisionRequest();
        apiRequestObj.requests = new AnnotateImageRequest[1];
        apiRequestObj.requests[0] = imageRequestObj;

        return apiRequestObj;
    }

    public GoogleVisionResponse makePost(GoogleVisionRequest request) throws Exception{
        Gson gson = new Gson();

        URL apiUrl = new URL(VISION_API_URL);

        String requestString = gson.toJson(request);
        System.out.println(requestString);

        HttpURLConnection connection = (HttpURLConnection)apiUrl.openConnection();

        connection.setRequestMethod("POST");
        DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
        writer.write(requestString.getBytes());
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

        return gson.fromJson(response.toString(), GoogleVisionResponse.class);
    }

    public String base64Encode(){
        return Base64.getEncoder().encodeToString(imageToUpload);
    }
}
