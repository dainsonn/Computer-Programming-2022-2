package bank;

public class SessionManager {

    public static Session generateSession(String id, String password, int accountID, Bank bank) {
        if(bank == null){
            return null;
        }
        String sessionKey = bank.generateSessionKey(id,password, accountID);
        if(sessionKey == null){
            return null;
        }
        return new Session(sessionKey,bank);
    }

    public static void expireSession(Session session){
        session.expireSession();
    }
}