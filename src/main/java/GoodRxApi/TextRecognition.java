package GoodRxApi;

import com.google.api.services.vision.v1.model.EntityAnnotation;

import java.util.List;
import GoodRxApi.JSONClasses.*;
import java.lang.*;



/**
 * Created by sierra on 4/23/16.
 */
public class TextRecognition {
    public static void processTextWithGoodRx(List<EntityAnnotation> textAnnotations){
        for(EntityAnnotation textInstance : textAnnotations) {
            String textIn = "" + textInstance.get("description");
            System.out.println("Text:\n" + textIn);

            GoodRxApiCaller goodRxApiCaller = new GoodRxApiCaller(textIn);
            GoodRxResponse goodRxResponse = goodRxApiCaller.sendApiRequest();
        }
    }

}
