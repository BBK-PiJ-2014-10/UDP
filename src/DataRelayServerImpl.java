import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DataRelayServerImpl implements DataRelayServer {

    private ServerSocket TCPServer;
    public static final int TCPPort = 8888;
    boolean printRelayedData = true;

    DataRelayServerImpl(boolean printRelayedData) {
        this.printRelayedData = printRelayedData;
    }

    /**
     * Listen for clients over TCP
     */
    @Override
    public void initialize() throws IOException {
        TCPServer = new ServerSocket(TCPPort);

        while (true) {
            System.out.println("Awaiting connection...");
            Socket connection = TCPServer.accept();
            new Thread(new ConnectionHandlerImpl(connection, printRelayedData)).start();
        }
    }
}