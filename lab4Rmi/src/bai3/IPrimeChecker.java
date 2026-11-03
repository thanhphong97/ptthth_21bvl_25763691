package bai3;

import java.rmi.*;

public interface IPrimeChecker extends Remote {
    boolean isPrime(int number) throws RemoteException;
}
