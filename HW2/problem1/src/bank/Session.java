package bank;

public class Session {

    private String sessionKey;
    private Bank bank;
    private boolean valid;
    private int transLimit = 3;
    private int numCall = 0;

    Session(String sessionKey,Bank bank){
        this.sessionKey = sessionKey;
        this.bank = bank;
        valid = true;
    }
    public boolean deposit(int amount) {
        if(valid){
            Bank bank = getBank();
            boolean boolDeposit = bank.deposit(sessionKey, amount);
            numCall += 1;
            if(numCall >= transLimit){
                expireSession();
            }
            return boolDeposit;
        }
        return false;
    }

    public Bank getBank() { return bank; }

    public boolean withdraw(int amount) {
        if(valid){
            Bank bank = getBank();
            boolean boolWithdraw = bank.withdraw(sessionKey, amount);
            numCall += 1;
            if(numCall >= transLimit){
                expireSession();
            }
            return boolWithdraw;
        }
        return false;
    }

    public boolean transfer(String targetId, int targetAccountID, int amount) {
        if(valid){
            Bank bank = getBank();
            boolean boolTransfer = bank.transfer(sessionKey, targetId, targetAccountID, amount);
            numCall += 1;
            if(numCall >= transLimit){
                expireSession();
            }
            return boolTransfer;
        }
        return false;
    }

    public void expireSession(){
        this.valid = false;
    }

}
