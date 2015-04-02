package com.noahbutler.agentyou.utilities.photo;

import java.util.Stack;

/**
 * Created by Noah Butler on 3/10/2015.
 *
 * This class will be used to create a pHash value from
 * a given picture that was taken by the agent.
 */
public class AgentHashCreator extends Thread {

    Stack<PictureHolder> pictureQueue;
    private String filePath;
    private String results;
    public AgentHashCreator() {
        pictureQueue = new Stack<>();
    }

    public AgentHashCreator(String filePath) {
        this.filePath = filePath;
    }

    /**
     * This methods pushes a pictureHolder object to the hash creator's queue.
     */
    public void queuePicture(PictureHolder pictureHolder) {
        pictureQueue.push(pictureHolder);
    }

    public boolean createHashValueFromImage() {
        ImagePHash imagePHash = new ImagePHash();
        try {
            results = imagePHash.getHash(filePath);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getResults() {
        return results;
    }
}
