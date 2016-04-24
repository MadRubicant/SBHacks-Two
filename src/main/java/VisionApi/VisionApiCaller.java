package VisionApi;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionScopes;
import com.google.api.services.vision.v1.model.*;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Vertex;
import com.google.common.collect.ImmutableList;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * Created by john on 4/22/16.
 */
public class VisionApiCaller {
    public static final String VISION_API_URL = "https://vision.googleapis.com/v1/images:annotate";
    private static final String APPLICATION_NAME = "sbhacks2";

    Path imageToUpload;
    private Vision vision;

    public VisionApiCaller(Path image){
        imageToUpload = image;
    }

    public List<EntityAnnotation> sendApiRequest(){
        try {
            vision = getVisionService();
            AnnotateImageResponse annotations = detectText(20);
            List<EntityAnnotation> textAnnotations = annotations.getTextAnnotations();
            writeWithTextAnnotations(imageToUpload, Paths.get("out_img.jpg"), textAnnotations);

            for(EntityAnnotation textInstance : textAnnotations){
                System.out.println("Text:\n" + textInstance.get("description"));
            }

            return textAnnotations;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace(System.out);
            return null;
        }
    }

    private static Vision getVisionService() throws IOException, GeneralSecurityException {
        GoogleCredential credential =
                GoogleCredential.getApplicationDefault().createScoped(VisionScopes.all());
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        return new Vision.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public AnnotateImageResponse detectText(int maxResults) throws IOException {
        byte[] data = Files.readAllBytes(imageToUpload);

        LinkedList<Feature> features = new LinkedList<Feature>();
        features.add(new Feature()
                .setType("TEXT_DETECTION")
                .setMaxResults(maxResults));
        features.add(new Feature()
                .setType("LABEL_DETECTION")
                .setMaxResults(maxResults));
        features.add(new Feature()
                .setType("IMAGE_PROPERTIES")
                .setMaxResults(maxResults));

        AnnotateImageRequest request =
                new AnnotateImageRequest()
                        .setImage(new Image().encodeContent(data))
                        .setFeatures(features);
        Vision.Images.Annotate annotate =
                vision.images()
                        .annotate(new BatchAnnotateImagesRequest().setRequests(ImmutableList.of(request)));
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotate.setDisableGZipContent(true);

        BatchAnnotateImagesResponse batchResponse = annotate.execute();
        assert batchResponse.getResponses().size() == 1;
        AnnotateImageResponse response = batchResponse.getResponses().get(0);
        if (response.getTextAnnotations() == null) {
            throw new IOException(
                    response.getError() != null
                            ? response.getError().getMessage()
                            : "Unknown error getting image annotations");
        }
        return response;
    }

    private static void writeWithTextAnnotations(Path inputPath, Path outputPath, List<EntityAnnotation> entities)
            throws IOException {
        BufferedImage img = ImageIO.read(inputPath.toFile());
        annotateWithEntities(img, entities);
        ImageIO.write(img, "jpg", outputPath.toFile());
    }

    /**
     * Annotates an image {@code img} with a polygon around each face in {@code entities}.
     */
    public static void annotateWithEntities(BufferedImage img, List<EntityAnnotation> entities) {
        for (EntityAnnotation entity : entities) {
            annotateWithEntity(img, entity);
        }
    }

    /**
     * Annotates an image {@code img} with a polygon defined by {@code entity}.
     */
    private static void annotateWithEntity(BufferedImage img, EntityAnnotation entity) {
        Graphics2D gfx = img.createGraphics();
        Polygon poly = new Polygon();
        for (Vertex vertex : entity.getBoundingPoly().getVertices()) {
            poly.addPoint(vertex.getX(), vertex.getY());
        }
        gfx.setStroke(new BasicStroke(5));
        gfx.setColor(new Color(0x00ff00));
        gfx.draw(poly);
    }
}
