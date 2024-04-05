package site.ycsb.db;

public class BalancedbClient extends DB {
  public static final String HOST_PROPERTY = "balance.host";
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
    String host = props.getProperty(HOST_PROPERTY);
  }
