/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 *
 * @author Georgiana
 */
public class RESTCall {

    private static String REST_API_URL = "";

    public RESTCall(String url) {
        REST_API_URL = url;
    }
    private static final Logger LOGGER = Logger.getLogger(EventProcessingM2MInterraction.class);

    public String callPutMethod(String baseIP, int basePort, String methodName, String body, String contentType, String acceptType) {
        String answer = "";

        URL url = null;
        HttpURLConnection connection = null;

        try {
               // answer+= "rest api " + REST_API_URL;

            url = new URL(REST_API_URL + "/" + methodName);
            //answer+= url;
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", contentType);
            connection.setRequestProperty("Accept", acceptType);
               // answer += "connection created to rest ";
            //write message body
            OutputStream os = connection.getOutputStream();
            os.write(body.getBytes());
            os.flush();
            os.close();

            InputStream errorStream = connection.getErrorStream();
            if (errorStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream));
                String line;
                answer += "~~~~~~~~~~~~~~~Error~~~~~~~~~~~~~";
                while ((line = reader.readLine()) != null) {
                    answer += line;
                }
            }

            InputStream inputStream = connection.getInputStream();
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                answer += "~~~~~~~~~~~~~~~InputStream~~~~~~~~~~~~~";
                while ((line = reader.readLine()) != null) {
                    answer += line;
                }
            }

        } catch (Exception e) {
            answer += "Exception: " + e.getMessage();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }

        }
        return answer;
    }

    public String callPostMethod(String baseIP, int basePort, String methodName, String body, String contentType, String acceptType) {
        URL url = null;
        String response = "";
        HttpURLConnection connection = null;

        try {
            url = new URL(REST_API_URL + "/" + methodName);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", contentType);
            connection.setRequestProperty("Accept", acceptType);

            //write message body
            OutputStream os = connection.getOutputStream();
            os.write(body.getBytes());
            os.flush();
            os.close();

            InputStream errorStream = connection.getErrorStream();
            if (errorStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream));
                String line;
                response += "~~~~~~~~~~~~~~~Error~~~~~~~~~~~~~";
                while ((line = reader.readLine()) != null) {
                    response += line;
                }
            }

            InputStream inputStream = connection.getInputStream();
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                response += "~~~~~~~~~~~~~~~InputStream~~~~~~~~~~~~~";
                while ((line = reader.readLine()) != null) {
                    response += line;
                }
            }

        } catch (Exception e) {
            response += "Exception: " + e.getMessage();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return response;
    }
}
