package bai3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PrimeChecker extends UnicastRemoteObject implements IPrimeChecker {

    PrimeChecker() throws java.rmi.RemoteException {
        super();
    }

    @Override
    public boolean isPrime(int number) throws RemoteException {
        if (number < 2) {
            return false;
        }

        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}
