import java.awt.image.BufferedImage;

/**
 * Created by William Bennett on 4/22/2016.
 */
public class ImageData {
    public int[][] Color2d;
    public int width;
    public int height;
    public String name;

    public ImageData(BufferedImage inImage, String filename) {
        name = filename;
        width = inImage.getWidth();
        height = inImage.getHeight();
        Color2d = new int[width][height];
        int[] RawData = new int[width * height];
        inImage.getRGB(0, 0, width, height, RawData, 0, 0);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color2d[x][y] = RawData[x * width + y];
            }
        }

    }
}
