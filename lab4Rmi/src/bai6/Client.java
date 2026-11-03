package bai6;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            Registry registry = LocateRegistry.getRegistry();
            IBankAccount account = (IBankAccount) registry.lookup("BankAccountServer");

            while (true) {
                System.out.println("\n===== TÀI KHOẢN NGÂN HÀNG =====");
                System.out.println("1. Kiểm tra số dư");
                System.out.println("2. Gửi tiền");
                System.out.println("3. Rút tiền");
                System.out.println("0. Thoát");
                System.out.print("Chọn: ");
                String choice = scanner.nextLine().trim();

                if (choice.equals("0")) {
                    break;
                }

                switch (choice) {
                    case "1":
                        System.out.println("Số dư hiện tại: " + account.getBalance());
                        break;
                    case "2": {
                        double amount = readAmount(scanner, "Nhập số tiền cần gửi: ");
                        if (amount > 0) {
                            account.deposit(amount);
                            System.out.println("Gửi tiền thành công. Số dư: " + account.getBalance());
                        }
                        break;
                    }
                    case "3": {
                        double amount = readAmount(scanner, "Nhập số tiền cần rút: ");
                        if (amount > 0) {
                            if (account.withdraw(amount)) {
                                System.out.println("Rút tiền thành công. Số dư: " + account.getBalance());
                            } else {
                                System.out.println("Rút tiền thất bại: số dư không đủ!");
                            }
                        }
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

    private static double readAmount(Scanner scanner, String prompt) {
        System.out.print(prompt);
        try {
            double amount = Double.parseDouble(scanner.nextLine().trim());
            if (amount <= 0) {
                System.out.println("Số tiền phải lớn hơn 0!");
            }
            return amount;
        } catch (NumberFormatException e) {
            System.out.println("Số tiền không hợp lệ!");
            return -1;
        }
    }
}
