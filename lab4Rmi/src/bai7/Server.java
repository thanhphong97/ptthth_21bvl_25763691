package bai7;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            IAreaCalculator calculator = new AreaCalculator();
            registry.rebind("AreaCalculatorServer", calculator);
            System.out.println("bai7.AreaCalculatorServer running...");
        } catch (Exception e) {
            System.err.println("Lỗi Server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
