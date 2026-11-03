package bai7;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            Registry registry = LocateRegistry.getRegistry();
            IAreaCalculator calculator = (IAreaCalculator) registry.lookup("AreaCalculatorServer");

            while (true) {
                System.out.println("\n===== TÍNH DIỆN TÍCH =====");
                System.out.println("1. Hình chữ nhật");
                System.out.println("2. Hình tròn");
                System.out.println("3. Hình tam giác");
                System.out.println("0. Thoát");
                System.out.print("Chọn: ");
                String choice = scanner.nextLine().trim();

                if (choice.equals("0")) {
                    break;
                }

                switch (choice) {
                    case "1": {
                        double width = readPositive(scanner, "Nhập chiều rộng: ");
                        double height = readPositive(scanner, "Nhập chiều dài: ");
                        System.out.println("Diện tích hình chữ nhật: " + calculator.rectangleArea(width, height));
                        break;
                    }
                    case "2": {
                        double radius = readPositive(scanner, "Nhập bán kính: ");
                        System.out.println("Diện tích hình tròn: " + calculator.circleArea(radius));
                        break;
                    }
                    case "3": {
                        double base = readPositive(scanner, "Nhập cạnh đáy: ");
                        double height = readPositive(scanner, "Nhập chiều cao: ");
                        System.out.println("Diện tích tam giác: " + calculator.triangleArea(base, height));
                        break;
                    }
                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi Client: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static double readPositive(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value > 0) {
                    return value;
                }
                System.out.println("Giá trị phải lớn hơn 0!");
            } catch (NumberFormatException e) {
                System.out.println("Giá trị không hợp lệ!");
            }
        }
    }
}
