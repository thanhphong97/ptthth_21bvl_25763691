package bai5;

import java.rmi.*;

public interface IChat extends Remote {
    void registerClient(IChatClient client) throws RemoteException;

    void sendMessage(String message) throws RemoteException;
}
