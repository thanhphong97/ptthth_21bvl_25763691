package bai1;

import java.rmi.server.UnicastRemoteObject;

public class Hello extends UnicastRemoteObject implements IHello { 
    public Hello() throws java.rmi.RemoteException {
        super();
    }

    @Override
    public String sayHello() {
        return "Hello, World!";
    }
    
}
