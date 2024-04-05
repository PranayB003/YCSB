import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
    public static void main(String[] args) {
        String serverUrl = "http://40.80.84.73:8080";

        // Send HTTP PUT request
        sendPutRequest(serverUrl, "hello=world");
        sendPutRequest(serverUrl, "swapnil=tatiya");
        sendPutRequest(serverUrl, "amit=pandit");

        // Send HTTP GET request
        sendGetRequest(serverUrl, "hello");

        // Send HTTP DELETE request
        sendDeleteRequest(serverUrl, "hue");

        // Send HTTP GET request
        sendGetRequest(serverUrl, "hello");
    }

    private static void sendGetRequest(String serverUrl, String data) {
        try {
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            System.out.println("GET Response Code: " + responseCode);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response;
            StringBuilder responseData = new StringBuilder();
            while ((response = reader.readLine()) != null) {
                responseData.append(response);
            }
            reader.close();

            System.out.println("GET Response Data: " + responseData.toString());

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendPutRequest(String serverUrl, String data) {
        try {
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            System.out.println("PUT Response Code: " + responseCode);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response;
            StringBuilder responseData = new StringBuilder();
            while ((response = reader.readLine()) != null) {
                responseData.append(response);
            }
            reader.close();

            System.out.println("PUT Response Data: " + responseData.toString());

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendDeleteRequest(String serverUrl, String data) {
        try {
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setDoOutput(true);

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            System.out.println("DELETE Response Code: " + responseCode);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response;
            StringBuilder responseData = new StringBuilder();
            while ((response = reader.readLine()) != null) {
                responseData.append(response);
            }
            reader.close();

            System.out.println("DELETE Response Data: " + responseData.toString());

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
