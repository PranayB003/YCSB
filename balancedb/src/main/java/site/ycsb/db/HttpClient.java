package site.ycsb.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class to connect with the BalanceDB instance over HTTP.
 */
public class HttpClient {
  private ArrayList<String> hostnames;

  public HttpClient(ArrayList<String> hostnames) {
    this.hostnames = hostnames;
  }

  private String getRandomServerUrl() {
    Random random = new Random();
    int index = random.nextInt(hostnames.size());
    return hostnames.get(index);
  }

  public String sendGetRequest(String data) {
    String serverUrl = getRandomServerUrl();
    return sendRequest("GET", serverUrl, data);
  }

  public String sendPutRequest(String data) {
    String serverUrl = getRandomServerUrl();
    return sendRequest("PUT", serverUrl, data);
  }

  public String sendDeleteRequest(String data) {
    String serverUrl = getRandomServerUrl();
    return sendRequest("DELETE", serverUrl, data);
  }

  private String sendRequest(String method, String serverUrl, String data) {
    try {
      URL url = new URL(serverUrl);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod(method);
      connection.setDoOutput(true);

      // Send data in request
      OutputStream outputStream = connection.getOutputStream();
      outputStream.write(data.getBytes());
      outputStream.flush();
      outputStream.close();

      //int responseCode = connection.getResponseCode();
      //System.out.println(method + " Response Code: " + responseCode);

      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String response;
      StringBuilder responseData = new StringBuilder();
      while ((response = reader.readLine()) != null) {
        responseData.append(response);
      }

      reader.close();
      connection.disconnect();
      return responseData.toString();
    } catch (IOException e) {
      e.printStackTrace();
      return "ERROR: HttpClient.sendRequest() failed.";
    }
  }
}
