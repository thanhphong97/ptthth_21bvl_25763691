package bai2;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

	public static void main(String[] args) {
		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			ICalculator calculator = new Calculator();
			registry.rebind("CalculatorServer", calculator);
			System.out.println("bai2.CalculatorServer running...");
		} catch (Exception e) {
			System.err.println("Lỗi Server: " + e.getMessage());
            e.printStackTrace();
		}
	}
}
