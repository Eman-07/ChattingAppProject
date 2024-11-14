import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;

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
            getChatArea().append("Successfully connected to " + contact.getName()+"\n");
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
        getContacts().add(new Contact("Haider", "03441919449"));
        getContacts().add(new Contact("Haroon", "03004534138"));
        getContacts().add(new Contact("Ahtisham", "03217356066"));




    }

    @Override
    public void login(){
        String correctUsername = "sami";
        String correctPassword = "2395";

        // Prompt for username
        String username = JOptionPane.showInputDialog(this, "Enter Username:", "Login", JOptionPane.PLAIN_MESSAGE);
        if (username == null) {
            // User canceled, exit the program
            JOptionPane.showMessageDialog(this, "Login canceled.");
            bye();
        }

        // Prompt for password
        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(this, passwordField, "Enter Password:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String password = new String(passwordField.getPassword());

            // Check if entered credentials match the correct ones
            if (username.equals(correctUsername) && password.equals(correctPassword)) {
                JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + username + "!");
            } else {
                JOptionPane.showMessageDialog(this, "Login failed! Incorrect username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                login(); // Retry login if the credentials are incorrect
            }
        } else {
            // User canceled at the password dialog, exit the program
            JOptionPane.showMessageDialog(this, "Login canceled.");
            super.bye();
        }
    }

    public static void main(String[] args) {
        Client n1 = new Client( 12345);
    }
}
