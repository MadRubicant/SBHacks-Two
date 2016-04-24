import GoodRxApi.JSONClasses.IdentifyApiCall;
import GoodRxApi.JSONClasses.IdentifyApiResponse;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import static spark.Spark.*;

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

        IdentifyApiResponse response = new IdentifyApiResponse();
        response.possible_pills.add("Adderall");

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
