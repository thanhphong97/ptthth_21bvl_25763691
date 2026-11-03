package bai5;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatClient extends UnicastRemoteObject implements IChatClient {

    public ChatClient() throws RemoteException {
        super();
    }

    @Override
    public void receiveMessage(String message) throws RemoteException {
        System.out.println("Server: " + message);
    }
}
