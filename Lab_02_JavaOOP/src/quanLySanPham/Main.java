package quanLySanPham;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		ArrayList<SanPham> stores = new ArrayList<SanPham>();
		
		var coke = new SanPham("SP001", "nước ngọt Coca-cola", 20000, 500);
		var bakery = new SanPham("SP002", "bánh mì Sài Gòn", 15000, 10);
		var chewingGum = new SanPham("SP003", "kẹo cool-air", 5500.54, 100);
		
		stores.add(coke);
		stores.add(bakery);
		stores.add(chewingGum);
		
		System.out.println("===============");
		System.out.println("Kiểm tra kho của cửa hàng:");
		PrintProductInformation(stores);
		
		System.out.println("Nhập số lượng bánh mì cần thêm hàng: ");
		int bakeryInbound = scanner.nextInt();
		bakery.nhapHang(bakeryInbound);
		
		System.out.println("===============");
		System.out.println("Kiểm tra kho của cửa hàng sau khi nhập thêm:");
		PrintProductInformation(stores);
		
		System.out.println("Bán 30 " + coke.getTenSanPham());
		var sellCokeResult = coke.banHang(30);
		PurchaseResult(sellCokeResult, 30, coke);
		
		System.out.println("===============");
		System.out.println("Kiểm tra kho của cửa hàng sau khi bán hàng:");
		PrintProductInformation(stores);
		
		System.out.println("Bán 1000 " + chewingGum.getTenSanPham());
		var sellchewingGumResult = chewingGum.banHang(1000);
		PurchaseResult(sellchewingGumResult, 1000, chewingGum);
			
		System.out.println("===============");
		System.out.println("Kiểm tra kho của cửa hàng sau khi bán hàng:");
		PrintProductInformation(stores);
		
		scanner.close();
		
	}
	
	private static void PrintProductInformation(ArrayList<SanPham> stores) {
		for (SanPham product : stores) {
			product.hienThiThongTin();
			System.out.println("-----------");
		}
	}
	
	private static void PurchaseResult(Boolean result, int amout, SanPham product) {
		if (result) {
			System.out.println("Bán thành công " + amout + " "  + product.getTenSanPham());
		}
		else {
			System.out.println("Bán " + product.getTenSanPham() + " thất bại");
		}
	}

}
