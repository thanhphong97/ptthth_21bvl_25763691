package bai9;

import java.rmi.*;
import java.util.List;

public interface IAuction extends Remote {
    boolean addProduct(String seller, String productName, double startPrice) throws RemoteException;

    List<Product> getProducts() throws RemoteException;

    boolean placeBid(String bidder, String productName, double amount) throws RemoteException;

    void registerClient(IAuctionClient client) throws RemoteException;

    void unregisterClient(IAuctionClient client) throws RemoteException;
}
