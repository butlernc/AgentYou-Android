package com.noahbutler.agentyou.utilities.photo;

/**
 * Created by Noah Butler on 3/17/2015.
 */
public class AgentHashCompare {

    public int distance(String s1, String s2) {
        int counter = 0;
        for (int k = 0; k < s1.length(); k++) {
            if (s1.charAt(k) != s2.charAt(k)) {
                counter++;
            }
        }
        return counter;
    }
}
