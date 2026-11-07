package bai8;

import java.rmi.*;

public interface IFlightBooking extends Remote {
    boolean bookTicket(String flightNumber, int seats) throws RemoteException;

    int availableSeats(String flightNumber) throws RemoteException;

    boolean cancelBooking(String flightNumber, int seats) throws RemoteException;
}
