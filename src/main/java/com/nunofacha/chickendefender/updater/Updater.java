package com.nunofacha.chickendefender.updater;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nunofacha.chickendefender.Main;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

class UpdateDataModel {
    public String name;
    public boolean active_development;
    public String current_version;
    public String last_update;
    public String download_location;
    public String update_location;
    public String operator_message;

}
@SuppressWarnings("ALL")
class HttpUtils {

    /**
     * Represents an HTTP connection
     */
    private static HttpURLConnection httpConn;

    /**
     * Makes an HTTP request using GET method to the specified URL.
     *
     * @param requestURL the URL of the remote server
     * @return An HttpURLConnection object
     * @throws IOException thrown if any I/O error occurred
     */
    public static String sendGetRequest(String requestURL, String accessToken)
            throws IOException {
        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setRequestProperty("Authorization", accessToken);


        httpConn.setDoInput(true); // true if we want to read server's response
        httpConn.setDoOutput(false); // false indicates this is a GET request
        int status = httpConn.getResponseCode();

        switch (status) {
            case 200:
            case 201:
                BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
                return sb.toString();
        }
        return "error";
    }

    /**
     * Makes an HTTP request using POST method to the specified URL.
     *
     * @param requestURL the URL of the remote server
     * @param params     A map containing POST data in form of key-value pairs
     * @return An HttpURLConnection object
     * @throws IOException thrown if any I/O error occurred
     */

    /**
     * Returns only one line from the server's response. This method should be
     * used if the server returns only a single line of String.
     *
     * @return a String of the server's response
     * @throws IOException thrown if any I/O error occurred
     */
    public static String readSingleLineRespone() throws IOException {
        InputStream inputStream = null;
        if (httpConn != null) {
            inputStream = httpConn.getInputStream();
        } else {
            throw new IOException("Connection is not established.");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));

        String response = reader.readLine();
        reader.close();

        return response;
    }

    /**
     * Returns an array of lines from the server's response. This method should
     * be used if the server returns multiple lines of String.
     *
     * @return an array of Strings of the server's response
     * @throws IOException thrown if any I/O error occurred
     */
    public static String[] readMultipleLinesRespone() throws IOException {
        InputStream inputStream = null;
        if (httpConn != null) {
            inputStream = httpConn.getInputStream();
        } else {
            throw new IOException("Connection is not established.");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
        List<String> response = new ArrayList<String>();

        String line = "";
        while ((line = reader.readLine()) != null) {
            response.add(line);
        }
        reader.close();

        return (String[]) response.toArray(new String[0]);
    }

    /**
     * Closes the connection if opened
     */
    public static void disconnect() {
        if (httpConn != null) {
            httpConn.disconnect();
        }
    }

}
public class Updater {
    final String UPDATE_URL = "https://raw.githubusercontent.com/nfacha/ChickenDefender/dev/meta.json";
    String CURRENT_VERSION = Main.VERSION;

    private final ObjectMapper objectMapper = new ObjectMapper();
    public UpdateDataModel updateData;

    public Updater() throws IOException {
        Main.logger.info("Starting update check!");
        Main.logger.info("Current version: "+CURRENT_VERSION);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        if(hasNewVersion()){
            Main.logger.info("There is a new update!");
            downloadUpdate();
        }
    }

    public boolean hasNewVersion() {
        try {
            String response = HttpUtils.sendGetRequest(UPDATE_URL, "");
            updateData = objectMapper.readValue(response, UpdateDataModel.class);
            Main.logger.info("Received remote version: " + updateData.current_version);
            return !CURRENT_VERSION.equals(updateData.current_version);
        } catch (IOException e) {
            Main.logger.severe("Failed to check for updates: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

    }

    public void downloadUpdate() {
        try {
            final String downloadURL = updateData.download_location;
            URL download = new URL(downloadURL);

            BufferedInputStream in = null;
            FileOutputStream fout = null;
            try {
                Main.logger.info("Downloading " + downloadURL);
                in = new BufferedInputStream(download.openStream());
                fout = new FileOutputStream("plugins/" + Main.plugin.getName() + ".jar");

                final byte data[] = new byte[1024];
                int count;
                while ((count = in.read(data, 0, 1024)) != -1) {
                    fout.write(data, 0, count);
                }
            } finally {
                if (in != null) {
                    in.close();
                }
                if (fout != null) {
                    fout.close();
                }
            }

            Main.logger.info("Update has been downloaded successfully!");
            Main.logger.info("The update will be installed the next time the server is restarted");
        } catch (IOException e) {
            e.printStackTrace();
            Main.logger.severe("Failed to download update");
        }
    }

}
