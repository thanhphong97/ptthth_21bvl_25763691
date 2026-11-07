package bai8;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            Registry registry = LocateRegistry.getRegistry();
            IFlightBooking flightBooking = (IFlightBooking) registry.lookup("FlightBookingServer");

            while (true) {
                System.out.println("\n===== ĐẶT VÉ MÁY BAY =====");
                System.out.println("Các chuyến bay: VN123, VJ456, QH789");
                System.out.println("1. Đặt vé");
                System.out.println("2. Hủy vé");
                System.out.println("3. Kiểm tra số ghế trống");
                System.out.println("0. Thoát");
                System.out.print("Chọn: ");
                String choice = scanner.nextLine().trim();

                if (choice.equals("0")) {
                    break;
                }

                switch (choice) {
                    case "1": {
                        System.out.print("Nhập mã chuyến bay: ");
                        String flight = scanner.nextLine().trim().toUpperCase();
                        int seats = readSeats(scanner);
                        System.out.println(flightBooking.bookTicket(flight, seats)
                                ? "Đặt vé thành công!"
                                : "Đặt vé thất bại: chuyến bay không tồn tại hoặc không đủ ghế!");
                        break;
                    }
                    case "2": {
                        System.out.print("Nhập mã chuyến bay: ");
                        String flight = scanner.nextLine().trim().toUpperCase();
                        int seats = readSeats(scanner);
                        System.out.println(flightBooking.cancelBooking(flight, seats)
                                ? "Hủy vé thành công!"
                                : "Hủy vé thất bại: chuyến bay không tồn tại hoặc số ghế hủy không hợp lệ!");
                        break;
                    }
                    case "3": {
                        System.out.print("Nhập mã chuyến bay: ");
                        String flight = scanner.nextLine().trim().toUpperCase();
                        int free = flightBooking.availableSeats(flight);
                        System.out.println(free >= 0
                                ? "Chuyến " + flight + " còn " + free + " ghế trống"
                                : "Không tìm thấy chuyến bay " + flight);
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

    private static int readSeats(Scanner scanner) {
        while (true) {
            System.out.print("Nhập số ghế: ");
            try {
                int seats = Integer.parseInt(scanner.nextLine().trim());
                if (seats > 0) {
                    return seats;
                }
                System.out.println("Số ghế phải lớn hơn 0!");
            } catch (NumberFormatException e) {
                System.out.println("Số ghế không hợp lệ!");
            }
        }
    }
}
