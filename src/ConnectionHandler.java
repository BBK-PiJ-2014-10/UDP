import java.io.IOException;

/**
 * Handles client connection in a separate thread.
 *
 * 1. Send unique ID to client
 * 2. Indicate to client if it is a sender or receiver process
 * 3. Listen for UDP connection
 * 4. Tell client to connect over UDP
 * 5. Relay Audio data
 */
public interface ConnectionHandler {

    /**
     * Returns a unique ID
     *
     * @return unique ID string
     */
    public String getUniqueID();

    /**
     * Handle the initialization signals from the client
     */
    public void initializeClientConnection() throws IOException;

    /**
     * Relays data sent by the broadcaster to all receivers.
     */
    public void relayBroadcast() throws IOException;

}
