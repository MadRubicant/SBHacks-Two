import java.awt.Color;
import java.util.*;

public class ColorWrapper extends Color{
    public ColorWrapper(int argb){
        super(argb);
    }
    private static class ColorName{

        String name;
        int[] hueRange;
        public ColorName(String name, int[] hueRange){
            this.name = name;
            this.hueRange = hueRange;

        }
    }
    public static ArrayList<ColorName> colorNames;
    static {
        colorNames = new ArrayList<ColorName>();
        colorNames.add(new ColorName("Red",new int[]{0,23}));
        colorNames.add(new ColorName("Orange",new int[]{23,62}));
        colorNames.add(new ColorName("Yellow",new int[]{62,68}));
        colorNames.add(new ColorName("Green",new int[]{68,158}));
        colorNames.add(new ColorName("Blue",new int[]{158,260}));
        colorNames.add(new ColorName("Purple",new int[]{260,300}));
        colorNames.add(new ColorName("Pink",new int[]{300,332}));
        colorNames.add(new ColorName("Red",new int[]{332,360}));
    }
    private int MSE(Color c){
        float[] hsl1 = new float[3];
        float[] hsl2 = new float[3];
        RGBtoHSB(getRed(),getGreen(),getBlue(),hsl1);
        RGBtoHSB(c.getRed(),c.getGreen(),c.getBlue(),hsl2);
        return 0;
    }

    public String closestColor(){
        System.out.println("Finding closest color to "+Integer.toHexString(getRGB()));
        String closest = "";
        float[] hsl = new float[3];
        RGBtoHSB(getRed(),getGreen(),getBlue(),hsl);
        closest = colorNames.get(0).name;
        for(ColorName name : colorNames){
            System.out.println(name.name);
            if((name.hueRange[0]<=hsl[0]*360)&&
                    (hsl[0]<=name.hueRange[1]*360)){
                closest = name.name;
            }
        }
        if(hsl[1]<.2) {
            if (hsl[2]<.2) closest = "Black";
            else if (hsl[2]>.8) closest = "White";
            else closest = "Gray";
        }
        return closest;
    }
}