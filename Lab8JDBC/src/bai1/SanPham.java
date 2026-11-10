package bai1;

import java.util.List;

public record SanPham(String maSP, String tenSP, double gia, int soLuong) {

    public static void inDanhSach(List<SanPham> ds) {
        if (ds.isEmpty()) {
            System.out.println("  (không có sản phẩm)");
            return;
        }
        System.out.printf("  %-5s | %-20s | %12s | %8s%n", "maSP", "tenSP", "gia", "soluong");
        for (SanPham sp : ds) {
            System.out.printf("  %-5s | %-20s | %,12.0f | %8d%n", sp.maSP, sp.tenSP, sp.gia, sp.soLuong);
        }
    }
}
