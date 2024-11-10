import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Client extends Gui implements Runnable {

    private Socket socket; // Changed to Socket for client-side connection
    private PrintWriter out;

    public Client(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port); // Client connects to the server

            new Thread(this).start(); // Start client thread
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not connect to server at " + serverAddress + ":" + port,
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @Override
    public void chatOnline() {
        JOptionPane.showMessageDialog(null, "I am in server");
    }


    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                getChatArea().append("Server: " + inputLine + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
            getChatArea().append("Client: " + message + "\n");
        }
    }

    public static void main(String[] args) {
        Client n1 = new Client("192.168.100.41", 12345);
    }
}
