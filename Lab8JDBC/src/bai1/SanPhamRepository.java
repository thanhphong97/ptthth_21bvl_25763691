package bai1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SanPhamRepository {
    public static final String SQL_TAO_BANG = """
            CREATE TABLE IF NOT EXISTS SanPham (
                maSP    TEXT PRIMARY KEY,
                tenSP   TEXT NOT NULL,
                gia     REAL NOT NULL CHECK (gia >= 0),
                soluong INTEGER NOT NULL CHECK (soluong >= 0)
            )""";

    private final Connection connection;

    public SanPhamRepository(Connection connection) {
        this.connection = connection;
    }

    public void taoBang() throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.executeUpdate(SQL_TAO_BANG);
        }
    }

    public void xoaTatCa() throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.executeUpdate("DELETE FROM SanPham");
        }
    }

    public int them(SanPham sp) throws SQLException {
        String sql = "INSERT INTO SanPham (maSP, tenSP, gia, soluong) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, sp.maSP());
            ps.setString(2, sp.tenSP());
            ps.setDouble(3, sp.gia());
            ps.setInt(4, sp.soLuong());
            return ps.executeUpdate();
        }
    }

    public List<SanPham> layTatCa() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM SanPham ORDER BY maSP")) {
            return docDanhSach(ps);
        }
    }

    public List<SanPham> timTheoTen(String tuKhoa) throws SQLException {
        String sql = "SELECT * FROM SanPham WHERE tenSP LIKE ? ORDER BY maSP";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + tuKhoa + "%");
            return docDanhSach(ps);
        }
    }

    public int capNhatGia(String maSP, double giaMoi) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE SanPham SET gia = ? WHERE maSP = ?")) {
            ps.setDouble(1, giaMoi);
            ps.setString(2, maSP);
            return ps.executeUpdate();
        }
    }

    public int capNhatSoLuong(String maSP, int soLuongMoi) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE SanPham SET soluong = ? WHERE maSP = ?")) {
            ps.setInt(1, soLuongMoi);
            ps.setString(2, maSP);
            return ps.executeUpdate();
        }
    }

    public int xoa(String maSP) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM SanPham WHERE maSP = ?")) {
            ps.setString(1, maSP);
            return ps.executeUpdate();
        }
    }

    public static SanPham docSanPham(ResultSet rs) throws SQLException {
        return new SanPham(rs.getString("maSP"), rs.getString("tenSP"), rs.getDouble("gia"), rs.getInt("soluong"));
    }

    private List<SanPham> docDanhSach(PreparedStatement ps) throws SQLException {
        List<SanPham> ds = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ds.add(docSanPham(rs));
            }
        }
        return ds;
    }
}
