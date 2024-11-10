import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Sms {
    private String content;
    private LocalDateTime timestamp;
    private boolean isRead;
    private String sender; // Added sender field to track message origin
    private static int idCounter = 1;
    private String smsId;





    public Sms(String content, String sender) {
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.isRead = false;
        this.sender = sender;
        this.smsId = String.format("%d", idCounter++);
    }

    public void markAsRead() {
        this.isRead = true;
    }

    public String getsmsId() {
        return smsId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    public boolean isRead() {
        return isRead;
    }

    public String detailedMsg(){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        return String.format("\t%-15s | sent time : %s" ,getContent(),timestamp.format(dateFormatter));
    }


    @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return "[" + timestamp.format(dateFormatter) + "] " + sender + ": " + content;
    }
}
