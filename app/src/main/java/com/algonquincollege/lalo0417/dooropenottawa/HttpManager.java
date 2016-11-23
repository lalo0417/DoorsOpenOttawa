package com.algonquincollege.lalo0417.dooropenottawa;

/**
 * Created by CalebLalonde on 2016-11-08.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpManager {

    public static String getData(String uri) {

        BufferedReader reader = null;

        try {
            // open the URI
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // make a buffered reader
            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            // read the HTTP response from URI one-line-at-a-time
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            // return the HTTP response
            return sb.toString();
            // exception handling: a) print stack-trace, b) return null
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }
}
