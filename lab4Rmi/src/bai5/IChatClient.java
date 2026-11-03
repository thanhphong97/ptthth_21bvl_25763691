package bai5;

import java.rmi.*;

public interface IChatClient extends Remote {
    void receiveMessage(String message) throws RemoteException;
}
