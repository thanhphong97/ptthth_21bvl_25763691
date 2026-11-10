package bai3;

import bai1.SanPham;

public class Bai3XuLyNgoaiLe {

    public static void main(String[] args) {
        SanPhamRepositoryFinally repo = new SanPhamRepositoryFinally();
        repo.taoBang();
        repo.xoaTatCa();

        System.out.println("== CREATE: thêm 3 sản phẩm");
        bao("Thêm SP01", repo.them(new SanPham("SP01", "Bàn phím cơ", 850_000, 20)));
        bao("Thêm SP02", repo.them(new SanPham("SP02", "Chuột không dây", 320_000, 35)));
        bao("Thêm SP03", repo.them(new SanPham("SP03", "Màn hình 24 inch", 3_200_000, 8)));

        System.out.println("\n== READ: tất cả sản phẩm");
        SanPham.inDanhSach(repo.layTatCa());

        System.out.println("\n== READ: tìm sản phẩm có tên chứa \"phím\"");
        SanPham.inDanhSach(repo.timTheoTen("phím"));

        System.out.println("\n== UPDATE + DELETE");
        bao("Cập nhật giá SP01", repo.capNhatGia("SP01", 790_000));
        bao("Cập nhật số lượng SP02", repo.capNhatSoLuong("SP02", 50));
        bao("Xóa SP03", repo.xoa("SP03"));
        SanPham.inDanhSach(repo.layTatCa());

        System.out.println("\n== Cố ý gây lỗi SQL, chương trình vẫn chạy tiếp");
        bao("Thêm trùng mã SP01", repo.them(new SanPham("SP01", "Trùng mã", 1_000, 1)));
        bao("Thêm sản phẩm không có tên", repo.them(new SanPham("SP04", null, 1_000, 1)));
        bao("Số lượng âm cho SP02", repo.capNhatSoLuong("SP02", -5));
        bao("Giá âm cho SP01", repo.capNhatGia("SP01", -100));

        System.out.println("\n== Dữ liệu sau các lỗi (không bị thay đổi)");
        SanPham.inDanhSach(repo.layTatCa());
    }

    private static void bao(String thaoTac, int ketQua) {
        String trangThai = ketQua > 0 ? "thành công"
                : ketQua == 0 ? "không tìm thấy mã sản phẩm"
                : "thất bại, đã bắt lỗi và đóng tài nguyên";
        System.out.println("  " + thaoTac + ": " + trangThai);
    }
}
