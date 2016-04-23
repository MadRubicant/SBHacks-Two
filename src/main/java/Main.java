/**
 * Created by john on 4/22/16.
 */

import VisionApi.VisionApiCaller;

import java.awt.image.*;
import java.nio.file.Paths;
import java.util.Scanner;
import javax.imageio.*;
import java.io.*;

public class Main {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);

        String imageFileName = in.nextLine();
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(imageFileName));
        } catch (IOException e) {
            System.out.println("ERROR: could not read image.");
            System.exit(0);
        }
        ImageData Image = new ImageData(img, imageFileName);
        System.out.printf("Average color is %d\n", Image.averageColor(200, 200));
        System.out.println("Successfully read image.");

        VisionApiCaller apiCaller = new VisionApiCaller(Paths.get(imageFileName));

        // apiCaller.sendApiRequest();

        if(true) return;

        ImageData data = new ImageData(img, imageFileName);
        System.out.println(data.toString());

    }
}
