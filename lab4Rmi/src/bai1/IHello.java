package bai1;
import java.rmi.*;
public interface IHello extends Remote {
 String sayHello() throws RemoteException;
}