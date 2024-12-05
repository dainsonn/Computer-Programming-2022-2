package security;

public class Message {
    private String id;
    private String password;
    private String requestType;
    private int accountID;
    private int amount;

    public Message(String requestType, String id, String password, int accountID, int amount){
        this.requestType = requestType;
        this.id = id;
        this.password = password;
        this.accountID = accountID;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
    public int getAccountID() {return accountID; }

    public String getRequestType() {
        return requestType;
    }
}
