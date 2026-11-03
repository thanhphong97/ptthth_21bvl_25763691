package bai3;

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            java.rmi.registry.Registry registry = java.rmi.registry.LocateRegistry.getRegistry();
            IPrimeChecker primeChecker = (IPrimeChecker) registry.lookup("PrimeCheckerServer");
            System.out.print("Nhập một số nguyên để kiểm tra: ");
            int numberToCheck = scanner.nextInt();
            boolean isPrime = primeChecker.isPrime(numberToCheck);
            System.out.println("Số " + numberToCheck + " là số nguyên tố: " + isPrime);
        } catch (Exception e) {
            System.err.println("Lỗi Client: " + e.getMessage());
            e.printStackTrace();
        }finally {
            scanner.close();
        }
    }
}
