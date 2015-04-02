package com.noahbutler.agentyou.fragments;

import android.app.Fragment;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.noahbutler.agentyou.MainActivity;
import com.noahbutler.agentyou.R;
import com.noahbutler.agentyou.data.Statics;
import com.noahbutler.agentyou.utilities.camera.CameraPreview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Noah Butler on 3/19/2015.
 */
public class AgentHashCreatingFragment extends Fragment {

    private FrameLayout cameraPreviewArea;
    private Button captureButton;
    private MainActivity mainActivity;


    CameraPreview cameraPreview;

    Camera.PictureCallback mPicture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /* grab the root view that holds all of the layouts */
        View rootView = inflater.inflate(R.layout.fragment_tag, container, false);
        /* grab main activity object */
        mainActivity = (MainActivity)getActivity();
        /* grab the layout that will hold the camera preview */
        cameraPreviewArea = (FrameLayout)rootView.findViewById(R.id.camera_preview_area);
        /* create a view that will display a stream from the camera, taken from the main activity */
        cameraPreview = new CameraPreview(getActivity().getBaseContext(), mainActivity.getCamera());
        /* add the view to the layout on the screen */
        cameraPreviewArea.addView(cameraPreview);
        /* create our custom camera callback (gets call when a picture is taken) */
        mPicture = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                File pictureFile = Statics.getOutputMediaFile();
                if (pictureFile == null){
                    Log.d(Statics.LOG, "Error creating media file, check storage permissions: ");
                    return;
                }

                Log.d("HELLO", "HELLO SIR");

                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();

                    Bundle bundle = new Bundle();
                    bundle.putString("location", pictureFile.getAbsolutePath());
                    bundle.putString("agent", AgentHashCreatingFragment.this.getArguments().getString("agent"));

                    StorageFragment storageFragment = new StorageFragment();
                    storageFragment.setArguments(bundle);
                    getActivity().getFragmentManager().beginTransaction().replace(R.id.main_content_area, storageFragment).commit();


                } catch (FileNotFoundException e) {
                    Log.d(Statics.LOG, "File not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.d(Statics.LOG, "Error accessing file: " + e.getMessage());
                }
            }
        };

        captureButton = (Button)rootView.findViewById(R.id.tag_button);
        captureButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // get an image from the camera
                Log.d("TAKE", "taking photo");
                /* call back will take user to a new fragment */
                mainActivity.getCamera().takePicture(null, null, mPicture);
                Log.d("TAKE", "POST TAKE");

            }
        });


        return  rootView;
    }


}
