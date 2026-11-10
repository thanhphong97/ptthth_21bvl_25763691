package bai2;

import bai1.SanPham;
import bai1.SanPhamRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Bai2TransactionChuyenKho {

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:nhanvien.db")) {
            SanPhamRepository repo = new SanPhamRepository(conn);
            repo.taoBang();
            repo.xoaTatCa();
            repo.them(new SanPham("SP01", "Bàn phím cơ", 850_000, 20));
            repo.them(new SanPham("SP02", "Chuột không dây", 320_000, 35));
            System.out.println("== Tồn kho ban đầu");
            SanPham.inDanhSach(repo.layTatCa());

            System.out.println("\n== Giao dịch 1: chuyển 5 cái từ SP01 sang SP02 (hợp lệ)");
            chuyenSoLuong(conn, "SP01", "SP02", 5);
            SanPham.inDanhSach(repo.layTatCa());

            System.out.println("\n== Giao dịch 2: chuyển 100 cái từ SP01 sang SP02 (vượt tồn kho)");
            chuyenSoLuong(conn, "SP01", "SP02", 100);
            SanPham.inDanhSach(repo.layTatCa());

            System.out.println("\n== Giao dịch 3: chuyển 3 cái từ SP01 sang SP99 (mã không tồn tại)");
            chuyenSoLuong(conn, "SP01", "SP99", 3);
            SanPham.inDanhSach(repo.layTatCa());
        } catch (SQLException e) {
            System.err.println("Lỗi CSDL: " + e.getMessage());
        }
    }

    public static boolean chuyenSoLuong(Connection conn, String tuMa, String denMa, int soLuong) throws SQLException {
        String sqlCong = "UPDATE SanPham SET soluong = soluong + ? WHERE maSP = ?";
        String sqlTru = "UPDATE SanPham SET soluong = soluong - ? WHERE maSP = ?";
        boolean autoCommitBanDau = conn.getAutoCommit();
        conn.setAutoCommit(false);
        try (PreparedStatement cong = conn.prepareStatement(sqlCong);
             PreparedStatement tru = conn.prepareStatement(sqlTru)) {
            cong.setInt(1, soLuong);
            cong.setString(2, denMa);
            if (cong.executeUpdate() == 0) {
                throw new SQLException("Không tìm thấy sản phẩm " + denMa);
            }
            System.out.println("  Bước 1: đã cộng " + soLuong + " vào " + denMa + " (chưa commit)");

            tru.setInt(1, soLuong);
            tru.setString(2, tuMa);
            if (tru.executeUpdate() == 0) {
                throw new SQLException("Không tìm thấy sản phẩm " + tuMa);
            }
            System.out.println("  Bước 2: đã trừ " + soLuong + " khỏi " + tuMa);

            conn.commit();
            System.out.println("  COMMIT: lưu cả 2 thay đổi");
            return true;
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("  Lỗi: " + e.getMessage());
            System.out.println("  ROLLBACK: hủy toàn bộ thay đổi của giao dịch");
            return false;
        } finally {
            conn.setAutoCommit(autoCommitBanDau);
        }
    }
}
