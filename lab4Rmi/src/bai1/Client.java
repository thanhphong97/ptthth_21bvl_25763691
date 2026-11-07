package bai1;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    public static void main(String[] args) {
        try {
            java.rmi.registry.Registry registry = java.rmi.registry.LocateRegistry.getRegistry();
            IHello hello = (IHello) registry.lookup("HelloServer");
            String response = hello.sayHello();
            System.out.println("Kết quả trả về từ Server: " + response);
        } catch (NotBoundException | RemoteException e) {
            System.err.println("Lỗi Client: " + e.getMessage());
        }

    }
}