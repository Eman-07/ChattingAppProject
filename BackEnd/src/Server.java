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
    private int port;

    private String contactId;
    private Contact contact;
    private int contactIndex;

    public Server(int port){
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


    public static void main(String[] args) {
        Server n1 = new Server(12345);
    }
}
