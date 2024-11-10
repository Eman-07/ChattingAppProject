import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Server extends Gui implements Runnable{

    private ServerSocket serverSocket;
    private PrintWriter out;

    public Server(int port){
        try {
            serverSocket = new ServerSocket(port);
            new Thread(this).start(); // Start server thread
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void chatOnline(){

        JOptionPane.showMessageDialog(null, "I am in server");
    }






    @Override
    public void run() {
        try {
            getChatArea().append("Waiting for client connection...\n");
            Socket clientSocket = serverSocket.accept();
            getChatArea().append("Client connected.\n");

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                getChatArea().append("Client: " + inputLine + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
            getChatArea().append("Server: " + message + "\n");
        }

    }




    public static void main(String[] args) {
        Server n1 = new Server(12345);
    }
}
