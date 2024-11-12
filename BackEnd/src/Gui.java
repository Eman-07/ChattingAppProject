import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.awt.event.*;
import java.io.PrintWriter;
import java.util.*;


public class Gui extends JFrame implements Runnable{

    private PrintWriter out;

    private String name = "test";

    private List<Contact> contacts = new ArrayList<>();
    private List<BlockList> blockedContacts = new ArrayList<>();

    Scanner sc = new Scanner(System.in);


    //panels
    private JPanel panel;
    private JPanel topPanel;
    private JPanel inputPanel;

    private JButton manageContacts = new JButton("Contacts");
    private JButton startChatButton = new JButton("Start Chat");
    private JButton chatHistoryButton = new JButton("Chat Management");
    private JButton darkModeButton = new JButton("☀️");
    private JButton sendButton = new JButton("Send");
    private JButton creatorsInfoButton = new JButton("Creators Info");


    //Gui Text Stuff
    private JTextArea chatArea;
    private JScrollPane scrollPane;
    private JTextField inputField;

    private Boolean isDark = false;

    private ImageIcon logo = new ImageIcon("logo.png");


    public Gui() {



        //pre additions
        preAddContacts();
        //dummysms();



        setTitle("ChatHub");
        setSize(600, 600);
        setIconImage(logo.getImage());



        setDefaultCloseOperation(EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        inputPanel = new JPanel();
        topPanel = new JPanel();



        //Text Stuff
        chatArea = new JTextArea(20, 30);
        chatArea.setEditable(false);
        scrollPane = new JScrollPane(chatArea);
        inputField = new JTextField(20);

        topPanel.add(manageContacts);
        topPanel.add(startChatButton);
        topPanel.add(chatHistoryButton);
        topPanel.add(creatorsInfoButton);

        inputPanel.add(darkModeButton);
        inputPanel.add(scrollPane);
        inputPanel.add(inputField);
        inputPanel.add(sendButton);


        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(inputPanel, BorderLayout.SOUTH);


        //action listener
        manageContacts.addActionListener(e-> manageContacts());
        startChatButton.addActionListener(e-> startChat());
        chatHistoryButton.addActionListener(e-> chatManagement());
        creatorsInfoButton.addActionListener(e -> creatorsInfo());



        add(panel);
        // login();
        setVisible(true); // Make visible only if login isDark successful








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
        darkModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isDark = !isDark;
                if (isDark) {
                    setDarkMode();
                    darkModeButton.setText("🌙");
                } else {
                    setLightMode();
                    darkModeButton.setText("☀️");
                }
            }
        });
    }

    public void creatorsInfo(){
        StringBuilder creators = new StringBuilder("Creators:\n");
        creators.append("\tSami-ur-Rehman(https://github.com/SamiUrRehman2395)\nMuhammad Eman(https://github.com/Eman-07)\n");
        JOptionPane.showMessageDialog(null, creators.toString());
    }
    public void login() {
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
            bye();
        }
    }
    public void bye() {

        JOptionPane.showMessageDialog(null, "Thanks for using the software.");
        System.exit(0); // Terminate the program
    }
    private void manageContacts() {

        String[] options = {"Add Contact", "Remove Contact",  "View All contacts" , "Block List"};
        int choice = JOptionPane.showOptionDialog(this, "Choose an option:", "Manage Contacts",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0 -> addContact();
            case 1 -> deleteContact();
            case 2 -> viewAllContacts();
            case 3 -> manageBlockedContacts();

        }
    }
    private void addContact() {

        // String id = JOptionPane.showInputDialog(this, "Enter Id:", "AddContact", JOptionPane.PLAIN_MESSAGE);
        String name = JOptionPane.showInputDialog(this, "Enter Name:", "AddContact", JOptionPane.PLAIN_MESSAGE);
        String number = JOptionPane.showInputDialog(this, "Enter PhoneNumber:", "AddContact", JOptionPane.PLAIN_MESSAGE);

        if (name != null && number != null) {
            JOptionPane.showMessageDialog(null,"Contact Added Successfully");
            contacts.add(new Contact(name, number));

        }
    }

    private void deleteContact() {

        StringBuilder data = new StringBuilder("Contacts : \n");
        if (contacts.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Contacts");
        } else {
            for (Contact contact : contacts) {
                data.append(contact.toString()).append("\n");
            }
            data.append("\nEnter Contact Id to delete Contact");




            String id = JOptionPane.showInputDialog(this, data.toString(), "DeleteContact", JOptionPane.PLAIN_MESSAGE);
            Contact contact = findContactById(id);

            if (contact != null) {
                JOptionPane.showMessageDialog(null, "Contact Deleted Successfully");
                contacts.remove(contact);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid");
            }

        }
    }
    public Contact findContactById(String id) {
        for (Contact contact : contacts) {
            if (contact.getId().equals(id)) {
                return contact;
            }
        }
        return null;
    }
    public BlockList findBlockedContactById(String id) {
        for (BlockList blocked : blockedContacts) {
            if (blocked.getId().equals(id)) {
                return blocked;
            }
        }
        return null;
    }

    public void manageBlockedContacts() {

        String[] options = {"Block Contact", "UnBlock Contact", "View Block List", };
        int choice = JOptionPane.showOptionDialog(this, "Choose an option:", "Block Contacts Menu",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);



        switch (choice) {
            case 0 -> blockContact();
            case 1 -> unblockContact();
            case 2 -> viewBlockedContacts();

        }

    }

    public void blockContact() {

        StringBuilder data = new StringBuilder("Blocked Contacts : \n");
        if (contacts.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Contacts");
        } else {
            for (Contact contact : contacts) {
                data.append(contact.toString()).append("\n");
            }
            data.append("\nEnter Contact Id to Block Contact");


            String id = JOptionPane.showInputDialog(this, data.toString(), "Block Contacts Menu", JOptionPane.PLAIN_MESSAGE);
            Contact contact = findContactById(id);

            if (contact != null) {
                blockedContacts.add(new BlockList(contact.getId(),contact.getName(), contact.getPhoneNumber()));
                JOptionPane.showMessageDialog(null, "Contact Added to BlockList");
            }

            else{
                JOptionPane.showMessageDialog(null, "Invalid Id Entered");
            }

        }



    }

    private void unblockContact() {

        StringBuilder data = new StringBuilder("Blocked Contacts : \n");
        if (contacts.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Blocked Contacts");
        } else {
            for (BlockList block : blockedContacts) {
                data.append(block.toString()).append("\n");
            }

            if (!blockedContacts.isEmpty()) {

                data.append("\nEnter Contact Id to Un-Block Contact");
                String id = JOptionPane.showInputDialog(this, data.toString(), "UnBlock Menu", JOptionPane.PLAIN_MESSAGE);



                BlockList blocked =  findBlockedContactById(id);



                if (blocked != null) {
                    JOptionPane.showMessageDialog(null, "Contact Un-Blocked Successfully");
                    blockedContacts.remove(blocked);
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "No Blocked Contacts");
            }


        }
    }

    public void preAddContacts() {
//        contacts.add(new Contact("Eman", "03039812367"));

    }
    public void viewAllContacts() {
        StringBuilder data = new StringBuilder("Contacts : \n");

        if (contacts.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Contacts");
        } else {
            for (Contact contact : contacts) {
                data.append(contact.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(this, data.toString());
        }

    }

    public void viewBlockedContacts() {

        StringBuilder data = new StringBuilder("Blocked Contacts : \n");

        if (blockedContacts.isEmpty()) {

            JOptionPane.showMessageDialog(null,"No Blocked Contacts");
        } else {
            for (BlockList blocked : blockedContacts) {
                data.append(blocked.toString()).append("\n");
            }

            JOptionPane.showMessageDialog(null, data, "BLocked Contatcs",  JOptionPane.PLAIN_MESSAGE);
        }
    }



    public void startChat(){

        String[] options = {"Chat", "Chat Online"};
        int choice = JOptionPane.showOptionDialog(this, "Choose an option:", "Chat",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0 -> chat();
            case 1 -> chatOnline();
        }

    }


    public void chatManagement(){
        String[] options = {"View Chat History", "Delete Message"};
        int choice = JOptionPane.showOptionDialog(this,"Choice an option:","Chat Management",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0 -> viewChatHistory();
            case 1 -> deleteMessage();

        }

    }

    public void deleteMessage() {

        StringBuilder data = new StringBuilder("Contacts : \n");

        if (contacts.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Contacts");
        } else {
            for (Contact contact : contacts) {
                data.append(contact.toString()).append("\n");
            }
            data.append("Select Contact id to Start view Chat With :");

            String id = JOptionPane.showInputDialog(this, data.toString());

            int contactIndex = findContactIndex(id);

            if (contactIndex == -1){
                JOptionPane.showMessageDialog(null, "Invalid Contact Id");
                return;
            }


            StringBuilder history = new StringBuilder("Chat History : \n");


            contacts.get(contactIndex).smsSorter();
            for (Sms smshistory : contacts.get(contactIndex).getChatHistory()){
                history.append(smshistory.detail()).append("\n");
            }

            history.append("Select Sms Id to delete Sms");
            String smsID = JOptionPane.showInputDialog(this, history.toString());


            int smsIndex = getSmsIndexById(smsID, contactIndex);

            if (smsIndex == -1 ){
                JOptionPane.showMessageDialog(null, "Invalid Sms Id");
                return;
            }
            else{
                contacts.get(contactIndex).getChatHistory().remove(smsIndex);
                JOptionPane.showMessageDialog(null, "Message Deleted Successfully");
            }
        }
    }

    public int getSmsIndexById(String id, int contactIndex){

        for (int i = 0; i < contacts.get(contactIndex).getChatHistory().size(); i++) {
            if (contacts.get(contactIndex).getChatHistory().get(i).getsmsId().equals(id)){
                return i;
            }
        }
        
        return -1;
    }

    public void dummysms(){
        contacts.get(1).getChatHistory().add(new Sms("HI","Eman"));
        contacts.get(1).getChatHistory().add(new Sms("kya hal hai","Eman"));
    }
    public void chat(){
        JOptionPane.showMessageDialog(this,"Coming Soon");
    }

    public String chatOnline(){
        StringBuilder data = new StringBuilder("Contacts : \n");

        if (contacts.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Contacts");
        } else {
            for (Contact contact : contacts) {
                data.append(contact.toString()).append("\n");
            }
            data.append("Select Contact id to Start Chat With :");


            String id = JOptionPane.showInputDialog(this, data.toString());

            Contact contact = findContactById(id);
            return id;
        }


        return null;

    }


    public void viewChatHistory() {
        StringBuilder data = new StringBuilder("Contacts : \n");

        if (contacts.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Contacts");
        } else {
            for (Contact contact : contacts) {
                data.append(contact.toString()).append("\n");
            }
            data.append("Select Contact id to Start view Chat With :");

            String id = JOptionPane.showInputDialog(this, data.toString());

            int index = findContactIndex(id);

            if (index == -1){
                JOptionPane.showMessageDialog(null, "Invalid Contact Id");
                return;
            }


            StringBuilder history = new StringBuilder("Chat History : \n");


            contacts.get(index).smsSorter();
            for (Sms smshistory : contacts.get(index).getChatHistory()){
                history.append(smshistory.toString()).append("\n");
            }


            JOptionPane.showMessageDialog(null, history.toString());
        }
    }


    public int findContactIndex(String contactId){

        for (int i = 0; i < getContacts().size() ; i++){
            if (getContacts().get(i).getId().equals(contactId)){
                return i;
            }
        }

        return -1;
    }


    //Chatting System
    // public void startChat(){

    //         StringBuilder data = new StringBuilder("Contacts : \n");

    //         if (contacts.isEmpty()) {
    //             JOptionPane.showMessageDialog(null, "No Contacts");
    //         } else {
    //             for (Contact contact : contacts) {
    //                 data.append(contact.toString()).append("\n");
    //             }
    //                 data.append("Enter Contact Id to Start Chat");

    //             String id = JOptionPane.showInputDialog(this, data.toString(), "StartChat", JOptionPane.PLAIN_MESSAGE);
    //             Contact contact = findContactById(id);




    //         }

    //     }




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

        manageContacts.setBackground(Color.BLACK);
        manageContacts.setForeground(Color.WHITE);

         creatorsInfoButton.setBackground(Color.BLACK);
         creatorsInfoButton.setForeground(Color.WHITE);

        darkModeButton.setBackground(Color.BLACK);
        darkModeButton.setForeground(Color.WHITE);

        startChatButton.setBackground(Color.BLACK);
        startChatButton.setForeground(Color.WHITE);
        chatHistoryButton.setBackground(Color.BLACK);
        chatHistoryButton.setForeground(Color.WHITE);


//        optionPane.setBackground(Color.DARK_GRAY);
//        optionPane.setForeground(Color.white);
        UIManager.put("OptionPane.background", new Color(45, 45, 45));  // Background color
        UIManager.put("Panel.background", new Color(45, 45, 45));       // Background color of the panel inside JOptionPane
        UIManager.put("OptionPane.messageForeground", Color.WHITE);     // Foreground color (text color)



    }


    private void setLightMode() {
        topPanel.setBackground(null);
        panel.setBackground(null);
        chatArea.setBackground(null);
        chatArea.setForeground(null);

        inputField.setBackground(null);
        inputField.setForeground(null);

        inputPanel.setBackground(null);

        sendButton.setBackground(null);
        sendButton.setForeground(null);

        manageContacts.setBackground(null);
        manageContacts.setForeground(null);

         creatorsInfoButton.setBackground(Color.WHITE);
         creatorsInfoButton.setForeground(Color.BLACK);
        darkModeButton.setBackground(null);
        darkModeButton.setForeground(Color.ORANGE);

        chatHistoryButton.setBackground(null);
        chatHistoryButton.setForeground(null);
        startChatButton.setBackground(null);
        startChatButton.setForeground(null);

        // Reset the UIManager properties if needed
        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
        UIManager.put("OptionPane.messageForeground", null);
    }



    //setter and getters
    public JTextArea getChatArea(){
        return chatArea;
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
            chatArea.append("Server: " + message + "\n");
        }
    }


    public static void main(String[] args) {
        new Gui();
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<BlockList> getBlockedContacts() {
        return blockedContacts;
    }

    public void setBlockedContacts(List<BlockList> blockedContacts) {
        this.blockedContacts = blockedContacts;
    }

    public Scanner getSc() {
        return sc;
    }

    public void setSc(Scanner sc) {
        this.sc = sc;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public JPanel getTopPanel() {
        return topPanel;
    }

    public void setTopPanel(JPanel topPanel) {
        this.topPanel = topPanel;
    }

    public JPanel getInputPanel() {
        return inputPanel;
    }

    public void setInputPanel(JPanel inputPanel) {
        this.inputPanel = inputPanel;
    }

    public JButton getManageContacts() {
        return manageContacts;
    }

    public void setManageContacts(JButton manageContacts) {
        this.manageContacts = manageContacts;
    }

    public JButton getStartChatButton() {
        return startChatButton;
    }

    public void setStartChatButton(JButton startChatButton) {
        this.startChatButton = startChatButton;
    }

    public JButton getChatHistoryButton() {
        return chatHistoryButton;
    }

    public void setChatHistoryButton(JButton chatHistoryButton) {
        this.chatHistoryButton = chatHistoryButton;
    }

    public JButton getDarkModeButton() {
        return darkModeButton;
    }

    public void setDarkModeButton(JButton darkModeButton) {
        this.darkModeButton = darkModeButton;
    }

    public JButton getSendButton() {
        return sendButton;
    }

    public void setSendButton(JButton sendButton) {
        this.sendButton = sendButton;
    }

    public void setChatArea(JTextArea chatArea) {
        this.chatArea = chatArea;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void setScrollPane(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public JTextField getInputField() {
        return inputField;
    }

    public void setInputField(JTextField inputField) {
        this.inputField = inputField;
    }

    public Boolean getIsDark() {
        return isDark;
    }

    public void setIsDark(Boolean isDark) {
        this.isDark = isDark;
    }

    @Override
    public void run() {

    }


}
