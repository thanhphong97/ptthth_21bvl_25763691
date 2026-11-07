package bai6;

import java.rmi.*;

public interface IBankAccount extends Remote {
    double getBalance() throws RemoteException;

    void deposit(double amount) throws RemoteException;

    boolean withdraw(double amount) throws RemoteException;
}
