package Problem_01;

import java.io.IOException;
import java.net.*;

public class Messenger {
    private final Runnable runnable;
    private static String address;
    private final static int SERVER_PORT = 1901;
    private final static int CLIENT_PORT = 1902;
    private static DatagramSocket socket;
    private static DatagramPacket packet;
    private static final int BUFFER_SIZE = 1024;
    private static byte[] buffer = new byte[BUFFER_SIZE];

    Messenger(Runnable runnable) {
        this.runnable = runnable;
    }

    static void setIP(String IP){
        address = IP;
    }

    static void server() {
        try {
            socket = new DatagramSocket(SERVER_PORT);
            int pos = 0;
            while (true) {
                int c = System.in.read();
                switch (c) {
                    case -1:
                        System.out.println("Now the server will be shut down");
                        break;
                    case '\r':
                        break;
                    case '\n':
                        socket.send(new DatagramPacket(buffer, pos, InetAddress.getByName(address),CLIENT_PORT));
                        pos = 0;
                        break;
                    default:
                        buffer[pos++] = (byte)c;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void client() {
        try {
            socket = new DatagramSocket(CLIENT_PORT);
            while (true) {
                packet = new DatagramPacket(buffer, BUFFER_SIZE);
                socket.receive(packet);
                System.out.println(new String(packet.getData(), 0, packet.getLength()));
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void run() {
        this.runnable.run();
    }
}
