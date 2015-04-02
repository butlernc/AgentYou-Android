package com.noahbutler.agentyou.fragments;

import android.app.Fragment;
import android.hardware.Camera;
import android.os.Bundle;
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

/**
 * Created by Noah Butler on 3/3/2015.
 */
public class IdentifyFragment extends Fragment{

    private FrameLayout cameraPreviewArea;
    private Button identifyButton;
    private MainActivity mainActivity;


    CameraPreview cameraPreview;

    Camera.PictureCallback mPicture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tag, container, false);

        mainActivity = (MainActivity)getActivity();

        cameraPreviewArea = (FrameLayout)rootView.findViewById(R.id.camera_preview_area);

        cameraPreview = new CameraPreview(getActivity().getBaseContext(), mainActivity.getCamera());

        cameraPreviewArea.addView(cameraPreview);

        mPicture = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                File pictureFile = Statics.getOutputMediaFile();
                if (pictureFile == null){
                    Log.d(Statics.LOG, "Error creating media file, check storage permissions: ");
                    return;
                }

                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();

                    Bundle bundle = new Bundle();
                    bundle.putString("location", pictureFile.getAbsolutePath());

                    WaitingFragment waitingFragment = new WaitingFragment();
                    waitingFragment.setArguments(bundle);
                    getActivity().getFragmentManager().beginTransaction().replace(R.id.main_content_area, waitingFragment).commit();


                } catch (FileNotFoundException e) {
                    Log.d(Statics.LOG, "File not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.d(Statics.LOG, "Error accessing file: " + e.getMessage());
                }
            }
        };

        identifyButton = (Button)rootView.findViewById(R.id.tag_button);
        identifyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // get an image from the camera
                Log.d("TAKE", "taking photo");
                mainActivity.getCamera().takePicture(null, null, mPicture);
            }
        });

        return rootView;

    }
}
