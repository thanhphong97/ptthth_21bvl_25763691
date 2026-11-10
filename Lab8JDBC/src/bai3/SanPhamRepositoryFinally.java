package bai3;

import bai1.SanPham;
import bai1.SanPhamRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SanPhamRepositoryFinally {

    public int taoBang() {
        return thucThi(SanPhamRepository.SQL_TAO_BANG);
    }

    public int xoaTatCa() {
        return thucThi("DELETE FROM SanPham");
    }

    public int them(SanPham sp) {
        return thucThi("INSERT INTO SanPham (maSP, tenSP, gia, soluong) VALUES (?, ?, ?, ?)",
                sp.maSP(), sp.tenSP(), sp.gia(), sp.soLuong());
    }

    public List<SanPham> layTatCa() {
        return truyVan("SELECT * FROM SanPham ORDER BY maSP");
    }

    public List<SanPham> timTheoTen(String tuKhoa) {
        return truyVan("SELECT * FROM SanPham WHERE tenSP LIKE ? ORDER BY maSP", "%" + tuKhoa + "%");
    }

    public int capNhatGia(String maSP, double giaMoi) {
        return thucThi("UPDATE SanPham SET gia = ? WHERE maSP = ?", giaMoi, maSP);
    }

    public int capNhatSoLuong(String maSP, int soLuongMoi) {
        return thucThi("UPDATE SanPham SET soluong = ? WHERE maSP = ?", soLuongMoi, maSP);
    }

    public int xoa(String maSP) {
        return thucThi("DELETE FROM SanPham WHERE maSP = ?", maSP);
    }

    private int thucThi(String sql, Object... thamSo) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:nhanvien.db");
            ps = conn.prepareStatement(sql);
            ganThamSo(ps, thamSo);
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("  [SQLException] " + e.getMessage());
            return -1;
        } finally {
            dong(ps);
            dong(conn);
        }
    }

    private List<SanPham> truyVan(String sql, Object... thamSo) {
        List<SanPham> ds = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:nhanvien.db");
            ps = conn.prepareStatement(sql);
            ganThamSo(ps, thamSo);
            rs = ps.executeQuery();
            while (rs.next()) {
                ds.add(SanPhamRepository.docSanPham(rs));
            }
        } catch (SQLException e) {
            System.err.println("  [SQLException] " + e.getMessage());
        } finally {
            dong(rs);
            dong(ps);
            dong(conn);
        }
        return ds;
    }

    private static void ganThamSo(PreparedStatement ps, Object... thamSo) throws SQLException {
        for (int i = 0; i < thamSo.length; i++) {
            ps.setObject(i + 1, thamSo[i]);
        }
    }

    private static void dong(AutoCloseable taiNguyen) {
        if (taiNguyen == null) {
            return;
        }
        try {
            taiNguyen.close();
        } catch (Exception e) {
            System.err.println("  Không đóng được tài nguyên: " + e.getMessage());
        }
    }
}
