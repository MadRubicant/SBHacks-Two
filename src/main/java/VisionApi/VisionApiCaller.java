package VisionApi;

import VisionApi.JSONClasses.AnnotateImageRequest;
import VisionApi.JSONClasses.ApiImage;
import VisionApi.JSONClasses.GoogleVisionRequest;

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
        URL apiUrl = new URL(VISION_API_URL);

        HttpURLConnection connection = (HttpURLConnection)apiUrl.openConnection();

        connection.setRequestMethod("POST");
    }

    public String base64Encode(){
        return Base64.getEncoder().encodeToString(imageToUpload);
    }
}
