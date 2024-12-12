import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.*;

public class ServerA extends Gui implements Runnable{

    private ServerSocket serverSocket;
    private PrintWriter out;
    private int port;

    private String contactId;
    private Contact contact;
    private int contactIndex;

    public ServerA(int port){
    this.port = port;
    }



    @Override
    public String chatOnline(){

        contactId = super.chatOnline();

        contact = findContactById(contactId);


            try {
            serverSocket = new ServerSocket(port);
            new Thread(this).start(); // Start server thread
        } catch (IOException e) {
            e.printStackTrace();
        }


            return null;


    }






    @Override
    public void run() {
        contactIndex = findContactIndex(contactId);

        try {
            getChatArea().append("Waiting for " +contact.getName() +" connection...\n");
            Socket clientSocket = serverSocket.accept();
            getChatArea().append(contact.getName()+" connected.\n");

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                getChatArea().append(contact.getName()+": " + inputLine + "\n");

                getContacts().get(contactIndex).getChatHistory().add(new Sms(inputLine,contact.getName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public int findContactIndex(String contactId){
//        int index;
        for (int i = 0; i < getContacts().size() ; i++){
            if (getContacts().get(i).getId().equals(contactId)){
                return i;
            }
        }

        return -1;
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
        getContacts().add(new Contact("Sami", "03039812367"));
        getContacts().add(new Contact("Ali", "03114554894"));
        getContacts().add(new Contact("Aliyan", "03114554894"));
        getContacts().add(new Contact("Wajahat Nazir Warraich", "03114554894"));


    }

    @Override
    public void login(){
        String correctUsername = "eman";
        String correctPassword = "1234";

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
        ServerA n1 = new ServerA(12345);
    }
}
