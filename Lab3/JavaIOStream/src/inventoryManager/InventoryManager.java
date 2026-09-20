package inventoryManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
	public static void main(String[] args) {
		Path csvFile = Path.of("data", "inventory.csv");
		Path report = Path.of("data", "inventory-report.txt");

		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
		List<Product> inputProducts = new ArrayList<>();

		System.out.println("Nhập sản phẩm theo dạng: ma,ten,donGia,soLuong");
		System.out.println("Nhập q để kết thúc (nhập q ngay nếu chỉ muốn đọc tệp có sẵn).");
		try {
			int inputNumber = 0;
			while (true) {
				String line = keyboard.readLine();
				if (line == null || line.equalsIgnoreCase("q")) {
					break;
				}
				inputNumber++;

				String[] parts = line.split(",", -1);
				if (parts.length != 4) {
					System.out.println("Từ chối dòng nhập " + inputNumber + ": cần đúng 4 giá trị");
					continue;
				}
				try {
					inputProducts.add(new Product(parts[0].trim(), parts[1].trim(), Double.parseDouble(parts[2].trim()),
							Integer.parseInt(parts[3].trim())));
					System.out.println("Đã thêm dòng nhập " + inputNumber);
				} catch (IllegalArgumentException e) {
					System.out.println("Từ chối dòng nhập " + inputNumber + ": " + e.getMessage());
				}
			}
		} catch (IOException e) {
			System.err.println("Không thể đọc dữ liệu từ bàn phím: " + e.getMessage());
			return;
		}

		if (!inputProducts.isEmpty()) {
			try {
				Files.createDirectories(csvFile.getParent());
				try (BufferedWriter writer = Files.newBufferedWriter(csvFile, StandardCharsets.UTF_8)) {
					writer.write("ma,ten,donGia,soLuong");
					writer.newLine();
					for (Product product : inputProducts) {
						writer.write("%s,%s,%.0f,%d".formatted(product.getCode(), product.getName(),
								product.getUnitPrice(), product.getQuantity()));
						writer.newLine();
					}
				}
				System.out.println("Đã lưu " + inputProducts.size() + " sản phẩm vào " + csvFile.toAbsolutePath());
			} catch (IOException e) {
				System.err.println("Không ghi được tệp " + csvFile + ": " + e.getMessage());
				return;
			}
		}

		List<Product> products = new ArrayList<>();
		try (BufferedReader reader = Files.newBufferedReader(csvFile, StandardCharsets.UTF_8)) {
			reader.readLine();
			int lineNumber = 1;
			while (true) {
				String line = reader.readLine();
				if (line == null) {
					break;
				}
				lineNumber++;

				if (line.isBlank()) {
					continue;
				}

				String[] parts = line.split(",", -1);
				if (parts.length != 4) {
					System.err.println("Tệp " + csvFile + " - bỏ qua dòng " + lineNumber + ": thiếu hoặc thừa cột");
					continue;
				}
				try {
					products.add(new Product(parts[0].trim(), parts[1].trim(), Double.parseDouble(parts[2].trim()),
							Integer.parseInt(parts[3].trim())));
				} catch (IllegalArgumentException e) {
					System.err.println("Tệp " + csvFile + " - dòng " + lineNumber + " không hợp lệ: " + e.getMessage());
				}
			}
		} catch (NoSuchFileException e) {
			System.err.println("Không tìm thấy tệp: " + csvFile.toAbsolutePath());
			return;
		} catch (IOException e) {
			System.err.println("Không đọc được tệp " + csvFile + ": " + e.getMessage());
			return;
		}

		if (products.isEmpty()) {
			System.out.println("Tệp " + csvFile + " không có sản phẩm hợp lệ.");
			return;
		}

		double total = 0;
		Product highest = products.get(0);
		for (Product product : products) {
			System.out.println(product);
			total += product.inventoryValue();
			if (product.inventoryValue() > highest.inventoryValue()) {
				highest = product;
			}
		}
		System.out.println("Tổng giá trị tồn kho: %,.0f VND".formatted(total));
		System.out.println("Giá trị tồn kho cao nhất: " + highest);

		try (BufferedWriter writer = Files.newBufferedWriter(report, StandardCharsets.UTF_8)) {
			writer.write("Số sản phẩm: " + products.size());
			writer.newLine();
			writer.write("Tổng giá trị tồn kho: %,.0f VND".formatted(total));
			writer.newLine();
			writer.write("Giá trị tồn kho cao nhất: " + highest);
			writer.newLine();
			writer.write("Danh sách:");
			writer.newLine();
			for (Product product : products) {
				writer.write(product.toString());
				writer.newLine();
			}
			System.out.println("Đã ghi báo cáo vào " + report.toAbsolutePath());
		} catch (IOException e) {
			System.err.println("Không ghi được báo cáo " + report + ": " + e.getMessage());
		}
	}

}
