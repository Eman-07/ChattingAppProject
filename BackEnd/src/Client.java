import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Client extends Gui implements Runnable {

    private Socket socket; // Changed to Socket for client-side connection
    private PrintWriter out;

    private String serverAddress;
    private int port;

    private String contactId;
    private Contact contact;
    private int contactIndex;



    public Client( int port) {
//        this.serverAddress = serverAddress;
        this.port = port;
    }

    @Override
    public String chatOnline() {

        contactId = super.chatOnline();
        contact = findContactById(contactId);

        serverAddress = JOptionPane.showInputDialog(this,"Enter IP Address of Server : ");

        try {
            socket = new Socket(serverAddress, port); // Client connects to the server

            new Thread(this).start(); // Start client thread
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not connect to " + contact.getName() + " at " + serverAddress + ":" + port,
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                getChatArea().append(contact.getName()+" : " + inputLine + "\n");
                getContacts().get(contactIndex).getChatHistory().add(new Sms(inputLine,contact.getName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
            getChatArea().append("Me: " + message + "\n");
        }
        getContacts().get(contactIndex).getChatHistory().add(new Sms(message,"You"));

    }

    @Override
    public void preAddContacts(){
        getContacts().add(new Contact("Eman", "03039812367"));
        getContacts().add(new Contact("Gen.Asim Munir", "+45941919449"));


    }

    public static void main(String[] args) {
        Client n1 = new Client( 12345);
    }
}
