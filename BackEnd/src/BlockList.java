public class BlockList extends Contact {

    String id;
    String name;
    String phoneNumber;


    public static int blockIdGenerator = 1;
    public BlockList(String id,String name, String phoneNumber) {
        // super(name, phoneNumber);
        this.name= name;
        this.phoneNumber = phoneNumber;
        this.id = String.format("%d",blockIdGenerator++);
    }

    @Override
    public String toString() {
        return "" + id + ".  " + name + "   " + phoneNumber;

    }


    public String getId(){
        return id;
    }
    // public String info(){
    //     return String.format("%d. %s\t%s",id,name,phoneNumber);
    // }
}
