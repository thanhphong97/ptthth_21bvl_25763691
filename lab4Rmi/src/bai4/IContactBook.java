package bai4;

import java.rmi.*;

public interface IContactBook extends Remote {
    void addContact(String name, String phone) throws RemoteException;

    String findContact(String name) throws RemoteException;

    boolean deleteContact(String name) throws RemoteException;
}
