package site.ycsb.db;

import site.ycsb.ByteIterator;
import site.ycsb.DB;
import site.ycsb.DBException;
import site.ycsb.Status;
import site.ycsb.StringByteIterator;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import java.util.Vector;
import java.util.HashMap;
import java.util.Properties;

/**
 * YCSB binding for BalanceDB.
 */
public class BalanceDBClient extends DB {
  private HttpClient client;
  public static final String HOST_PROPERTY = "balance.hosts";
  public static final String PORT_PROPERTY = "balance.port";

  public void init() throws DBException {
    Properties props = getProperties();
    int port;

    String portString = props.getProperty(PORT_PROPERTY);
    if (portString != null) {
      port = Integer.parseInt(portString);
    } else {
      port = 8080;
    }

    String hosts = props.getProperty(HOST_PROPERTY);
    String[] hostsArray = hosts.split(",");
    ArrayList<String> ipAddressList = new ArrayList<>(Arrays.asList(hostsArray));

    // Append port to all IP addresses
    for (int i = 0; i < ipAddressList.size(); i++) {
      String ipAddress = ipAddressList.get(i);
      //ipAddressList.set(i, "http://" + ipAddress + ":" + port);
      ipAddressList.set(i, "http://" + ipAddress);
    }

    client = new HttpClient(ipAddressList);
  }

  private String mapToPayload(String key, Map<String, String> map) {
    StringBuilder sb = new StringBuilder();

    sb.append(key).append("=");
    for (Map.Entry<String, String> entry : map.entrySet()) {
      sb.append("$FLD$").append(entry.getKey()).append("$VAL$").append(entry.getValue());
    }

    return sb.toString();
  }

  private Map<String, String> stringToMap(String str) {
    Map<String, String> map = new HashMap<>();
    String[] parts = str.split("\\$FLD\\$|\\$VAL\\$");

    for (int i = 1; i < parts.length; i += 2) {
      map.put(parts[i], parts[i + 1]);
    }

    return map;
  }

  private void mapSubset(Map<String, String> map, Set<String> fields) {
    // Create a copy of the original map to avoid ConcurrentModificationException
    Map<String, String> copyMap = new HashMap<>(map);

    // Iterate over the original map and remove entries with keys not present in the Set of fields
    for (String key : copyMap.keySet()) {
      if (!fields.contains(key)) {
        map.remove(key);
      }
    }
  }

  @Override
  public Status insert(String table, String key, Map<String, ByteIterator> values) {
    Map<String, String> strValues = StringByteIterator.getStringMap(values); 

    String data = mapToPayload(key, strValues);
    if (client.sendPutRequest(data).equals("OK")) {
      return Status.OK;
    }
    return Status.ERROR;
  }

  @Override
  public Status read(String table, String key, Set<String> fields, Map<String, ByteIterator> result) {
    String data = client.sendGetRequest(key);
    if (data.contains("ERROR:")) {
      return Status.ERROR;
    } else {
      Map<String, String> values = stringToMap(data);
      if (fields != null) {
        mapSubset(values, fields);
      }
      StringByteIterator.putAllAsByteIterators(result, values);
    }
    return result.isEmpty() ? Status.ERROR : Status.OK; 
  }

  @Override
  public Status delete(String table, String key) {
    client.sendDeleteRequest(key);
    return Status.OK;
  }

  @Override
  public Status update(String table, String key, Map<String, ByteIterator> values) {
    String data = client.sendGetRequest(key);
    if (data.contains("ERROR:")) {
      return Status.ERROR;
    } else {
      Map<String, String> allValues = stringToMap(data); // Deserialisation
      Map<String, String> updates = StringByteIterator.getStringMap(values); 
      // Update fields present in the values param (converted to 'updates')
      for (Map.Entry<String, String> entry : updates.entrySet()) {
        allValues.put(entry.getKey(), entry.getValue());
      }
      String updatedData = mapToPayload(key, allValues); // Serialisation
      if (client.sendPutRequest(updatedData).equals("OK")) {
        return Status.OK;
      }
      return Status.ERROR;
    }
  }

  @Override
  public Status scan(String table, String startkey, int recordcount,
      Set<String> fields, Vector<HashMap<String, ByteIterator>> result) {
    // Get list of keys to perform read on
//    String res = client.sendGetRequest("__KEY_RANGE__,".append(startKey).append(recordCount));
//    String[] keys = res.split(",");
//    HashMap<String, ByteIterator> values;
//    for (String key : keys) {
//        values = new HashMap<String, ByteIterator>();
//        read(table, key, fields, values);
//        result.add(values);
//    }

    return Status.OK;
  }
}
