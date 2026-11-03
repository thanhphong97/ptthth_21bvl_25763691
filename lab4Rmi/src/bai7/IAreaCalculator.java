package bai7;

import java.rmi.*;

public interface IAreaCalculator extends Remote {
    double rectangleArea(double width, double height) throws RemoteException;

    double circleArea(double radius) throws RemoteException;

    double triangleArea(double base, double height) throws RemoteException;
}
