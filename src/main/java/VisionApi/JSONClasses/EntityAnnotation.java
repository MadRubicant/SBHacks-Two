package VisionApi.JSONClasses;

/**
 * Created by sierra on 4/22/16.
 */
public class EntityAnnotation {
    public String mid;
    public String locale;
    public String description;
    public int score;
    public double confidence;
    public double topicality;
    public BoundingPoly boundingPoly;
    public LocationInfo locations;
    public Property properties;
}
