package bai7;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AreaCalculator extends UnicastRemoteObject implements IAreaCalculator {

    public AreaCalculator() throws RemoteException {
        super();
    }

    @Override
    public double rectangleArea(double width, double height) throws RemoteException {
        System.out.println("Tính diện tích hình chữ nhật: " + width + " x " + height);
        return width * height;
    }

    @Override
    public double circleArea(double radius) throws RemoteException {
        System.out.println("Tính diện tích hình tròn bán kính: " + radius);
        return Math.PI * radius * radius;
    }

    @Override
    public double triangleArea(double base, double height) throws RemoteException {
        System.out.println("Tính diện tích tam giác: đáy " + base + ", cao " + height);
        return 0.5 * base * height;
    }
}
