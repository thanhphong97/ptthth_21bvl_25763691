package bai2;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		try {
//			Registry registry = LocateRegistry.getRegistry("localhost", 1099);
			Registry registry = LocateRegistry.getRegistry();
			
			ICalculator calculator = (ICalculator) registry.lookup("CalculatorServer");
			System.out.print("Nhập số nguyên a: ");
            int a = scanner.nextInt();
            System.out.print("Nhập số nguyên b: ");
            int b = scanner.nextInt();
            int result = calculator.add(a, b);
            System.out.println("Kết quả trả về từ Server: " + a + " + " + b + " = " + result);
		} catch (Exception e) {
			System.err.println("Lỗi Client: " + e.getMessage());
			e.printStackTrace();
		}finally {
			scanner.close();
		}

	}

}
