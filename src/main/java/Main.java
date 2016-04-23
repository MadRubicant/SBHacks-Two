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
            //System.exit(0);
        }

        WilliamTest();

        System.out.println("Successfully read image.");

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

    public static void WilliamTest() {
        String filename = "test-images/pill6.jpg";
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(filename));
        }
        catch (IOException e) {
            ;
        }
        ImageData Image = new ImageData(img, filename);
        //System.out.println(Image.toString());
        ImageData Horizontal = Image.newConvoluteImage(ImageData.horizontalEdgeMatrix);
        ImageData Vertical = Image.newConvoluteImage(ImageData.verticalEdgeMatrix);
        ImageData DiagLeft = Image.newConvoluteImage(ImageData.leftDiagonalEdgeMatrix);
        ImageData DiagRight = Image.newConvoluteImage(ImageData.rightDiagonalEdgeMatrix);
        ImageData Cardinal = Horizontal.averageWith(Vertical);
        ImageData Diag = DiagLeft.averageWith(DiagRight);
        Image = Cardinal.averageWith(Diag);
        Image.convoluteImage(ImageData.averageColorMatrix);
        //Image.convoluteImage(ImageData.diagonalEdgeMatrix);
        //System.out.println(Image.toString());
        Image.writeImage("test.png");
        System.out.printf("Average color is %d\n", Image.averageColor(200, 200));

    }
}
