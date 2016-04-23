/**
 * Created by john on 4/22/16.
 */

import VisionApi.VisionApiCaller;
import com.google.api.services.vision.v1.model.EntityAnnotation;

import java.awt.image.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import javax.imageio.*;
import java.io.*;

public class Main {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);

        String imageFileName = in.nextLine();

        if(imageFileName.equals("serve")){
            Server server = new Server();
            server.setup();
            return;
        }

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(imageFileName));
        } catch (IOException e) {
            System.out.println("ERROR: could not read image.");
            System.exit(0);
        }

        System.out.println("Successfully read image.");

        ImageData Image = new ImageData(img, imageFileName);
        Image.toBufferedImage();
        System.out.printf("Average color is %d\n", Image.averageColor(200, 200));

        VisionApiCaller apiCaller = new VisionApiCaller(Paths.get(imageFileName));
        List<EntityAnnotation> textAnnotations = apiCaller.sendApiRequest();

        if(textAnnotations != null){
            // do_stuff
        }
        else{
            System.out.println("Sorry, no text found.");

            // do other stuff
        }

        if(true) return;

        ImageData data = new ImageData(img, imageFileName);
        //System.out.println(data.toString());

    }
}
