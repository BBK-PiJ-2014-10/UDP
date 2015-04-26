import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class DataRelayClientImpl implements DataRelayClient {
    public boolean printReceivedData = false;
    private String id;
    private InetAddress serverIP;
    private static final String serverName = "localhost";
    private static final int TCPPort = 8888;
    private static final int clientUDPPort = 8888;
    private static final int serverUDPPort = 8887;
    private Socket TCPSocket;
    private DataOutputStream TCPOutput;
    private BufferedReader TCPInput;

    DataRelayClientImpl(boolean printReceivedData) {
        this.printReceivedData = printReceivedData;
    }

    /**
     * Send data in a loop over UDP
     */
    @Override
    public void streamData(String pathToFile) throws IOException {
        Path path = Paths.get(pathToFile);
        byte[] data = Files.readAllBytes(path);

        DatagramSocket UDPSocket = new DatagramSocket();
        DatagramPacket packet = new DatagramPacket(data, data.length, serverIP, serverUDPPort);
        while (true) {
            UDPSocket.send(packet);
        }
    }

    /**
     * Receive data in a loop via UDP
     */
    @Override
    public void receiveData() throws IOException {
        byte[] data = new byte[1024];
        DatagramSocket UDPSocket = new DatagramSocket(clientUDPPort);
        DatagramPacket packet = new DatagramPacket(data, data.length, serverIP, clientUDPPort);

        while (true) {
            UDPSocket.receive(packet);
            if (printReceivedData) {
                System.out.println("Data received: " + new String(packet.getData()));
            }
        }
    }

    /**
     * Initialize TCP connection
     */
    @Override
    public void initializeTCP() throws IOException {
        TCPSocket = new Socket(serverName, TCPPort);
        TCPOutput = new DataOutputStream(TCPSocket.getOutputStream());
        TCPInput = new BufferedReader(new InputStreamReader(TCPSocket.getInputStream()));
    }

    /**
     * Initialize UDP connection
     */
    @Override
    public void initializeUDP() throws IOException {

        serverIP = InetAddress.getByName(serverName);
    }

    /**
     * Get client ID from the server
     */
    @Override
    public void setID() throws IOException {
        TCPOutput.writeBytes(Signal.GET_ID.getSignal() + "\n");
        id = TCPInput.readLine();
    }

    /**
     * Check if client is first to connect
     */
    @Override
    public boolean isFirstToConnect() throws IOException {
        System.out.println("Checking if there is a broadcaster already...");
        TCPOutput.writeBytes(Signal.CHECK_IF_FIRST.getSignal() + "\n");
        String response = TCPInput.readLine();
        System.out.println("Server response: " + response);

        if (response.equals(Signal.BROADCASTER_PRESENT.getSignal())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Initialize the client
     */
    @Override
    public void initialize() {
        try {
            System.out.println("Connecting to server " + serverName + ":" + TCPPort + "...");
            initializeTCP();
            System.out.println("Getting ID from server...");
            setID();
            System.out.println("ID received from the server: " + id);
            initializeUDP();

            if (isFirstToConnect()) {
                System.out.println("Broadcasting data...");
                streamData("test_data.txt");
            } else {
                System.out.println("Receiving data...");
                receiveData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
