import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Collection;

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

   // @Override
//    public String toString() {
//        return "" + id + ".  " + name + "   " + phoneNumber;
//    }
   @Override
   public String toString() {
       return String.format("%s.   %-25s\n      %-25s",id,name,phoneNumber);
   }



    public void smsSorter() {
        chatHistory.sort(Comparator.comparing(Sms::getTimestamp).reversed());
    }

    public String getContactsInfo(){
        return String.format("\tID: %-03d | %-12s | %-22s",getId(),getName(),getPhoneNumber() );
    }

}
