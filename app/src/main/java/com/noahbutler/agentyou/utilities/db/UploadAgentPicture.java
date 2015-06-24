package com.noahbutler.agentyou.utilities.db;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.noahbutler.agentyou.data.Statics;
import com.noahbutler.agentyou.utilities.photo.FaceLinker;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Noah Butler on 4/26/2015.
 */
public class UploadAgentPicture extends AsyncTask<String, Integer, Boolean> {

    /* image directory on blue host */
    private static final String BH_IMAGE_DIRECTORY = "http://www.schasta.com/agentyou/FaceAPIInterface/save_file.php";
    private String fileName = null;
    private String agentEmail;

    public boolean uploadFile(String sourceFileUri) {


        fileName = sourceFileUri.substring(sourceFileUri.lastIndexOf('/') + 1, sourceFileUri.length());
        Log.d(Statics.LOG, "File Name: " + fileName);
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {
            Log.e("uploadFile", "Source File not exist");
            return false;
        } else {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(BH_IMAGE_DIRECTORY);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"test\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes("test");
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName +"\"" + lineEnd);
                dos.writeBytes(lineEnd);

                Log.e(Statics.LOG, "Headers are written");


                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necessary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // close streams
                fileInputStream.close();

                dos.flush();

                Log.e(Statics.LOG,"File Sent, Response: "+String.valueOf(conn.getResponseCode()));

                InputStream is = conn.getInputStream();

                // retrieve the response from server
                int ch;

                StringBuffer b = new StringBuffer();
                while( ( ch = is.read() ) != -1 ){ b.append( (char)ch ); }
                String s=b.toString();
                Log.i("Response", s);
                dos.close();

            } catch (MalformedURLException ex) {

                ex.printStackTrace();

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                e.printStackTrace();

//                Log.e("Upload file to server Exception", "Exception : "
//                        + e.getMessage(), e);
            }

        }
        return true;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        boolean finished = uploadFile(strings[0]);
        Log.d(Statics.LOG, "Finished uploading picture: " + Boolean.toString(finished));
        return finished;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        Log.d(Statics.LOG, "File: " + fileName + ", has been uploaded to the bluehost server!");
        Log.d(Statics.LOG, "Now sending info to api");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                FaceLinker faceLinker = new FaceLinker();
                boolean stored = faceLinker.enrollFaceFromImage(agentEmail, fileName);
                Log.d(Statics.LOG, Boolean.toString(stored));
            }
        }).start();

    }

    public void setAgentEmail(String agentEmail) {
        this.agentEmail = agentEmail;
    }
}
