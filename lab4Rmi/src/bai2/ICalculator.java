package bai2;

import java.rmi.*;

public interface ICalculator extends Remote {
 int add(int a, int b) throws RemoteException;
}
