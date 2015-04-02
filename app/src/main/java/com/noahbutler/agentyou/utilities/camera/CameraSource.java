package com.noahbutler.agentyou.utilities.camera;

import android.hardware.Camera;
import android.util.Log;

/**
 * Created by Noah Butler on 3/3/2015.
 *
 * This class and sub classes allow direct access to the camera and methods
 *
 * CameraController directly accesses this.
 */
public class CameraSource {

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch(Exception e) {

        }
        return c;
    }

    public static Camera openFrontFacingCamera() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    Log.e("CAMERA", "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }

        return cam;
    }

}
