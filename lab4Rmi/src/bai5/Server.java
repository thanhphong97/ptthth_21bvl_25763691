package bai5;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            Chat chat = new Chat();
            registry.rebind("ChatServer", chat);
            System.out.println("bai5.ChatServer running...");

            while (scanner.hasNextLine()) {
                chat.reply(scanner.nextLine());
            }
        } catch (Exception e) {
            System.err.println("Lỗi Server: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
