import GoodRxApi.JSONClasses.IdentifyApiCall;
import GoodRxApi.JSONClasses.IdentifyApiResponse;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.*;


import VisionApi.VisionApiCaller;
import com.google.api.services.vision.v1.model.EntityAnnotation;

import java.awt.image.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import javax.imageio.*;
import java.io.*;

import GoodRxApi.*;

import static spark.Spark.*;

import GoodRxApi.*;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import VisionApi.VisionApiCaller;

import GoodRxApi.JSONClasses.*;


/**
 * Created by john on 4/23/16.
 */
public class Server {
    public void setup(){
        System.out.println("Starting server.");

        staticFileLocation("/public");
        post("/identify", (req, res) -> handlePost(req, res));

        System.out.println("Server setup complete.");
    }

    public static String handlePost(Request req, Response res){
        Gson gson = new Gson();

        IdentifyApiCall request = gson.fromJson(req.body(), IdentifyApiCall.class);
        String file64 = request.file;

        IdentifyApiResponse response = new IdentifyApiResponse();
        BufferedImage img = decodeToImage(file64);

        try {
            ImageIO.write(img, "png", new File("uploaded.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageData data = new ImageData(img,"input");
        ColorWrapper c = new ColorWrapper(data.averageAreaColor(data.width/2,data.height/2,10));
        String pillColor = c.closestColor();
        System.out.println("Color: "+pillColor);
        //response.color = pillColor;

        VisionApiCaller apiCaller = new VisionApiCaller(Paths.get("uploaded.png"));
        List<EntityAnnotation> textAnnotations = apiCaller.sendApiRequest();
        GoodRxResponse goodRxResponse = null;
        if(textAnnotations != null){
            goodRxResponse = TextRecognition.processTextWithGoodRx(textAnnotations,pillColor);
        }
        else{
            System.out.println("Sorry, no text found.");

            // do other stuff
        }
        if(goodRxResponse!=null && goodRxResponse.results!=null) {
            if(goodRxResponse.results.length==0){
                goodRxResponse = TextRecognition.processTextWithGoodRx(textAnnotations,"any");
            }
            for (int i = 0; i < goodRxResponse.results.length; i++) {
                for (int j = 0; j < goodRxResponse.results[i].pills.length; j++) {
                    response.possible_pills.add(goodRxResponse.results[i].pills[j].display);
                    response.possible_images.add(goodRxResponse.results[i].pills[j].image);
                }
            }
        }
        /*response.possible_pills.add("Adderall");
        response.possible_pills.add("Mentats");
        response.possible_pills.add("Mentats");
        response.possible_pills.add("Mentats");
        response.possible_pills.add("Mentats");
        response.possible_pills.add("Mentats");
        response.possible_pills.add("Mentats");
        response.possible_pills.add("Mentats");
        response.possible_pills.add("Mentats");
        response.possible_pills.add("Mentats");*/

        res.type("application/json");
        return gson.toJson(response);
    }

    public static BufferedImage decodeToImage(String imageString) {

        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
}
