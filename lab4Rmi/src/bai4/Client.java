package bai4;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            Registry registry = LocateRegistry.getRegistry();
            IContactBook contactBook = (IContactBook) registry.lookup("ContactBookServer");

            while (true) {
                System.out.println("\n===== QUẢN LÝ DANH BẠ =====");
                System.out.println("1. Thêm liên hệ");
                System.out.println("2. Sửa liên hệ");
                System.out.println("3. Xóa liên hệ");
                System.out.println("4. Tìm kiếm liên hệ");
                System.out.println("0. Thoát");
                System.out.print("Chọn: ");
                String choice = scanner.nextLine().trim();

                if (choice.equals("0")) {
                    break;
                }

                switch (choice) {
                    case "1": {
                        System.out.print("Nhập tên: ");
                        String name = scanner.nextLine().trim();
                        if (contactBook.findContact(name) != null) {
                            System.out.println("Liên hệ đã tồn tại, hãy dùng chức năng sửa.");
                            break;
                        }
                        System.out.print("Nhập số điện thoại: ");
                        String phone = scanner.nextLine().trim();
                        contactBook.addContact(name, phone);
                        System.out.println("Đã thêm liên hệ " + name);
                        break;
                    }
                    case "2": {
                        System.out.print("Nhập tên cần sửa: ");
                        String name = scanner.nextLine().trim();
                        if (contactBook.findContact(name) == null) {
                            System.out.println("Không tìm thấy liên hệ " + name);
                            break;
                        }
                        System.out.print("Nhập số điện thoại mới: ");
                        String phone = scanner.nextLine().trim();
                        contactBook.addContact(name, phone);
                        System.out.println("Đã cập nhật liên hệ " + name);
                        break;
                    }
                    case "3": {
                        System.out.print("Nhập tên cần xóa: ");
                        String name = scanner.nextLine().trim();
                        boolean deleted = contactBook.deleteContact(name);
                        System.out.println(deleted ? "Đã xóa liên hệ " + name : "Không tìm thấy liên hệ " + name);
                        break;
                    }
                    case "4": {
                        System.out.print("Nhập tên cần tìm: ");
                        String name = scanner.nextLine().trim();
                        String phone = contactBook.findContact(name);
                        System.out.println(phone != null ? name + ": " + phone : "Không tìm thấy liên hệ " + name);
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
}
