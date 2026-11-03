package bai6;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            IBankAccount account = new BankAccount(1000000);
            registry.rebind("BankAccountServer", account);
            System.out.println("bai6.BankAccountServer running...");
        } catch (Exception e) {
            System.err.println("Lỗi Server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
