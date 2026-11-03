package bai6;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class BankAccount extends UnicastRemoteObject implements IBankAccount {

    private double balance;

    public BankAccount(double initialBalance) throws RemoteException {
        super();
        this.balance = initialBalance;
    }

    @Override
    public synchronized double getBalance() throws RemoteException {
        System.out.println("Kiểm tra số dư: " + balance);
        return balance;
    }

    @Override
    public synchronized void deposit(double amount) throws RemoteException {
        if (amount <= 0) {
            System.out.println("Từ chối gửi số tiền không hợp lệ: " + amount);
            return;
        }
        balance += amount;
        System.out.println("Gửi " + amount + ", số dư mới: " + balance);
    }

    @Override
    public synchronized boolean withdraw(double amount) throws RemoteException {
        if (amount <= 0 || amount > balance) {
            System.out.println("Rút " + amount + " thất bại, số dư: " + balance);
            return false;
        }
        balance -= amount;
        System.out.println("Rút " + amount + ", số dư mới: " + balance);
        return true;
    }
}
