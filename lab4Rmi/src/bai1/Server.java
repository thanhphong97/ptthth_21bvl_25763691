package bai1;

public class Server {
    
    public static void main(String[] args) {
        try {
            java.rmi.registry.Registry registry = java.rmi.registry.LocateRegistry.createRegistry(1099);
            IHello hello = new Hello();
            registry.rebind("HelloServer", hello);
            System.out.println("bai1.HelloServer running...");
        } catch (Exception e) {
            System.err.println("Lỗi Server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
