package bai5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Bai5ThongKeLuong {

    private static final Object[][] DS_NHAN_VIEN = {
            {"Nguyễn Văn An", "Trưởng phòng", 25_000_000},
            {"Trần Thị Bình", "Kế toán", 14_000_000},
            {"Lê Văn Cường", "Nhân viên", 9_500_000},
            {"Phạm Thị Dung", "Nhân viên", 10_000_000},
            {"Hoàng Văn Em", "Nhân viên", 8_500_000},
            {"Võ Thị Phương", "Kế toán", 15_500_000},
            {"Đặng Văn Giang", "Bảo vệ", 7_000_000},
            {"Bùi Thị Hoa", "Nhân viên", 9_000_000}
    };

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:nhanvien.db")) {
            chuanBiBang(conn);
            themDuLieuMau(conn);

            System.out.printf("Tổng lương          : %,.0f%n", layMotGiaTri(conn, "SELECT SUM(luong) FROM NhanVien"));
            System.out.printf("Lương trung bình    : %,.0f%n", layMotGiaTri(conn, "SELECT AVG(luong) FROM NhanVien"));

            System.out.println("\nNhân viên lương cao nhất:");
            inNhanVienTheoMucLuong(conn, "MAX");
            System.out.println("Nhân viên lương thấp nhất:");
            inNhanVienTheoMucLuong(conn, "MIN");

            System.out.println("\nSố nhân viên theo chức vụ:");
            demTheoChucVu(conn);
        } catch (SQLException e) {
            System.err.println("Lỗi CSDL: " + e.getMessage());
        }
    }

    private static void chuanBiBang(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS NhanVien ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, ten TEXT NOT NULL, chuc_vu TEXT, luong REAL)");
            if (!coCot(conn, "NhanVien", "luong")) {
                st.executeUpdate("ALTER TABLE NhanVien ADD COLUMN luong REAL");
                System.out.println("Đã thêm cột luong vào bảng NhanVien");
            }
        }
    }

    private static boolean coCot(Connection conn, String bang, String cot) throws SQLException {
        try (ResultSet rs = conn.getMetaData().getColumns(null, null, bang, cot)) {
            return rs.next();
        }
    }

    private static void themDuLieuMau(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM NhanVien");
        }
        String sql = "INSERT INTO NhanVien (ten, chuc_vu, luong) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Object[] nv : DS_NHAN_VIEN) {
                ps.setString(1, (String) nv[0]);
                ps.setString(2, (String) nv[1]);
                ps.setDouble(3, (Integer) nv[2]);
                ps.addBatch();
            }
            System.out.println("Đã thêm " + ps.executeBatch().length + " nhân viên\n");
        }
    }

    private static double layMotGiaTri(Connection conn, String sql) throws SQLException {
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getDouble(1) : 0;
        }
    }

    private static void inNhanVienTheoMucLuong(Connection conn, String hamTongHop) throws SQLException {
        String sql = "SELECT ten, chuc_vu, luong FROM NhanVien "
                + "WHERE luong = (SELECT " + hamTongHop + "(luong) FROM NhanVien)";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("  %-16s | %-13s | %,12.0f%n",
                        rs.getString("ten"), rs.getString("chuc_vu"), rs.getDouble("luong"));
            }
        }
    }

    private static void demTheoChucVu(Connection conn) throws SQLException {
        String sql = "SELECT chuc_vu, COUNT(*) AS so_luong FROM NhanVien "
                + "GROUP BY chuc_vu ORDER BY so_luong DESC, chuc_vu";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("  %-13s : %d%n", rs.getString("chuc_vu"), rs.getInt("so_luong"));
            }
        }
    }
}
