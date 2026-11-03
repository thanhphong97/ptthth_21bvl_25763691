package bai5;

import java.rmi.NoSuchObjectException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ChatClient chatClient = null;
        try {
            Registry registry = LocateRegistry.getRegistry();
            IChat chat = (IChat) registry.lookup("ChatServer");

            chatClient = new ChatClient();
            chat.registerClient(chatClient);
            System.out.println("Đã kết nối tới server. Nhập tin nhắn (gõ 'exit' để thoát):");

            while (scanner.hasNextLine()) {
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
                chat.sendMessage(message);
            }
        } catch (Exception e) {
            System.err.println("Lỗi Client: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
            // Gỡ export callback để JVM của client có thể kết thúc
            if (chatClient != null) {
                try {
                    UnicastRemoteObject.unexportObject(chatClient, true);
                } catch (NoSuchObjectException ignored) {
                }
            }
        }
    }
}
