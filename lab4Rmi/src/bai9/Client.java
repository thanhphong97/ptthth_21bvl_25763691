package bai9;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        IAuction auction = null;
        AuctionClient auctionClient = null;
        try {
            Registry registry = LocateRegistry.getRegistry();
            auction = (IAuction) registry.lookup("AuctionServer");

            System.out.print("Nhập tên của bạn: ");
            String userName = scanner.nextLine().trim();

            auctionClient = new AuctionClient();
            auction.registerClient(auctionClient);

            while (true) {
                System.out.println("\n===== ĐẤU GIÁ TRỰC TUYẾN =====");
                System.out.println("1. Thêm sản phẩm đấu giá (người bán)");
                System.out.println("2. Xem danh sách sản phẩm");
                System.out.println("3. Đặt giá thầu (người đấu giá)");
                System.out.println("0. Thoát");
                System.out.print("Chọn: ");
                String choice = scanner.nextLine().trim();

                if (choice.equals("0")) {
                    break;
                }

                switch (choice) {
                    case "1": {
                        System.out.print("Nhập tên sản phẩm: ");
                        String productName = scanner.nextLine().trim();
                        double startPrice = readPrice(scanner, "Nhập giá khởi điểm: ");
                        System.out.println(auction.addProduct(userName, productName, startPrice)
                                ? "Thêm sản phẩm thành công!"
                                : "Thêm thất bại: sản phẩm đã tồn tại!");
                        break;
                    }
                    case "2": {
                        List<Product> products = auction.getProducts();
                        if (products.isEmpty()) {
                            System.out.println("Chưa có sản phẩm nào.");
                        }
                        for (Product product : products) {
                            System.out.println(product);
                        }
                        break;
                    }
                    case "3": {
                        System.out.print("Nhập tên sản phẩm: ");
                        String productName = scanner.nextLine().trim();
                        double amount = readPrice(scanner, "Nhập giá thầu: ");
                        System.out.println(auction.placeBid(userName, productName, amount)
                                ? "Đặt giá thành công!"
                                : "Đặt giá thất bại: sản phẩm không tồn tại hoặc giá chưa cao hơn giá hiện tại!");
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
            if (auctionClient != null) {
                try {
                    auction.unregisterClient(auctionClient);
                    UnicastRemoteObject.unexportObject(auctionClient, true);
                } catch (Exception ignored) {
                }
            }
        }
    }

    private static double readPrice(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double price = Double.parseDouble(scanner.nextLine().trim());
                if (price > 0) {
                    return price;
                }
                System.out.println("Giá phải lớn hơn 0!");
            } catch (NumberFormatException e) {
                System.out.println("Giá không hợp lệ!");
            }
        }
    }
}
