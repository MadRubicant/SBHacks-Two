import java.awt.Color;
import java.util.*;

public class ColorWrapper extends Color{
    public ColorWrapper(int argb){
        super(argb);
    }
    private static class ColorName{
        int[] hueRange;
        String name;
        public ColorName(String name, int[] hueRange){
            this.name = name;
            this.hueRange = hueRange;
        }
    }
    public static ArrayList<ColorName> colorNames;
    static {
        colorNames = new ArrayList<ColorName>();
        colorNames.add(new ColorName("Red",new int[]{0,19}));
        colorNames.add(new ColorName("Orange",new int[]{19,44}));
        colorNames.add(new ColorName("Yellow",new int[]{44,66}));
        colorNames.add(new ColorName("Green",new int[]{66,150}));
        colorNames.add(new ColorName("Blue",new int[]{150,257}));
        colorNames.add(new ColorName("Purple",new int[]{257,299}));
        colorNames.add(new ColorName("Pink",new int[]{299,339}));
        colorNames.add(new ColorName("Red",new int[]{339,360}));
    }

    private int MSE(Color c){
        return ((int)Math.pow((getRed()-c.getRed()),2)+(int)Math.pow(getGreen()-c.getGreen(),2)
                +(int)Math.pow(getBlue()-c.getBlue(),2))/3;
    }

    public String closestColor(){
        String closest = "";
        float[] hsl = new float[3];
        Color.RGBtoHSB(getRed(),getGreen(),getBlue(),hsl);
        for(ColorName name : colorNames){
            if(name.hueRange[0]<=hsl[0]*360&&hsl[0]*360<=name.hueRange[1]){
                closest = name.name;
            }
        }
        if(hsl[1]<.1){
            if(hsl[2]<.1) closest = "Black";
            else if(hsl[2]>.9) closest = "White";
            else closest = "Gray";
        }
        return closest;
    }
}