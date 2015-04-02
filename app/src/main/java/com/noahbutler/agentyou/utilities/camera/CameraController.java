package com.noahbutler.agentyou.utilities.camera;


/**
 * Created by Noah Butler on 3/3/2015.
 *
 * Class that links the CameraSource object to the IdentifyFragment.
 * Used as a bridge to allow IdentifyFragment is of access to the Camera.
 */
public class CameraController {

    CameraSource cameraSource;

    public CameraController() {
        cameraSource = new CameraSource();
    }

    public boolean createCamera() {
        boolean r = false;

        new Thread(new Runnable() {
            @Override
            public void run() {
                CameraSource.getCameraInstance();
            }
        });

        return r;
    }

}
