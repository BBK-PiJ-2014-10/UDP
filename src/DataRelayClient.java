import java.io.IOException;

/**
 * The audio relay client connects to the server and either sends audio or receives it.
 *
 * 1. Client connects to server (TCP)
 * 2. Asks for Unique ID
 * 3. Asks if its the first to connect
 * 4. Open UDP connection to server
 * (a) If first client: send audio (in chunks) to server
 * (b) If not first client: listen for audio chunks coming in on UDP and play audio
 */
public interface DataRelayClient {

    /**
     * Send data in a loop over UDP
     */
    public void streamData(String pathToFile) throws IOException;

    /**
     * Receive data in a loop via UDP
     */
    public void receiveData() throws IOException;

    /**
     * Initialize TCP connection
     */
    public void initializeTCP() throws IOException;
    /**
     * Initialize UDP connection
     */
    public void initializeUDP() throws IOException;

    /**
     * Get client ID from the server
     */
    public void setID() throws IOException;

    /**
     * Check if client is first to connect
     */
    public boolean isFirstToConnect() throws IOException;

    /**
     * Initialize the client
     */
    public void initialize() throws Exception;
}