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
        inImage.getRGB(0, 0, width, height, RawData, 0, width);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color2d[x][y] = RawData[x + y*width];
            }
            
        }
    }

    // Begin bit-level magic
    public static byte getRed(int pixel) {
        return (byte)(pixel & redMask);
    }

    public static byte getGreen(int pixel) {
        return (byte)((pixel & greenMask) >> 8);
    }

    public static byte getBlue(int pixel) {
        return (byte)((pixel & blueMask) >> 16);
    }

    public static byte getAlpha(int pixel) {
        return (byte)((pixel & alphaMask) >> 24);
    }

    public static int newPixel(byte r, byte g, byte b, byte a) {
        int pixel = 0;
        pixel |= r | (g << 8) | (b << 16) | (a << 24);
        return pixel;
    }
    // End bit-level magic

    //Finds the average color in a floodfilled area.
    public int[] averageAreaColor(int x, int y, int tolerance){
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

    // Returns a byte array for network streaming
    public byte[] toByteArray() {
        byte[] unpackedImage = new byte[width * height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = Color2d[x][y];
                unpackedImage[x + y * width] = getRed(pixel);
                unpackedImage[x + y * width + 1] = getGreen(pixel);
                unpackedImage[x + y * width + 2] = getBlue(pixel);
                unpackedImage[x + y * width + 3] = getAlpha(pixel);
            }
        }
        return unpackedImage;
    }

    // Finds the average color of a pixel and its 8 surrounding pixels
    public int averageColor(int x, int y) {
        int[][] subArray = new int[3][3];
        int[][] pixelWeight = { { 1, 1, 1 },
                                { 1, 4, 1 },
                                { 1, 1, 1,} };
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <=1; j++) {
                int indx = x + i;
                int indy = y + j;
                if (indx < 0)
                    indx = 0;
                if (indx >= width)
                    indx = width - 1;
                if (indy < 0)
                    indy = 0;
                if (indy >= height)
                    indy = height - 1;
                subArray[i + 1][j + 1] = Color2d[indx][indy];
            }
        }
        int[] colorSums = new int[4];
        int totalWeight = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                colorSums[0] += getRed(subArray[i][j]) * pixelWeight[i][j];
                colorSums[1] = getGreen(subArray[i][j]) * pixelWeight[i][j];
                colorSums[2] = getBlue(subArray[i][j]) * pixelWeight[i][j];
                colorSums[3] = getAlpha(subArray[i][j]) * pixelWeight[i][j];
                totalWeight += pixelWeight[i][j];
            }
        }

        return newPixel((byte)(colorSums[0] / totalWeight), (byte)(colorSums[1] / totalWeight),
                (byte)(colorSums[2] / totalWeight), (byte)(colorSums[3] / totalWeight));
    }
}
