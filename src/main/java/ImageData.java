import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Stack;

/**
 * Created by William Bennett on 4/22/2016.
 */
public class ImageData {
    public int[][] Color2d;
    public int width;
    public int height;
    public String name;

    public static int redMask = 0xff;
    public static int greenMask = 0xff00;
    public static int blueMask = 0xff0000;
    public static int alphaMask = 0xff000000;

    public ImageData(BufferedImage inImage, String filename) {
        name = filename;
        width = inImage.getWidth();
        height = inImage.getHeight();
        Color2d = new int[width][height];
        int[] RawData = new int[width * height];
        inImage.getRGB(0, 0, width, height, RawData, 0, 0);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color2d[x][y] = RawData[x + y*width];
            }
            
        }
    }
    
    public static int getRed(int pixel) {
        return pixel & redMask;
    }

    public static int getGreen(int pixel) {
        return (pixel & greenMask) >> 8;
    }

    public static int getBlue(int pixel) {
        return (pixel & blueMask) >> 16;
    }

    public static int getAlpha(int pixel) {
        return (pixel & alphaMask) >> 24;
    }

    //Finds the average color in a floodfilled area.
    public int[] averageColor(int x, int y, int tolerance){
        Stack<Integer[]> stack = new Stack<Integer[]>();
        stack.push(new Integer[]{x,y});
        int[] totalsRGB = new int[3];
        while(!stack.isEmpty()){
            stack.pop();
        }
        return new int[]{0,0,0};
    }

    public String toString(){
        String result = "";
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                result+=Color2d[x][y]+" ";
            }
            result+="\n";
        }
        return result;
    }
}
