import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.List;

public class Server extends JFrame implements Runnable {
    private ServerSocket serverSocket;
    private PrintWriter out;
    private Map<String, Contact> contacts = new HashMap<>();
    private List<BlockList> blockedContacts = new ArrayList<>();


    //Gui's Stuff

        //Panels
    private JPanel panel;
    private JPanel topPanel;
    private JPanel inputPanel;


        //Gui Text Stuff
    private JTextArea chatArea;
    private JScrollPane scrollPane;
    private JTextField inputField;

        //Buttons
    private JButton sendButton;
    JButton manageContactsButton;
    JButton creatorsInfoButton;
    JButton toggleDarkModeButton;


    private JOptionPane optionPane;

    private boolean isDarkMode = false;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            setupGUI();
            preAddContacts();
            new Thread(this).start(); // Start server thread
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupGUI() {


        //Application name and resolution
        setTitle("ChatHub");
        setSize(500, 500);


        //Panels
        panel = new JPanel();  //Like a main div
        panel.setLayout(new BorderLayout());

        topPanel = new JPanel();
        inputPanel = new JPanel();





        //Text Stuff
        chatArea = new JTextArea(20, 30);
        chatArea.setEditable(false);
        scrollPane = new JScrollPane(chatArea);
        inputField = new JTextField(20);



        //Buttons & Action Listeners
        sendButton = new JButton("Send");

        manageContactsButton = new JButton("Manage Contacts");
        manageContactsButton.addActionListener(e -> openContactManagement());

        creatorsInfoButton = new JButton("Creators Info");
        creatorsInfoButton.addActionListener(e -> viewCreators() );

        toggleDarkModeButton = new JButton("☀️");


//  Adding Components to Panels

        topPanel.add(manageContactsButton);
        topPanel.add(creatorsInfoButton);
        topPanel.add(toggleDarkModeButton);

        inputPanel.add(inputField);
        inputPanel.add(scrollPane);
        inputPanel.add(sendButton);



        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(inputPanel, BorderLayout.SOUTH);
        panel.add(scrollPane, BorderLayout.CENTER);






        //Setting app logo
        ImageIcon logo = new ImageIcon("logo.png");
        setIconImage(logo.getImage());



        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);







        //Adding Function that if users press ENTER key then message will be sent
        inputField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendButton.doClick();
                }
            }
        });

        sendButton.addActionListener(e -> {
            String message = inputField.getText();
            if (!message.isEmpty()) {
                sendMessage(message);
                inputField.setText("");
            }
        });







        //Adding Darkmode Function
        toggleDarkModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isDarkMode = !isDarkMode;
                if (isDarkMode) {
                    setDarkMode();
                    toggleDarkModeButton.setText("🌙");
                } else {
                    setLightMode();
                    toggleDarkModeButton.setText("☀️");
                }
            }
        });



    }


    private void preAddContacts() {
        contacts.put("1", new Contact("1", "Imad Wasim", "03039812367"));
        contacts.put("2", new Contact("2", "Mohammad Amir", "0317080150"));
    }

    private void openContactManagement() {
        String[] options = {"Add Contact", "Remove Contact", "Block Contact", "View All contacts" , "View Blocked contacts"};
        int choice = JOptionPane.showOptionDialog(this, "Choose an option:", "Manage Contacts",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0 -> addContact();
            case 1 -> removeContact();
            case 2 -> blockContact();
            case 3 -> viewAllContacts();
            case 4 -> viewBlockedContacts();
        }
    }

    private void addContact() {
        String name = JOptionPane.showInputDialog(this, "Enter Contact Name:");
        String number = JOptionPane.showInputDialog(this, "Enter Contact Number:");
        String id = String.valueOf(contacts.size() + 1);
        Contact contact = new Contact(id, name, number);
        contacts.put(id, contact);
        JOptionPane.showMessageDialog(this, "Contact added successfully!");
    }


    private void removeContact() {
        String id = JOptionPane.showInputDialog(this, "Enter Contact ID to remove:");
        if (contacts.remove(id) != null) {
            JOptionPane.showMessageDialog(this, "Contact removed successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Contact not found.");
        }
    }

    private void blockContact() {
        String id = JOptionPane.showInputDialog(this, "Enter Contact ID to block:");
        Contact contact = contacts.get(id);
        if (contact != null) {
            blockedContacts.add(new BlockList(contact.getId(), contact.getName(), contact.getPhoneNumber()));
            JOptionPane.showMessageDialog(this, "Contact blocked successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Contact not found.");
        }
    }


    //This is will execute when user hits viewBlock contacts
    private void viewBlockedContacts() {
        if (blockedContacts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No contacts are currently blocked.");
            return;
        }
        StringBuilder blockedList = new StringBuilder("Blocked Contacts:\n");
        for (BlockList blocked : blockedContacts) {
            blockedList.append(blocked).append("\n");
        }
        JOptionPane.showMessageDialog(this, blockedList.toString());
    }

    public void viewCreators(){
        StringBuilder creators = new StringBuilder("Creators:\n");
        creators.append("\tSami-ur-Rehman(https://github.com/SamiUrRehman2395)\nMuhammad Eman(https://github.com/Eman-07)\n");

        JOptionPane.showMessageDialog(null, creators.toString());
    }

    //creating method to view all contacts
    public void viewAllContacts() {
        StringBuilder contactList = new StringBuilder("Contacts:\n");
            for (Contact contact : contacts.values()) {
                contactList.append(contact).append("\n");
            }
            JOptionPane.showMessageDialog(this, contactList.toString());


    }

    public void run() {
        try {
            chatArea.append("Waiting for client connection...\n");
            Socket clientSocket = serverSocket.accept();
            chatArea.append("Client connected.\n");

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                chatArea.append("Client: " + inputLine + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void sendMessage(String message) {
        if (out != null) {
            out.println(message);
            chatArea.append("Server: " + message + "\n");
        }

    }




    public void setDarkMode(){



        topPanel.setBackground(Color.DARK_GRAY);
        scrollPane.setBackground(Color.DARK_GRAY);

        inputPanel.setBackground(Color.DARK_GRAY);

        chatArea.setBackground(Color.GRAY);
        chatArea.setForeground(Color.WHITE);

        inputField.setBackground(Color.LIGHT_GRAY);
        inputField.setForeground(Color.BLACK);

        sendButton.setBackground(Color.BLACK);
        sendButton.setForeground(Color.WHITE);

        manageContactsButton.setBackground(Color.BLACK);
        manageContactsButton.setForeground(Color.WHITE);

        creatorsInfoButton.setBackground(Color.BLACK);
        creatorsInfoButton.setForeground(Color.WHITE);

        toggleDarkModeButton.setBackground(Color.BLACK);
        toggleDarkModeButton.setForeground(Color.GRAY);


//        optionPane.setBackground(Color.DARK_GRAY);
//        optionPane.setForeground(Color.white);
        UIManager.put("OptionPane.background", new Color(45, 45, 45));  // Background color
        UIManager.put("Panel.background", new Color(45, 45, 45));       // Background color of the panel inside JOptionPane
        UIManager.put("OptionPane.messageForeground", Color.WHITE);     // Foreground color (text color)



    }


    private void setLightMode() {
        topPanel.setBackground(Color.LIGHT_GRAY);
        panel.setBackground(Color.LIGHT_GRAY);
        chatArea.setBackground(Color.WHITE);
        chatArea.setForeground(Color.BLACK);

        inputField.setBackground(Color.WHITE);
        inputField.setForeground(Color.BLACK);

        inputPanel.setBackground(Color.WHITE);

        sendButton.setBackground(Color.WHITE);
        sendButton.setForeground(Color.BLACK);

        manageContactsButton.setBackground(Color.WHITE);
        manageContactsButton.setForeground(Color.BLACK);

        creatorsInfoButton.setBackground(Color.WHITE);
        creatorsInfoButton.setForeground(Color.BLACK);
        toggleDarkModeButton.setBackground(Color.WHITE);
        toggleDarkModeButton.setForeground(Color.BLACK);



        // Reset the UIManager properties if needed
        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
        UIManager.put("OptionPane.messageForeground", null);
    }


    public static void main(String[] args) {
        new Server(12345);
    }
}
