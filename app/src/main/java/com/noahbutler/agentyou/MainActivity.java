package com.noahbutler.agentyou;


import android.hardware.Camera;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.luxand.FSDK;
import com.noahbutler.agentyou.fragments.LoginFragment;
import com.noahbutler.agentyou.utilities.camera.CameraSource;
import com.noahbutler.agentyou.utilities.photo.FaceLinker;
import com.noahbutler.agentyou.utilities.threads.HandlerPool;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    ArrayList<String> hashArray;
    Camera camera;
    Camera frontCamera;
    String FSDK_LICENSE_KEY = "sJpqj21uS2L9ETomNFo3c6HqfGPGeXokOBQq+hfMEKP1EX+9aeSmVG3jgzqCqk/PcZT5W486jfe+yiln7Zp3skCwkAZS6b3LgAincXuOYp9cL4nazvjqMymFKdTjHOzODRGDH+qzQ9cDAedLsim7byrF6lP5vNF/nngjYJIJ/EA=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HandlerPool.faceDataHandler = new HandlerPool.FaceDataHandler();

        FSDK.ActivateLibrary(FSDK_LICENSE_KEY);
        FSDK.Initialize();

        /* grab camera from source
            TODO: figure out how to switch cameras in the app
         */
        camera = CameraSource.getCameraInstance();
        getFragmentManager().beginTransaction().replace(R.id.main_content_area, new LoginFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Camera getCamera() {
       return camera;
    }

    public Camera getFrontFaceCamera() {
        //TODO: fix this

        if(camera != null) {
            camera.release();
        }

        if(frontCamera != null) {
            frontCamera.release();
        }
        frontCamera = CameraSource.openFrontFacingCamera();
        return frontCamera;
    }

    public ArrayList<String> getHashArray() {
        return this.hashArray;
    }

    public void setHashArray(ArrayList<String> hashArray) {
        this.hashArray = hashArray;
    }
}
