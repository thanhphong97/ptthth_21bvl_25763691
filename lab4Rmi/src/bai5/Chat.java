package bai5;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Chat extends UnicastRemoteObject implements IChat {

    private IChatClient client;

    public Chat() throws RemoteException {
        super();
    }

    @Override
    public synchronized void registerClient(IChatClient client) throws RemoteException {
        this.client = client;
        System.out.println("Client đã kết nối. Nhập tin nhắn để trả lời:");
    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        System.out.println("Client: " + message);
    }

    public synchronized void reply(String message) {
        if (client == null) {
            System.out.println("Chưa có client kết nối!");
            return;
        }
        try {
            client.receiveMessage(message);
        } catch (RemoteException e) {
            System.out.println("Client đã ngắt kết nối.");
            client = null;
        }
    }
}
