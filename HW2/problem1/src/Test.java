import bank.*;
import bank.event.*;
import security.method.*;
import security.*;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        System.out.println("1.1 Test Cases -----------------------------");
        subproblem1();
        System.out.println("1.2 Test Cases -----------------------------");
        subproblem2();
        System.out.println("1.3 Test Cases -----------------------------");
        subproblem3();
    }
    static void subproblem1() {
        Bank bank = new Bank();
        int b1,b2,b3;
        String janePW = "1234asdf";
        String evaPW = "5678ghkj";
        String janeid = "Jane";
        String evaid = "Eva";
        int janeAccountId = 1;
        int evaAccountID = 10;

        bank.createClient(janeid, janePW);
        bank.createClient(evaid, evaPW);

        bank.createAccount(janeid, janePW, janeAccountId);
        bank.createAccount(evaid, evaPW, evaAccountID, 1000);

        String wrongID = "MelloMello";
        String wrongPW = "abcdefg";
        bank.deposit(janeid, janePW, janeAccountId, 500);
        bank.deposit(janeid, wrongPW, janeAccountId,3900);
        bank.deposit(wrongID, wrongPW, janeAccountId,2800);
        bank.deposit(evaid, evaPW, evaAccountID,6200);
        bank.deposit(evaid, wrongPW, evaAccountID,3200);
        b1 = bank.getBalance(janeid,janePW, janeAccountId);
        b2 = bank.getBalance(evaid,evaPW, evaAccountID);
        b3 = bank.getBalance(wrongID,evaPW, 3);
        printOX("1.1.1. deposit, getBalance & their robustness to wrong id and passwd",b1 == 500 && b2 == 7200 && b3 < 0);

        bank.withdraw(janeid, janePW, janeAccountId,450);
        bank.withdraw(janeid, janePW, janeAccountId,600);
        bank.withdraw(janeid, wrongPW, janeAccountId, 600);
        bank.withdraw(wrongID, janePW, evaAccountID, 300);
        bank.withdraw(evaid, evaPW, evaAccountID, 2400);
        bank.withdraw(evaid, wrongPW, evaAccountID, 2200);
        bank.withdraw(evaid, evaPW, evaAccountID, 78200);
        b1 = bank.getBalance(janeid,janePW, janeAccountId);
        b2 = bank.getBalance(evaid,evaPW, evaAccountID);
        printOX("1.1.2. withdraw & their robustness to wrong id and passwd",b1 == 45 && b2 == 4795 );

        bank.deposit(evaid, evaPW, evaAccountID, 2341);
        bank.deposit(janeid, janePW, janeAccountId, 532);
        bank.withdraw(janeid, janePW, janeAccountId, 6623);
        bank.deposit(janeid, janePW, janeAccountId, 2220);
        bank.deposit(evaid, evaPW, evaAccountID, 6200);
        bank.withdraw(evaid, evaPW, evaAccountID, 2400);
        bank.deposit(janeid, janePW, janeAccountId,5600);
        bank.withdraw(janeid, janePW, janeAccountId, 4150);
        bank.withdraw(evaid, evaPW, evaAccountID, 936);
        bank.withdraw(janeid, janePW, janeAccountId, 452);;
        bank.withdraw(evaid, evaPW, evaAccountID,6120);
        bank.withdraw(janeid, janePW, janeAccountId, 40);
        bank.withdraw(evaid, evaPW, evaAccountID, 10000);
        b1 = bank.getBalance(janeid,janePW, janeAccountId);
        b2 = bank.getBalance(evaid,evaPW, evaAccountID);
        printOX("1.1.3. deposit + withdraw ",b1 == 3740 && b2 == 3875 );

        bank.transfer(janeid, evaPW, janeAccountId, evaid, evaAccountID, 300);
        bank.transfer(janeid, janePW, janeAccountId, evaid, evaAccountID, 652);
        bank.transfer(evaid, evaPW, evaAccountID, janeid, janeAccountId,310);
        bank.transfer(janeid, janePW, janeAccountId, evaid, evaAccountID, 310);
        bank.transfer(evaid, wrongPW, evaAccountID, janeid, janeAccountId, 200);
        bank.transfer(evaid, evaPW, evaAccountID, wrongID, janeAccountId, 120);
        bank.transfer(janeid, janePW, janeAccountId, evaid, evaAccountID, 8210);
        bank.transfer(evaid, wrongPW, janeAccountId, wrongID, janeAccountId, 512);
        bank.transfer(wrongID, wrongPW, janeAccountId, janeid, janeAccountId, 512);
        b1 = bank.getBalance(janeid,janePW, janeAccountId);
        b2 = bank.getBalance(evaid,evaPW, evaAccountID);
        printOX("1.1.4. transfer & their robustness to wrong id and passwd",b1 == 3078 && b2 == 4522);

        bank.withdraw(evaid, evaPW, evaAccountID, 230);
        bank.transfer(janeid, janePW, janeAccountId, evaid, evaAccountID, 520);
        bank.deposit(evaid, evaPW, evaAccountID, 2300);
        bank.deposit(janeid, janePW, janeAccountId, 5320);
        bank.deposit(evaid, evaPW, evaAccountID, 2100);
        bank.withdraw(janeid, janePW, janeAccountId, 322);
        bank.deposit(evaid, evaPW, evaAccountID, 19);
        bank.transfer(evaid, evaPW, evaAccountID, janeid, janeAccountId, 3270);
        bank.deposit(janeid, janePW, janeAccountId, 777);
        bank.transfer(janeid, janePW, janeAccountId, evaid, evaAccountID, 9337);
        bank.deposit(janeid, janePW, janeAccountId, 555);
        bank.transfer(evaid, evaPW, evaAccountID, janeid, janeAccountId, 15034);
        b1 = bank.getBalance(janeid,janePW, janeAccountId);
        b2 = bank.getBalance(evaid,evaPW, evaAccountID);
        printOX("1.1.5. deposit + withdraw + transfer ",b1 == 17845 && b2 == 254);


        Event[] events1,events2;
        char d = 'd', w = 'w' ,s='s', r='r';
        events1 = bank.getEvents(janeid, janePW, janeAccountId);
        events2 = bank.getEvents(evaid, evaPW, evaAccountID);
        printOX("1.1.6. getEvents ",
                compareEvents(events1,new char[]{d,w,d,d,d,w,w,w,s,r,s,s,d,w,r,d,s,d,r}) &&
                        compareEvents(events2,new char[]{d,w,d,d,w,w,w,r,s,r,w,r,d,d,d,s,r,s}));
    }

    static void subproblem2() {
        Bank bank = new Bank();
        Bank bank2 = new Bank();
        int b1,b2,b3,b4;
        String janePW = "1234asdf";
        String janePW2 = "1234asdf2";
        String evaPW = "5678ghkj";
        String evaPW2 = "5678ghkj2";
        String janeid = "Jane";
        String evaid = "Eva";

        int janeAccountId = 1;
        int evaAccountID = 10;
        bank.createClient(janeid, janePW);
        bank.createClient(evaid, evaPW);
        bank2.createClient(janeid, janePW2);
        bank2.createClient(evaid, evaPW2);

        bank.createAccount(janeid, janePW , janeAccountId, 6000);
        bank.createAccount(evaid, evaPW, evaAccountID, 15000);

        bank2.createAccount(janeid, janePW2 , janeAccountId, 6000);
        bank2.createAccount(evaid, evaPW2, evaAccountID, 10000);

        Session janesession1 = SessionManager.generateSession(janeid, janePW, janeAccountId, bank);
        Session evasession1 = SessionManager.generateSession(evaid, evaPW, evaAccountID, bank);
        Session janesession2 = SessionManager.generateSession(janeid, janePW2, janeAccountId, bank2);
        Session evasession2 = SessionManager.generateSession(evaid, evaPW2, evaAccountID, bank2);

        boolean condition = janesession1 != null && janesession2 != null && evasession1 != null && evasession2 != null;
        printOX("1.2.1. Implementation of 1.1 Done ", condition);

        if(condition) {
            evasession2.deposit(400);
            evasession1.deposit(200);
            janesession2.deposit(1000);
            janesession2.deposit(200);
            janesession2.deposit(300);
            janesession2 = SessionManager.generateSession(janeid, janePW2, janeAccountId, bank2);
            janesession1.deposit(4000);
            evasession2.deposit(10);
            janesession2.deposit(730);
            janesession1.deposit(1230);
            evasession1.deposit(2200);
            janesession1.deposit(20);
            janesession1 = SessionManager.generateSession(janeid, janePW, janeAccountId, bank);
            evasession2.deposit(930);
            evasession2 = SessionManager.generateSession(evaid, evaPW2, evaAccountID, bank2);
            b1 = bank.getBalance(janeid, janePW, janeAccountId);
            b2 = bank2.getBalance(janeid, janePW2, janeAccountId);
            b3 = bank.getBalance(evaid, evaPW, evaAccountID);
            b4 = bank2.getBalance(evaid, evaPW2, evaAccountID);
            printOX("1.2.2. Session deposit + getBalance ", b1 == 11250 && b2 == 8230 && b3 == 17400 && b4 == 11340);

            janesession2.withdraw(8000);
            janesession2.withdraw(200);
            janesession2 = SessionManager.generateSession(janeid, janePW2, janeAccountId, bank2);
            janesession2.withdraw(20000);
            evasession2.withdraw(5);
            evasession2.withdraw(5);
            evasession1.withdraw(7200);
            evasession1 = SessionManager.generateSession(evaid, evaPW, evaAccountID, bank);
            evasession1.withdraw(550);
            janesession2.withdraw(5);
            janesession1.withdraw(5);
            evasession2.withdraw(5);
            evasession2 = SessionManager.generateSession(evaid, evaPW2, evaAccountID, bank2); //sessionX
            evasession2.withdraw(12000);
            janesession1.withdraw(7800);
            janesession1 = SessionManager.generateSession(janeid, janePW, janeAccountId, bank);
            b1 = bank.getBalance(janeid, janePW, janeAccountId);
            b2 = bank2.getBalance(janeid, janePW2, janeAccountId);
            b3 = bank.getBalance(evaid, evaPW, evaAccountID);
            b4 = bank2.getBalance(evaid, evaPW2, evaAccountID);
            printOX("1.2.3. Session withdraw ", b1 == 3445 && b2 == 10 && b3 == 9650 && b4 == 11325);

            evasession1.transfer(janeid, janeAccountId,600);
            janesession1.transfer(evaid, evaAccountID,6000);
            janesession2.transfer(evaid, evaAccountID,20000);
            janesession2 = SessionManager.generateSession(janeid, janePW2, janeAccountId,bank2);
            janesession1.transfer(evaid, evaAccountID,20);
            janesession2.transfer(evaid, evaAccountID, 400);
            evasession1.transfer(janeid, janeAccountId,10);
            evasession1 = SessionManager.generateSession(evaid, evaPW, evaAccountID, bank);
            evasession2.transfer(janeid, janeAccountId,10);
            b1 = bank.getBalance(janeid, janePW, janeAccountId);
            b2 = bank2.getBalance(janeid, janePW2, janeAccountId);
            b3 = bank.getBalance(evaid, evaPW, evaAccountID);
            b4 = bank2.getBalance(evaid, evaPW2, evaAccountID);
            printOX("1.2.4. Session transfer ", b1 == 4030 && b2 == 20 && b3 == 9050 && b4 == 11315);

            janesession1.deposit(5210);
            janesession1 = SessionManager.generateSession(janeid, janePW, janeAccountId,bank);
            janesession2.withdraw(8296);
            janesession2.deposit(266);
            janesession2 = SessionManager.generateSession(janeid, janePW2, janeAccountId,bank2);
            evasession1.transfer(janeid, janeAccountId,670);
            evasession2.deposit(445);
            evasession2.withdraw(3);
            evasession2 = SessionManager.generateSession(evaid, evaPW2, evaAccountID, bank2);
            janesession1.transfer(evaid, evaAccountID,23);
            janesession2.withdraw(24);
            evasession2.transfer(janeid, janeAccountId,11);
            janesession1.transfer(evaid, evaAccountID,6423);
            janesession2.deposit(731);
            janesession1.withdraw(2);
            janesession1 = SessionManager.generateSession(janeid, janePW, janeAccountId,bank);
            evasession1.deposit(2210);
            janesession1.withdraw(12000);
            janesession2.withdraw(24700);
            janesession2 = SessionManager.generateSession(janeid, janePW2, janeAccountId, bank2);
            evasession1.deposit(2);
            evasession1 = SessionManager.generateSession(evaid, evaPW, evaAccountID,bank);
            janesession2.transfer(evaid, evaAccountID, 22222);
            evasession1.withdraw(555);
            evasession2.deposit(1);
            evasession1.transfer(janeid, janeAccountId,11);
            evasession1.withdraw(7244);
            evasession1 = SessionManager.generateSession(evaid, evaPW, evaAccountID, bank);
            janesession1.deposit(1230);
            evasession2.withdraw(1);
            evasession2 = SessionManager.generateSession(evaid, evaPW2,evaAccountID, bank2);
            janesession2.deposit(250);
            janesession2.withdraw(1);
            janesession2 = SessionManager.generateSession(janeid, janePW2, janeAccountId,bank2);
            janesession1.withdraw(7800);
            janesession1 = SessionManager.generateSession(janeid, janePW, janeAccountId,bank);
            evasession2.deposit(929);
            janesession2.deposit(999);
            janesession2.transfer(evaid, evaAccountID,445);
            janesession1.deposit(20);
            evasession2.withdraw(2);
            b1 = bank.getBalance(janeid, janePW, janeAccountId);
            b2 = bank2.getBalance(janeid, janePW2, janeAccountId);
            b3 = bank.getBalance(evaid, evaPW, evaAccountID);
            b4 = bank2.getBalance(evaid, evaPW2, evaAccountID);
            printOX("1.2.5. Session deposit + withdraw + transfer", b1 == 4708 && b2 == 1792 && b3 == 9223 && b4 == 13121);
        }
    }
    static void subproblem3() {
        Bank bank = new Bank();
        Integer b1,b2,b3;
        boolean bool1,bool2;
        String janePW = "1234asdf";
        String evaPW = "5678ghkj";
        String janeid = "Jane";
        String evaid = "Eva";
        int janeAccountId = 1;
        int janeAccountId2 = 2;
        int evaAccountID = 10;

        bank.createClient(janeid, janePW);
        bank.createClient(evaid, evaPW);

        bank.createAccount(janeid, janePW, janeAccountId);
        bank.createAccount(janeid, janePW, janeAccountId2, 100);

        bank.createAccount(evaid, evaPW, evaAccountID,1000);

        MobileApp jane = new MobileApp(janeid, janePW, janeAccountId);
        MobileApp jane2 = new MobileApp(janeid, janePW, janeAccountId2);
        MobileApp eva = new MobileApp(evaid, evaPW, evaAccountID);

        Protocol.handshake(jane,bank);
        Protocol.communicate(new Deposit(),jane, bank,600);
        Protocol.communicate(new Deposit(),jane, bank, 768);
        Protocol.communicate(new Deposit(),jane, bank, 123);

        Protocol.handshake(eva,bank);
        Protocol.communicate(new Deposit(),eva, bank, 928);
        Protocol.communicate(new Deposit(),eva, bank, 1221);

        b1 = bank.getBalance(janeid,janePW, janeAccountId);
        b2 = bank.getBalance(janeid,janePW, janeAccountId2);
        b3 = bank.getBalance(evaid,evaPW, evaAccountID);

        printOX("1.3.1. deposit with secure connection", b1 == 1491 && b2 == 100 && b3 == 3149 );
//
        Protocol.handshake(eva,bank);
        Protocol.handshake(jane,bank);
        Protocol.communicate(new Withdraw(),jane, bank, 491);
        Protocol.communicate(new Withdraw(),eva, bank, 144);
        Protocol.communicate(new Withdraw(),jane, bank, 200);
        Protocol.communicate(new Withdraw(),eva, bank, 20);
        Protocol.communicate(new Withdraw(),eva, bank, 3150);
        Protocol.communicate(new Withdraw(),jane, bank, 1500);

        Protocol.handshake(jane2,bank);
        Protocol.communicate(new Withdraw(),jane2, bank, 200);

        b1 = bank.getBalance(janeid,janePW, janeAccountId);
        b2 = bank.getBalance(janeid,janePW, janeAccountId2);
        b3 = bank.getBalance(evaid,evaPW, evaAccountID);
        printOX("1.3.2. withdraw with secure connection", b1 == 790 && b2 == 100 && b3 == 2975 );

        Protocol.handshake(jane,bank);
        Protocol.handshake(jane2, bank);
        Protocol.handshake(eva,bank);
        Protocol.communicate(new Deposit(),jane, bank, 3900);
        Protocol.communicate(new Deposit(),eva, bank, 5000);
        bank.transfer(janeid, janePW, janeAccountId, evaid,evaAccountID, 20);
        bank.transfer(evaid, evaPW, evaAccountID, janeid, janeAccountId2, 320);
        bank.transfer(janeid, janePW, janeAccountId, evaid, evaAccountID, 1100);
        bank.transfer(janeid, janePW, janeAccountId, evaid, evaAccountID, 1000);
        bank.transfer(evaid, evaPW, evaAccountID, janeid, janeAccountId2, 1925);
        bank.transfer(janeid, janePW, janeAccountId, evaid, evaAccountID, 62000);
        bank.transfer(evaid, evaPW, evaAccountID,  janeid,janeAccountId2, 7000);

        bool1 = bank.transfer(evaid, evaPW, evaAccountID, janeid, janeAccountId, 7000);
        bool2 = bank.transfer(janeid, janePW, janeAccountId, evaid,evaAccountID, 62000);

        b1 = bank.getBalance(janeid,janePW, janeAccountId);
        b2 = bank.getBalance(janeid,janePW, janeAccountId2);
        b3 = bank.getBalance(evaid,evaPW, evaAccountID);

        printOX("1.3.3. deposit and withdraw with secure connection",
                b1 == 2555 &&  b2 == 9345 && b3 == 835 &&
                        !bool1  && !bool2);

        Event[] events1,events2, events3;
        char d = 'd', w = 'w' ,s='s',r='r';
        events1 = bank.getEvents(janeid,janePW, janeAccountId);
        events2 = bank.getEvents(janeid, janePW, janeAccountId2);
        events3 = bank.getEvents(evaid,evaPW, evaAccountID);
        printOX("1.3.4. getEvents with secure connection",
                events1 != null && compareEvents(events1,new char[]{d, d, d, w, w, d, s, s, s}) &&
                        events2 != null && compareEvents(events2,new char[]{r, r, r}) &&
                        events3 != null && compareEvents(events3,new char[]{d,d,w,w,d,r,s,r,r,s,s}) );

    }
    static void printOX(String prompt,boolean condition){
        if(condition){
            System.out.println("" + prompt + " | O");
        }
        else{
            System.out.println("" + prompt + " | X");
        }
    }
    static void print(Object o){
        System.out.println(o);
    }
    static void print(Event[] events){
        for(Event e : events){
            System.out.println(e);
        }
    }
    static boolean compareEvents(Event[] events,char[] answer ){
        if(events == null){
            return false;
        }
        if(events.length != answer.length){
            return false;
        }
        for(int i = 0; i < events.length; i++){
            switch(answer[i]){
                case 'd':
                    if (!(events[i] instanceof DepositEvent)){
                        return false;
                    }
                    break;
                case 'r':
                    if (!(events[i] instanceof ReceiveEvent)){
                        return false;
                    }
                    break;
                case 's' :
                    if (!(events[i] instanceof SendEvent)){
                        return false;
                    }
                    break;
                case 'w' :
                    if (!(events[i] instanceof WithdrawEvent)){
                        return false;
                    }
                    break;
            }
        }
        return true;
    }
}
