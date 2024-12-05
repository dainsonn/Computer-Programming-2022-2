package bank;

import security.key.BankPublicKey;
import security.key.BankSymmetricKey;
import security.*;

public class MobileApp {

    private String randomUniqueStringGen(){
        return Encryptor.randomUniqueStringGen();
    }
    private final String AppId = randomUniqueStringGen();
    public String getAppId() {
        return AppId;
    }

    private BankSymmetricKey bankSymmetricKey;

    String id, password;
    int accountID;
    public MobileApp(String id, String password, int accountID){
        this.id = id;
        this.password = password;
        this.accountID = accountID;
    }

    public Encrypted<BankSymmetricKey> sendSymKey(BankPublicKey publickey){
        bankSymmetricKey = new BankSymmetricKey(randomUniqueStringGen());
        Encrypted<BankSymmetricKey> encryptedBSK;
        encryptedBSK = new Encrypted(bankSymmetricKey, publickey);
        return encryptedBSK;
    }

    public Encrypted<Message> deposit(int amount){
       Message message;
       message = new Message("deposit", id, password, accountID, amount);
       Encrypted<Message> encryptedM;
       encryptedM = new Encrypted(message, bankSymmetricKey);
       return encryptedM;
    }

    public Encrypted<Message> withdraw(int amount){
        Message message;
        message = new Message("withdraw", id, password, accountID, amount);
        Encrypted<Message> encryptedM;
        encryptedM = new Encrypted(message, bankSymmetricKey);
        return encryptedM;
    }

    public boolean processResponse(Encrypted<Boolean> obj){
        if(obj != null){
            if(obj.decrypt(bankSymmetricKey) != null){
                return obj.decrypt(bankSymmetricKey);
            }
        }
        return false;
    }

}

