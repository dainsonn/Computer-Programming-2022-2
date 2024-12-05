package bank;

import bank.event.*;
import security.*;
import security.key.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Bank {
    private int numClients=0;
    final static int maxClients = 100;
    private Client[] clients = new Client[maxClients];
    public void createClient(String id, String password){
        for(int i=0; i<maxClients; i++){
            if(clients[i] == null){
                clients[i] = new Client(id, password);
                break;
            }
            else if(clients[i].getId().equals(id)){
                break;
            }
        }
    }
    public void createAccount(String id, String password, int accountID) {
        createAccount(id, password, accountID, 0);
    }

    public void createAccount(String id, String password, int accountID, int initBalance) {
        for(int i=0; i<maxClients; i++){
            if(clients[i] != null && clients[i].getId().equals(id)){
                if(clients[i].authenticate(password) && clients[i].findAccount(accountID) == null){
                    clients[i].createAccount(accountID, initBalance);
                    clients[i].expireAuthenticatedState();
                    break;
                }
            }
        }
    }

    public boolean deposit(String id, String password, int accountID, int amount) {
        for(int i=0; i<maxClients; i++){
            if(clients[i] != null && clients[i].getId().equals(id)){
                if(clients[i].authenticate(password) && clients[i].findAccount(accountID) != null){
                    clients[i].findAccount(accountID).deposit(amount);
                    clients[i].expireAuthenticatedState();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean withdraw(String id, String password, int accountID, int amount) {
        for(int i=0; i<maxClients; i++){
            if(clients[i] != null && clients[i].getId().equals(id)){
                if(clients[i].authenticate(password) && clients[i].findAccount(accountID) != null){
                    boolean boolWithDraw = clients[i].findAccount(accountID).withdraw(amount, clients[i].getMembership());
                    clients[i].expireAuthenticatedState();
                    if(!boolWithDraw) return false;
                    else return true;
                }
            }
        }
        return false;
    }

    public boolean transfer(String sourceId, String password, int sourceAccountID, String targetId, int targetAccountID, int amount) {
        Client source = null;
        for(int i=0; i<maxClients; i++){
            if(clients[i] != null && clients[i].getId().equals(sourceId)){
                source = clients[i];
                break;
            }
        }
        if(source == null) return false;
        else source.expireAuthenticatedState();

        if(source.authenticate(password) && source.findAccount(sourceAccountID) != null) {
            for (int i = 0; i < maxClients; i++) {
                if (clients[i] != null && clients[i].getId().equals(targetId) && clients[i].findAccount(targetAccountID) != null) {
                    boolean boolSend = source.findAccount(sourceAccountID).send(amount, source.getMembership());
                    if (boolSend) {
                        clients[i].findAccount(targetAccountID).receive(amount);
                        return true;
                    } else return false;
                }
            }
        }
        return false;
    }

    public Event[] getEvents(String id, String password, int accountID) {
        for(int i=0; i<maxClients; i++){
            if(clients[i] != null && clients[i].getId().equals(id)){
                if(clients[i].authenticate(password) && clients[i].findAccount(accountID) != null){
                    if(clients[i].findAccount(accountID).getEvents() != null){
                        clients[i].expireAuthenticatedState();
                        List<Event> list = new ArrayList<>(Arrays.asList(clients[i].findAccount(accountID).getEvents()));
                        list.removeAll(Collections.singletonList(null));
                        return list.toArray(new Event[list.size()]);
                    }
                }
            }
        }
        return null;
    }

    public int getBalance(String id, String password, int accountID) {
        for(int i=0; i<maxClients; i++){
            if (clients[i] != null && clients[i].getId().equals(id)) {
                if(clients[i].authenticate(password) && clients[i].findAccount(accountID) != null){
                    clients[i].expireAuthenticatedState();
                    return clients[i].findAccount(accountID).getBalance();
                }
            }
        }
        return -1;
    }

    public Client[] getClients() { return clients; }

    public Client getClientByID(String id){
        for(int i=0; i<maxClients; i++){
            if(clients[i].getId().equals(id)){
                return clients[i];
            }
        }
        return null;
    }

    private static String randomUniqueStringGen(){
        return Encryptor.randomUniqueStringGen();
    }
    private BankAccount find(String id, int accountID) {

        Client client = getClientByID(id);
        BankAccount clientAccount = client.findAccount(accountID);

        return clientAccount;
    }

    final static int maxSessionKey = 100;
    int numSessionKey = 0;
    String[] sessionKeyArr = new String[maxSessionKey];
    Client[] bankClientmap = new Client[maxSessionKey];
    BankAccount[] bankAccountmap = new BankAccount[maxSessionKey];
    String generateSessionKey(String id, String password, int accountID){
        Client client = getClientByID(id);

        if(client == null || !client.authenticate(password) || client.findAccount(accountID)==null){
            return null;
        }

        String sessionkey = randomUniqueStringGen();
        sessionKeyArr[numSessionKey] = sessionkey;
        bankClientmap[numSessionKey] = client;
        bankAccountmap[numSessionKey] = client.findAccount(accountID);
        numSessionKey += 1;
        return sessionkey;
    }

    Client getClient(String sessionkey){
        for(int i=0; i < numSessionKey; i++){
            if(sessionKeyArr[i] != null && sessionKeyArr[i].equals(sessionkey)) {
                return bankClientmap[i];
            }
        }
        return null;
    }

    BankAccount getAccount(String sessionkey){
        for(int i = 0 ;i < numSessionKey; i++){
            if(sessionKeyArr[i] != null && sessionKeyArr[i].equals(sessionkey)){
                return bankAccountmap[i];
            }
        }
        return null;
    }

    boolean deposit(String sessionkey, int amount) {
        BankAccount dAccount = getAccount(sessionkey);
        if(dAccount != null){
            dAccount.deposit(amount);
            return true;
        }
        else return false;
    }

    boolean withdraw(String sessionkey, int amount) {
        BankAccount wAccount = getAccount(sessionkey);
        if(wAccount != null){
            boolean boolWithdraw = wAccount.withdraw(amount, getClient(sessionkey).getMembership());
            return boolWithdraw;
        }
        else return false;
    }

    boolean transfer(String sessionkey, String targetId, int targetAccountID, int amount) {
        BankAccount tAccount = getAccount(sessionkey);
        if(tAccount != null){
            if(find(targetId, targetAccountID) != null){
                boolean boolTransfer = tAccount.send(amount, getClient(sessionkey).getMembership());
                if(boolTransfer){
                    find(targetId, targetAccountID).receive(amount);
                }
                return boolTransfer;
            }
            else return false;
        }
        else return false;
    }

    private BankSecretKey secretKey;
    public BankPublicKey getPublicKey(){
        BankKeyPair keypair = Encryptor.publicKeyGen(); // generates two keys : BankPublicKey, BankSecretKey
        secretKey = keypair.deckey; // stores BankSecretKey internally
        return keypair.enckey; //return BankPublicKey
    }

    int maxHandshakes = 10000;
    int numSymmetrickeys = 0;
    BankSymmetricKey[] bankSymmetricKeys = new BankSymmetricKey[maxHandshakes];
    String[] AppIds = new String[maxHandshakes];

    public String[] getAppIds() { return AppIds; }

    public int getAppIdIndex(String AppId){
        for(int i=0; i<numSymmetrickeys; i++){
            if(AppIds[i].equals(AppId)){
                return i;
            }
        }
        return -1;
    }

    public void fetchSymKey(Encrypted<BankSymmetricKey> encryptedKey, String AppId){
        BankSymmetricKey decryptedBSK;
        decryptedBSK = encryptedKey.decrypt(secretKey);
        numSymmetrickeys += 1;
        for(int i=0; i<maxHandshakes; i++){
            if(AppIds[i] != null && AppIds[i].equals(AppId)){
                break;
            }
            else if(AppIds[i] == null){
                AppIds[i] = AppId;
                break;
            }
        }
        int num = getAppIdIndex(AppId);
        if(num >= 0){
            bankSymmetricKeys[getAppIdIndex(AppId)] = decryptedBSK;
        }
    }

    public Encrypted<Boolean> processRequest(Encrypted<Message> messageEnc, String AppId) {
        Message message = null;
        if (getAppIdIndex(AppId) >= 0) {
            if(bankSymmetricKeys[getAppIdIndex(AppId)] != null && messageEnc != null){
                message = messageEnc.decrypt(bankSymmetricKeys[getAppIdIndex(AppId)]);
            }
            if(message.getRequestType().equals("deposit")){
                boolean boolDeposit;
                boolDeposit = deposit(message.getId(), message.getPassword(), message.getAccountID(), message.getAmount());
                return new Encrypted(boolDeposit, bankSymmetricKeys[getAppIdIndex(AppId)]);
            }
            else if(message.getRequestType().equals("withdraw")){
                boolean boolWithdraw;
                boolWithdraw = withdraw(message.getId(), message.getPassword(), message.getAccountID(), message.getAmount());
                return new Encrypted(boolWithdraw, bankSymmetricKeys[getAppIdIndex(AppId)]);
            }
        }
        return null;
    }
}