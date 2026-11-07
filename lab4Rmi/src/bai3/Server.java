package bai3;

public class Server {
    
    public static void main(String[] args) {
        try {
            java.rmi.registry.Registry registry = java.rmi.registry.LocateRegistry.createRegistry(1099);
            IPrimeChecker primeChecker = new PrimeChecker();
            registry.rebind("PrimeCheckerServer", primeChecker);
            System.out.println("bai3.PrimeCheckerServer running...");
        } catch (Exception e) {
            System.err.println("Lỗi Server: " + e.getMessage());
            e.printStackTrace();
        }
    }   
}
