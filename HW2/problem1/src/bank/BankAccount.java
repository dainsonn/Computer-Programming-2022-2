package bank;

import bank.event.*;

import java.util.Arrays;

class BankAccount {
    private Event[] events = new Event[maxEvents];
    final static int maxEvents = 100;

    private int event_num;
    private int accountId;
    private int balance;

    BankAccount(int accountID, int balance) {
        this.accountId = accountID;
        this.balance = balance;
    }

    int getAccountId() {
        return accountId;
    }
    int getBalance() {
        return balance;
    }

    Event[] getEvents() {
        Event[] events;
        events = this.events.clone();
        return events;
    }

    void deposit(int amount) {
        balance += amount;
        for(int i=0; i<maxEvents; i++){
            if(events[i] == null){
                events[i] = new DepositEvent();
                break;
            }
        }
    }

    boolean withdraw(int amount, String membership) {
        if(membership.equals("Normal")){
            amount += 5;
        }
        if(amount <= balance){
            balance -= amount;
            for(int i=0; i<maxEvents; i++){
                if(events[i] == null){
                    events[i] = new WithdrawEvent();
                    break;
                }
            }
            return true;
        }
        else return false;
    }

    void receive(int amount) {
        balance += amount;
        for(int i=0; i<maxEvents; i++){
            if(events[i] == null){
                events[i] = new ReceiveEvent();
                break;
            }
        }
    }

    boolean send(int amount, String membership) {
        if(membership.equals("Normal")){
            amount += 5;
        }
        if(amount <= balance){
            balance -= amount;
            for(int i=0; i<maxEvents; i++){
                if(events[i] == null){
                    events[i] = new SendEvent();
                    break;
                }
            }
            return true;
        }
        else return false;
    }
}
