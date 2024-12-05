package bank;

public class Client {
    private int numAccounts;
    final static int maxAccounts = 10;
    private BankAccount[] accounts = new BankAccount[maxAccounts];

    private String id;
    private String password;
    private String membership = "Normal";
    private boolean authenticated = false;

    Client(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId(){
        return this.id;
    }

    String getMembership(){
        int sum = 0;
        for(int i=0; i<maxAccounts; i++){
            if(accounts[i] != null){
                sum += accounts[i].getBalance();
            }
        }
        if(sum > 10000){
            return  "VIP";
        }
        else return "Normal";
    }

    boolean authenticate(String password){
        if(password.equals(this.password)){
            authenticated = true;
        }
        return authenticated;
    }

    void expireAuthenticatedState(){
        authenticated = false;
    }

    BankAccount findAccount(int accountID){
        BankAccount theAccount = null;
        for(int i=0; i<maxAccounts; i++){
            if(accounts[i] != null && accountID == accounts[i].getAccountId()) {
                theAccount = accounts[i];
                break;
            }
        }
        return theAccount;
    }

    boolean createAccount(int accountID, int initBalance){
        if(findAccount(accountID) != null) {
            return false;
        }
        else{
            for(int i=0; i<maxAccounts; i++){
                if(accounts[i] == null){
                    accounts[i] = new BankAccount(accountID, initBalance);
                    break;
                }
            }
            return true;
        }
    }
    public BankAccount[] getAccounts() { return accounts; }
}
