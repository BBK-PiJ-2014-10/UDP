/**
 * Runs audio relay server.
 */
public class RunServer {
    public static void main(String[] args) throws Exception {
        boolean printRelayedData = true;
        DataRelayServer dataRelayServer = new DataRelayServerImpl(printRelayedData);
        dataRelayServer.initialize();
    }
}
