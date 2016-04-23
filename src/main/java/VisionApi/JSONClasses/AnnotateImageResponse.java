package VisionApi.JSONClasses;

/**
 * Created by sierra on 4/22/16.
 */
public class AnnotateImageResponse {

    public EntityAnnotation labelAnnotations;
    public EntityAnnotation textAnnotations;
    public EntityAnnotation landmarkAnnotations;
    public EntityAnnotation logoAnnotations;
    public ImageProperties imagePropertiesAnnotation;
    public Status error;

}