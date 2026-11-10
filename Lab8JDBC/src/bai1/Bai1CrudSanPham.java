package bai1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Bai1CrudSanPham {

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:nhanvien.db")) {
            SanPhamRepository repo = new SanPhamRepository(conn);
            repo.taoBang();
            repo.xoaTatCa();

            System.out.println("== CREATE: thêm 3 sản phẩm");
            List<SanPham> dsMoi = List.of(
                    new SanPham("SP01", "Bàn phím cơ", 850_000, 20),
                    new SanPham("SP02", "Chuột không dây", 320_000, 35),
                    new SanPham("SP03", "Màn hình 24 inch", 3_200_000, 8));
            for (SanPham sp : dsMoi) {
                System.out.println("  Thêm " + sp.maSP() + ": " + repo.them(sp) + " dòng");
            }

            System.out.println("\n== READ: tất cả sản phẩm");
            SanPham.inDanhSach(repo.layTatCa());

            System.out.println("\n== READ: tìm sản phẩm có tên chứa \"chuột\"");
            SanPham.inDanhSach(repo.timTheoTen("chuột"));

            System.out.println("\n== UPDATE: SP01 giảm giá còn 790.000, SP02 nhập thêm hàng còn 50");
            baoKetQua("Cập nhật giá SP01", repo.capNhatGia("SP01", 790_000));
            baoKetQua("Cập nhật số lượng SP02", repo.capNhatSoLuong("SP02", 50));
            baoKetQua("Cập nhật giá SP99", repo.capNhatGia("SP99", 1_000));
            SanPham.inDanhSach(repo.layTatCa());

            System.out.println("\n== DELETE: xóa SP03");
            baoKetQua("Xóa SP03", repo.xoa("SP03"));
            SanPham.inDanhSach(repo.layTatCa());
        } catch (SQLException e) {
            System.err.println("Lỗi CSDL: " + e.getMessage());
        }
    }

    private static void baoKetQua(String thaoTac, int soDong) {
        System.out.println("  " + thaoTac + ": " + (soDong > 0 ? "thành công" : "không tìm thấy mã sản phẩm"));
    }
}
