package bai4;

import bai1.SanPham;
import bai1.SanPhamRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Bai4PreparedStatementBaoMatHieuNang {
    private static final int SO_DONG_THU = 5_000;

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:nhanvien.db")) {
            SanPhamRepository repo = new SanPhamRepository(conn);
            repo.taoBang();
            repo.xoaTatCa();
            repo.them(new SanPham("SP01", "Bàn phím cơ", 850_000, 20));
            repo.them(new SanPham("SP02", "Chuột không dây", 320_000, 35));
            repo.them(new SanPham("SP03", "Màn hình 24 inch", 3_200_000, 8));

            String dauVaoDocHai = "x' OR '1'='1";
            System.out.println("== BẢO MẬT: tìm sản phẩm với tên nhập vào = " + dauVaoDocHai);

            System.out.println("\n-- Statement + nối chuỗi (bị SQL Injection)");
            SanPham.inDanhSach(timBangStatement(conn, dauVaoDocHai));

            System.out.println("\n-- PreparedStatement + tham số ?");
            SanPham.inDanhSach(timBangPreparedStatement(conn, dauVaoDocHai));

            System.out.println("\n== HIỆU NĂNG: thêm " + SO_DONG_THU + " sản phẩm");
            System.out.println("  Statement (biên dịch lại SQL mỗi lần) : "
                    + doThoiGian(conn, () -> themBangStatement(conn)) + " ms");
            System.out.println("  PreparedStatement + batch              : "
                    + doThoiGian(conn, () -> themBangPreparedBatch(conn)) + " ms");
        } catch (SQLException e) {
            System.err.println("Lỗi CSDL: " + e.getMessage());
        }
    }

    private static List<SanPham> timBangStatement(Connection conn, String ten) throws SQLException {
        String sql = "SELECT * FROM SanPham WHERE tenSP = '" + ten + "'";
        System.out.println("  SQL thực thi: " + sql);
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            return docDanhSach(rs);
        }
    }

    private static List<SanPham> timBangPreparedStatement(Connection conn, String ten) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM SanPham WHERE tenSP = ?")) {
            ps.setString(1, ten);
            try (ResultSet rs = ps.executeQuery()) {
                return docDanhSach(rs);
            }
        }
    }

    private static List<SanPham> docDanhSach(ResultSet rs) throws SQLException {
        List<SanPham> ds = new ArrayList<>();
        while (rs.next()) {
            ds.add(SanPhamRepository.docSanPham(rs));
        }
        return ds;
    }

    private static void themBangStatement(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            for (int i = 1; i <= SO_DONG_THU; i++) {
                st.executeUpdate("INSERT INTO SanPham VALUES ('T" + i + "', 'Hàng thử " + i + "', " + i + ", " + i + ")");
            }
        }
    }

    private static void themBangPreparedBatch(Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO SanPham VALUES (?, ?, ?, ?)")) {
            for (int i = 1; i <= SO_DONG_THU; i++) {
                ps.setString(1, "T" + i);
                ps.setString(2, "Hàng thử " + i);
                ps.setDouble(3, i);
                ps.setInt(4, i);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static long doThoiGian(Connection conn, ThaoTacSql thaoTac) throws SQLException {
        conn.setAutoCommit(false);
        try {
            long batDau = System.nanoTime();
            thaoTac.chay();
            long thoiGian = (System.nanoTime() - batDau) / 1_000_000;
            conn.rollback();
            return thoiGian;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    @FunctionalInterface
    private interface ThaoTacSql {
        void chay() throws SQLException;
    }
}
