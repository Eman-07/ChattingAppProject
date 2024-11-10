import java.util.ArrayList;
import java.util.List;

public class Contact {
    private String id;
    private String name;
    private String phoneNumber;
    private List<Sms> chatHistory; // List to store message history

    public static int contactIdGenerator = 1;

    public Contact(){}

    public Contact(String name, String phoneNumber) {

        this.id = String.format("%d",contactIdGenerator++);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.chatHistory = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<Sms> getChatHistory() {
        return chatHistory;
    }

    // Add a message to chat history
    public void addMessage(Sms message) {
        chatHistory.add(message);
    }

    @Override
    public String toString() {
        return "" + id + ".  " + name + "   " + phoneNumber;
    }


    public String getContactsInfo(){
        return String.format("\tID: %-03d | %-12s | %-12s",getId(),getName(),getPhoneNumber() );
    }

}
