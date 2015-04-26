/**
 * Runs audio relay server.
 */
public class RunServer {
    public static void main(String[] args) throws Exception {
        DataRelayServer dataRelayServer = new DataRelayServerImpl();
        dataRelayServer.initialize();
    }
}
