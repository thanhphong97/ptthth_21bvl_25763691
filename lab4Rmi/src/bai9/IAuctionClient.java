package bai9;

import java.rmi.*;

public interface IAuctionClient extends Remote {
    void receiveNotification(String message) throws RemoteException;
}
