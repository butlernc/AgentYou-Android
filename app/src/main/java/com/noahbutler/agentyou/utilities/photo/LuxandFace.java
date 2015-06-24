package com.noahbutler.agentyou.utilities.photo;

import android.util.Log;

import com.luxand.FSDK;
import com.noahbutler.agentyou.data.Statics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Noah Butler on 5/5/2015.
 */
public class LuxandFace {

    public FSDK.FSDK_FaceTemplate getFaceTemplateFromImage(String path) {
        /* initialize the image for FSDK */
        FSDK.HImage image = new FSDK.HImage();
        FSDK.LoadImageFromFile(image, path);

        /* grab our facial features from the image */
        FSDK.FSDK_FaceTemplate facialFeatures = new FSDK.FSDK_FaceTemplate();
        int r = FSDK.GetFaceTemplate(image, facialFeatures);

//        for(int i = 0; i < facialFeatures.template.length; i++) {
//            Log.d(Statics.LOG, "faceTemplate: [" + i + "], " + "[" + facialFeatures.template[i] + "]");
//        }

        return facialFeatures;
    }

    public int match(FSDK.FSDK_FaceTemplate face1, FSDK.FSDK_FaceTemplate face2) {
        float[] similarity = new float[10];
        return FSDK.MatchFaces(face1, face2, similarity);
    }

    /**
     * After getting the facial features from an image, we need to convert it to a string to save
     * on our database. The data is linked to the agent's email.
     * @param fileLocation
     * @return
     */
    public HashMap<String, String> prepImageForDB(String fileLocation) {

        FSDK.HImage image = new FSDK.HImage();
        FSDK.LoadImageFromFile(image, fileLocation);

        int bufferLength[] = new int[1];
        byte buffer[];
        FSDK.FSDK_IMAGEMODE imageMode = new FSDK.FSDK_IMAGEMODE();
        if((FSDK.FSDKE_OK == FSDK.GetImageBufferSize(image, bufferLength, imageMode))) {
            buffer = new byte[bufferLength[0]];
            FSDK.SaveImageToBuffer(image, buffer, imageMode);
        }else {
            return null;
        }

        StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bufferLength[0]; i++) {
                sb.append(buffer[i]);
                sb.append(',');
            }

        HashMap<String, String> returnData = new HashMap<>();
        returnData.put("data", sb.toString());
        returnData.put("bufferLength", String.valueOf(bufferLength[0]));

        return returnData;
    }

    /**
     * Creates a single Facial Feature object out of the strings that come from the FACEDATA_1 column in the
     * AGENT_FACEDATA Table.
     * @param inputData
     * @return
     */
    public FSDK.FSDK_FaceTemplate prepImageDataFromDB(HashMap<String, String> inputData) {
        FSDK.HImage image = new FSDK.HImage();
        FSDK.FSDK_FaceTemplate faceTemplate = new FSDK.FSDK_FaceTemplate();

        String data = inputData.get("data");
        int bufferLength = Integer.parseInt(inputData.get("bufferLength"));

        String[] in = data.split(",");
        byte[] buffer = new byte[in.length];
        for (int i = 0; i < in.length; i++) {
            buffer[i] = Byte.parseByte(in[i]);
            Log.d(Statics.LOG, "buffer[" + i + "]: " + buffer[i] + ", database:" + in[i]);
        }

        FSDK.LoadImageFromJpegBuffer(image, buffer, bufferLength);
        FSDK.GetFaceTemplate(image, faceTemplate);

        return faceTemplate;
    }

}
