import java.awt.Color;
import java.util.*;

public class ColorWrapper extends Color{
    public ColorWrapper(int argb){
        super(argb);
    }
    private static class ColorName{
        Color color;
        String name;
        public ColorName(String name, Color color){
            this.name = name;
            this.color = color;
        }
    }
    public static ArrayList<ColorName> colorNames;
    static {
        colorNames = new ArrayList<ColorName>();
        colorNames.add(new ColorName("Red",new Color(255,0,0)));
        colorNames.add(new ColorName("Pink",new Color(255,133,196)));
        colorNames.add(new ColorName("Orange",new Color(255,138,0)));
        colorNames.add(new ColorName("Yellow",new Color(255,255,0)));
        colorNames.add(new ColorName("Green",new Color(0,255,0)));
        colorNames.add(new ColorName("Blue",new Color(0,0,255)));
        colorNames.add(new ColorName("Purple",new Color(192,0,255)));
        colorNames.add(new ColorName("Brown",new Color(135,77,41)));
        colorNames.add(new ColorName("Black",new Color(0,0,0)));
        colorNames.add(new ColorName("Gray",new Color(128,128,128)));
        colorNames.add(new ColorName("White",new Color(255,255,255)));
    }
    private int MSE(Color c){
        return ((int)Math.pow((getRed()-c.getRed()),2)+(int)Math.pow(getGreen()-c.getGreen(),2)
                +(int)Math.pow(getBlue()-c.getBlue(),2))/3;
    }

    public String closestColor(){
        String closest = "";
        int minMSE = MSE(colorNames.get(0).color);
        closest = colorNames.get(0).name;
        for(ColorName name : colorNames){
            System.out.println(name.name);
            int newMSE = MSE(name.color);
            if(newMSE<minMSE){
                closest = name.name;
                minMSE = newMSE;
            }
        }
        return closest;
    }
}