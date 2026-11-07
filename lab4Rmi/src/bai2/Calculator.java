package bai2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Calculator extends UnicastRemoteObject implements ICalculator{

	protected Calculator() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int add(int a, int b) throws RemoteException {
		System.out.println("Server nhận được yêu cầu tính: " + a + " + " + b);
        return a + b;
	}

}
