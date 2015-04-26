import java.io.IOException;

/**
 * The TCP server relays the audio stream to all the clients who connect after first, who sends the audio.
 *
 * 1. Server listens for clients (TCP)
 * 2. Client connects to server
 * 3. Server places client connection and further handling of client in a separate thread
 */
public interface DataRelayServer {

    /**
     * Listen for clients over TCP
     */
    public void initialize() throws IOException;
}