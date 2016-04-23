import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.nio.file.Path;
import java.util.Stack;

/**
 * Created by William Bennett on 4/22/2016.
 */
public class ImageData {
    public int[][] Color2d;
    public int width;
    public int height;
    public String name;

    public static int blueMask = 0xff;
    public static int greenMask = 0xff00;
    public static int redMask = 0xff0000;
    public static int alphaMask = 0xff000000;

    public static final int[][] averageColorMatrix = {{ 1, 1, 1 },
                                                      { 1, 4, 1 },
                                                      { 1, 1, 1 }};

    public static final int[][] horizontalEdgeMatrix = {
            {2, 2, 2, 2, 2},
            {1, 1, 1, 1, 1 },
            {0, 0, 0, 0, 0 },
            {-1, -1, -1, -1, -1},
            {-2, -2, -2, -2, -2}};

    public static final int[][] verticalEdgeMatrix = {
            {2, 1, 0, -1, -2},
            {2, 1, 0, -1, -2},
            {2, 1, 0, -1, -2},
            {2, 1, 0, -1, -2},
            {2, 1, 0, -1, -2}};



    public static final int[][] rightDiagonalEdgeMatrix = {
            {3, 3, 2, 1, 0},
            {3, 2, 1, 0, -1},
            {2, 1, 0, -1, -2},
            {1, 0, -1, -2, -3},
            {0, -1, -2, 3, -3}};

    public static final int[][] leftDiagonalEdgeMatrix = {{0, 1, 1},
                                                          {-1, 0, 1},
                                                          {-1, -1, 0}};

    public static final int[][] identityMatrix = {{0, 0, 0},
                                                  {0, 1, 0},
                                                  {0, 0, 0}};

    public static final int[][] laplaceMatrix = {{0, -1, 0},
                                                 {-1, 4, -1},
                                                 {0, -1, 0}};
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
        convertToLuminosity();
    }

    public ImageData(int[][] colorData, String filename) {
        name = filename;
        width = colorData.length;
        height = colorData[0].length;
        Color2d = colorData;
    }

    // Begin bit-level magic
    public static int getRed(int pixel) {
        return ((pixel & redMask)>>16);
    }

    public static int getGreen(int pixel) {
        return (pixel & greenMask) >> 8;
    }

    public static int getBlue(int pixel) {
        return ((pixel & blueMask));
    }

    public static int getAlpha(int pixel) {
        return ((pixel & alphaMask) >> 24);
    }

    public static int newPixel(int r, int g, int b, int a) {
        int pixel = 0;
        pixel |= (a & 0xff) << 24;
        pixel |= (r & 0xff) << 16;
        pixel |= (g & 0xff) << 8;
        pixel |= (b & 0xff);
        return pixel;
    }
    // End bit-level magic

    public static int averagePixel(int pixelOne, int pixelTwo) {
        int r = (getRed(pixelOne) + getRed(pixelTwo)) / 2;
        int g = (getGreen(pixelOne) + getGreen(pixelTwo)) / 2;
        int b = (getBlue(pixelOne) + getBlue(pixelTwo)) / 2;
        return newPixel(r, g, b, 0xff);
    }

    //Finds the average color in a floodfilled area.
    public int averageAreaColor(int x, int y, int tolerance){
        int topBound = y;
        int bottomBound = y;
        int rightBound = x;
        int leftBound = x;
        System.out.println("Starting fill at "+x+", "+y);
        Stack<Integer[]> stack = new Stack<Integer[]>();
        boolean[][] visited = new boolean[width][height];
        int totalPixels = 0;
        stack.push(new Integer[]{x,y});
        int[] totalsRGB = new int[3];

        while(!stack.isEmpty()){
            Integer[] current = stack.pop();
            x = current[0];
            y = current[1];

            if(!visited[x][y]) {
                if(x<leftBound) leftBound = x;
                if(x>rightBound) rightBound = x;
                if(y<topBound) topBound = y;
                if(y>bottomBound) bottomBound = y;
                totalPixels++;
                visited[x][y] = true;
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (i != j && x + i >= 0 && x + i < width && y + j >= 0 && y + j < height
                                && colorDifference(Color2d[x + i][y + j], Color2d[x][y]) <= tolerance) {
                            stack.push(new Integer[]{x + i, y + j});
                        }
                    }
                }
                totalsRGB[0] += getRed(Color2d[x][y])&0xFF;
                totalsRGB[1] += getGreen(Color2d[x][y])&0xFF;
                totalsRGB[2] += getBlue(Color2d[x][y])&0xFF;
            }
        }
        System.out.println(bottomBound+" "+topBound+" "+leftBound+" "+rightBound);
        System.out.println(totalsRGB[0]+" "+totalsRGB[1]+" "+totalsRGB[2]);
        System.out.println(totalPixels);
        return newPixel((totalsRGB[0]/totalPixels), (totalsRGB[1]/totalPixels),
                (totalsRGB[2]/totalPixels),0xFF);
    }

    public static int colorDifference(int color1, int color2){
        int red1 = getRed(color1)&0xFF;
        int red2 = getRed(color2)&0xFF;
        int redDiff = Math.abs((getRed(color1)&0xFF)-(getRed(color2)&0xFF));
        int greenDiff = Math.abs((getGreen(color1)&0xFF)-(getGreen(color2)&0xFF));
        int blueDiff = Math.abs((getBlue(color1)&0xFF)-(getBlue(color2)&0xFF));
        int result = redDiff+greenDiff+blueDiff;
        return result;
    }

    public String toString(){
        String result = "";
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                result+=Integer.toHexString(Color2d[x][y])+" ";
            }
            result+="\n";
        }
        return result;
    }

    // Returns a byte array for network streaming
    public byte[] toByteArray() {
        byte[] unpackedImage = new byte[width * height * 4];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = Color2d[x][y];
                unpackedImage[(x + y * width) * 4] = (byte)getRed(pixel);
                unpackedImage[(x + y * width) * 4 + 1] = (byte)getGreen(pixel);
                unpackedImage[(x + y * width) * 4 + 2] = (byte)getBlue(pixel);
                unpackedImage[(x + y * width) * 4 + 3] = (byte)getAlpha(pixel);
            }
        }
        return unpackedImage;
    }

    // Finds the average color of a pixel and its 8 surrounding pixels
    public int averageColor(int x, int y) {
        int[][] pixelWeight = {
                                { 1, 1, 1 },
                                { 1, 4, 1 },
                                { 1, 1, 1 }};
        return convolutePixel(x, y, pixelWeight);
    }

    private int convolutePixel(int x, int y, int pixelWeight[][]) {
        int length = pixelWeight.length;
        if (length % 2 == 0 || length != pixelWeight[0].length)
            throw new IllegalArgumentException("Convolution matrix must be square with odd sides");

        int[][] subArray = new int[length][length];
        for (int i = -(length / 2); i <= length / 2; i++) {
            for (int j = -(length / 2); j <=length / 2; j++) {
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
                subArray[i + length / 2][j + length / 2] = Color2d[indx][indy];
            }
        }
        int[] colorSums = new int[4];
        int totalWeight = 0;
        int posWeight = 0;
        int negWeight = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                colorSums[0] += (getRed(subArray[i][j])) * pixelWeight[i][j];
                colorSums[1] += (getGreen(subArray[i][j])) * pixelWeight[i][j];
                colorSums[2] += (getBlue(subArray[i][j])) * pixelWeight[i][j];
                if (pixelWeight[i][j] > 0)
                    posWeight += pixelWeight[i][j];
                else if (pixelWeight[i][j] < 0)
                    negWeight += pixelWeight[i][j];
            }
        }
        totalWeight = Math.max(posWeight, Math.abs(negWeight));
        return newPixel((colorSums[0] / totalWeight), (colorSums[1] / totalWeight),
                (colorSums[2] / totalWeight), 0xff);
    }

    public ImageData newConvoluteImage(int[][] convolutionMatrix) {
        int[][] rawData = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                rawData[x][y] = convolutePixel(x, y, convolutionMatrix);
            }
        }
        return new ImageData(rawData, name);
    }

    public void convoluteImage(int[][] convolutionMatrix) {
        int[][] rawData = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                rawData[x][y] = convolutePixel(x, y, convolutionMatrix);
            }
        }
        Color2d = rawData;
    }

    public void convertToLuminosity() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = Color2d[x][y];
                int luminance = 299 * (getRed(pixel)) + 587 * ( getGreen(pixel)) + 114 * (getBlue(pixel));
                luminance /= 1000;
                Color2d[x][y] = newPixel(luminance, luminance, luminance, 0xff);
            }
        }
        // Luminosity = 0.2126*R + 0.7152*G + 0.0722*B
    }
    public BufferedImage toBufferedImage() {
        int[] rawData = new int[width * height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                rawData[x + y * width] = Color2d[x][y];
            }
        }
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        img.setRGB(0, 0, width, height, rawData, 0, width);
        return img;
    }

    public void writeImage(String filename) {
        Path p = Paths.get(filename);
        try {
            ImageIO.write(toBufferedImage(), "png", p.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ImageData averageWith(ImageData img) {
        if (width != img.width || height != img.height)
            throw new IllegalArgumentException("Can only average two images of the same size");
        int[][] rawImage = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                rawImage[x][y] = averagePixel(Color2d[x][y], img.Color2d[x][y]);
            }
        }
        return new ImageData(rawImage, name);
    }
}
