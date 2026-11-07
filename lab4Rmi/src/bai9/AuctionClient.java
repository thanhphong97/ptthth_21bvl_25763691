package bai9;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AuctionClient extends UnicastRemoteObject implements IAuctionClient {

    public AuctionClient() throws RemoteException {
        super();
    }

    @Override
    public void receiveNotification(String message) throws RemoteException {
        System.out.println("\n[THÔNG BÁO] " + message);
    }
}
