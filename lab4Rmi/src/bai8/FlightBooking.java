package bai8;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class FlightBooking extends UnicastRemoteObject implements IFlightBooking {

    private final HashMap<String, Integer> capacity = new HashMap<>();
    private final HashMap<String, Integer> available = new HashMap<>();

    public FlightBooking() throws RemoteException {
        super();
        addFlight("VN123", 100);
        addFlight("VJ456", 80);
        addFlight("QH789", 50);
    }

    private void addFlight(String flightNumber, int seats) {
        capacity.put(flightNumber, seats);
        available.put(flightNumber, seats);
    }

    @Override
    public synchronized boolean bookTicket(String flightNumber, int seats) throws RemoteException {
        Integer free = available.get(flightNumber);
        if (free == null || seats <= 0 || seats > free) {
            System.out.println("Đặt " + seats + " ghế chuyến " + flightNumber + " thất bại");
            return false;
        }
        available.put(flightNumber, free - seats);
        System.out.println("Đặt " + seats + " ghế chuyến " + flightNumber + ", còn trống: " + (free - seats));
        return true;
    }

    @Override
    public synchronized int availableSeats(String flightNumber) throws RemoteException {
        Integer free = available.get(flightNumber);
        System.out.println("Kiểm tra ghế trống chuyến " + flightNumber + ": " + free);
        return free == null ? -1 : free;
    }

    @Override
    public synchronized boolean cancelBooking(String flightNumber, int seats) throws RemoteException {
        Integer free = available.get(flightNumber);
        if (free == null || seats <= 0 || free + seats > capacity.get(flightNumber)) {
            System.out.println("Hủy " + seats + " ghế chuyến " + flightNumber + " thất bại");
            return false;
        }
        available.put(flightNumber, free + seats);
        System.out.println("Hủy " + seats + " ghế chuyến " + flightNumber + ", còn trống: " + (free + seats));
        return true;
    }
}
