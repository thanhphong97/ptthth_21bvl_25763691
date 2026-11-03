package bai9;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            IAuction auction = new Auction();
            registry.rebind("AuctionServer", auction);
            System.out.println("bai9.AuctionServer running...");
        } catch (Exception e) {
            System.err.println("Lỗi Server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
