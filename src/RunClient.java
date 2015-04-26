/**
 * Runs audio relay client.
 */
public class RunClient {
    public static void main(String[] args) throws Exception {
        boolean printReceivedData = true;
        DataRelayClient dataRelayClient = new DataRelayClientImpl(printReceivedData);
        dataRelayClient.initialize();
    }
}
