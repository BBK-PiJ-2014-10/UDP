import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Handles client connection in a separate thread.
 *
 * 1. Send unique ID to client
 * 2. Indicate to client if it is a sender or receiver process
 * 3. Listen for UDP connection
 * 4. Tell client to connect over UDP
 * 5. Relay Audio data
 */
public class ConnectionHandlerImpl implements ConnectionHandler, Runnable {

    private Socket TCPSocketConnection;
    private BufferedReader TCPInputStream;
    private DataOutputStream TCPOutputStream;
    private boolean clientIsBroadcaster = false;
    public static boolean broadcasterConnected = false;
    public static List<InetAddress> receivers = new ArrayList<InetAddress>();
    public static final int serverUDPPort = 8887;
    public static final int clientUDPPort = 8888;
    private boolean printRelayedData = false;

    /**
     * Threaded server for handling client connection
     */
    ConnectionHandlerImpl(Socket connection, boolean printRelayedData) {
        this.TCPSocketConnection = connection;
        this.printRelayedData = printRelayedData;
    }

    /**
     * Returns a unique ID
     *
     * @return unique ID string
     */
    @Override
    public String getUniqueID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Handle the initialization signals from the client
     */
    @Override
    public void initializeClientConnection() throws IOException {
        System.out.println("Initializing new client...");
        TCPInputStream = new BufferedReader(new InputStreamReader(TCPSocketConnection.getInputStream()));
        TCPOutputStream = new DataOutputStream(TCPSocketConnection.getOutputStream());

        System.out.println("Waiting for signal from client...");
        String clientSignal = TCPInputStream.readLine();
        System.out.println("Received signal from client: " + clientSignal);

        if (clientSignal.equals(Signal.GET_ID.getSignal())) {
            String ClientID = getUniqueID();
            System.out.println("Sending unique ID to the client: " + ClientID);
            TCPOutputStream.writeBytes(ClientID + "\n");
        } else {
            TCPOutputStream.writeBytes("Incorrect signal received: " + clientSignal + "\n");
        }
    }

    /**
     * Relays data sent by the broadcaster to all receivers.
     */
    @Override
    public void relayBroadcast() throws IOException{

        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);

        System.out.println("Broadcasting client data...");

        DatagramSocket UDPReceiver = new DatagramSocket(serverUDPPort);
        DatagramSocket UDPBroadcaster = new DatagramSocket();

        while (true) {

            UDPReceiver.receive(packet);
            byte[] relayedData = packet.getData();
            if (printRelayedData) {
                System.out.println("Receivers: " + receivers);
                System.out.println("Relaying data: " + new String(relayedData));
            }

            for (InetAddress receiverIP : receivers) {
                DatagramPacket relayedPacket = new DatagramPacket(relayedData, relayedData.length, receiverIP, clientUDPPort);
                UDPBroadcaster.send(relayedPacket);
            }
        }
    }

    /**
     * Handles the client connection on a separate thread.
     * Either adds the client to broadcast receivers or starts the broadcast from her.
     */
    @Override
    public void run() {
        try {
            initializeClientConnection();

            String clientSignal = TCPInputStream.readLine();
            System.out.println("Received signal from client: " + clientSignal);

            if (clientSignal.equals(Signal.CHECK_IF_FIRST.getSignal())) {
                System.out.println("Client wants to know if there is a broadcaster already...");
                if (broadcasterConnected) {
                    System.out.println("There is, letting client know...");
                    TCPOutputStream.writeBytes(Signal.BROADCASTER_PRESENT.getSignal() + "\n");
                } else {
                    System.out.println("No, there isn't, setting client as the broadcaster...");
                    broadcasterConnected = true;
                    clientIsBroadcaster = true;
                    TCPOutputStream.writeBytes("You're the broadcaster now, send data over UDP!\n");
                }
            } else {
                TCPOutputStream.writeBytes("Incorrect signal received: " + clientSignal + "\n");
            }

            if (!clientIsBroadcaster) {
                receivers.add(TCPSocketConnection.getInetAddress());
                System.out.println("Client added to receivers.");
            } else {
                System.out.println("Setting client as broadcaster...");
                relayBroadcast();
            }
        } catch (Exception ex) {
            try {
                FileOutputStream fos = new FileOutputStream(new File("ConnectionHandler.log"), true);
                PrintStream logStream = new PrintStream(fos);
                ex.printStackTrace(logStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}