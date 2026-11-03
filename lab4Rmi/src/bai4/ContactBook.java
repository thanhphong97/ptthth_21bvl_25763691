package bai4;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class ContactBook extends UnicastRemoteObject implements IContactBook {

    private final HashMap<String, String> contacts = new HashMap<>();

    public ContactBook() throws RemoteException {
        super();
    }

    @Override
    public synchronized void addContact(String name, String phone) throws RemoteException {
        String old = contacts.put(name, phone);
        if (old == null) {
            System.out.println("Thêm liên hệ: " + name + " - " + phone);
        } else {
            System.out.println("Sửa liên hệ: " + name + " - " + old + " -> " + phone);
        }
    }

    @Override
    public synchronized String findContact(String name) throws RemoteException {
        System.out.println("Tìm liên hệ: " + name);
        return contacts.get(name);
    }

    @Override
    public synchronized boolean deleteContact(String name) throws RemoteException {
        System.out.println("Xóa liên hệ: " + name);
        return contacts.remove(name) != null;
    }
}
