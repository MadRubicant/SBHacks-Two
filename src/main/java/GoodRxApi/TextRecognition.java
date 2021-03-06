package GoodRxApi;

import com.google.api.services.vision.v1.model.EntityAnnotation;

import java.util.List;

import GoodRxApi.JSONClasses.*;

import java.lang.*;


/**
 * Created by sierra on 4/23/16.
 */
public class TextRecognition {
    public static GoodRxResponse processTextWithGoodRx(List<EntityAnnotation> textAnnotations, String pillColor) {
        GoodRxResponse goodRxResponse;
        for(EntityAnnotation textInstance : textAnnotations) {
            String textIn = "" + textInstance.get("description");
            System.out.println("Text:\n" + textIn);
            //textIn = textIn.replace('\n', ' ');

            GoodRxApiCaller goodRxApiCaller = new GoodRxApiCaller(textIn, pillColor, "any");
            goodRxResponse = goodRxApiCaller.sendApiRequest(10);
            if(goodRxResponse != null)
                return goodRxResponse;
        }
        return null;
    }

}
