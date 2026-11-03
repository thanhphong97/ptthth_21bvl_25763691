package bai8;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            IFlightBooking flightBooking = new FlightBooking();
            registry.rebind("FlightBookingServer", flightBooking);
            System.out.println("bai8.FlightBookingServer running...");
        } catch (Exception e) {
            System.err.println("Lỗi Server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
