package quanLySanPham;

import java.text.DecimalFormat;

public class SanPham {
	private String maSanPham;
	private String tenSanPham;
	private double donGia;
	private int soLuong;
	
	public SanPham() {
		// TODO Auto-generated constructor stub
	}

	public String getMaSanPham() {
		return maSanPham;
	}

	public void setMaSanPham(String maSanPham) {
		this.maSanPham = maSanPham;
	}

	public String getTenSanPham() {
		return tenSanPham;
	}

	public void setTenSanPham(String tenSanPham) {
		this.tenSanPham = tenSanPham;
	}

	public double getDonGia() {
		return donGia;
	}

	public void setDonGia(double donGia) {
		this.donGia = donGia;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}

	public SanPham(String maSanPham, String tenSanPham, double donGia, int soLuong) {
		this.maSanPham = maSanPham;
		this.tenSanPham = tenSanPham;
		this.donGia = donGia;
		this.soLuong = soLuong;
	}
	
	public double tinhThanhTien() {
		double thanhTien = donGia * soLuong;
		return thanhTien;
	}
	
	public void nhapHang(int soLuongNhap) {
		if (soLuongNhap <= 0) {
			System.out.println("Số lượng nhập phải lớn hơn 0");
		}

		this.soLuong += soLuongNhap;
	}
	
	public boolean banHang(int soLuongBan) {		
		if (soLuongBan <= 0) {
			System.out.println("Số lượng bán phải lớn 0");
			return false;
		}
		
		if (soLuongBan > this.soLuong) {
			System.out.println("Không được bán quá số lượng tồn kho");
			return false;
		}
		
		this.soLuong -= soLuongBan;
		
		return true;
	}

	public void hienThiThongTin() {
		DecimalFormat df = new DecimalFormat("#,###");
		System.out.println("Mã Sản Phẩm: " + getMaSanPham());
		System.out.println("Tên Sản Phẩm: " + getTenSanPham());
		String donGiaFormat = df.format(getDonGia());
		System.out.println("Đơn giá: " + donGiaFormat);
		String soLuongFormat = df.format(getSoLuong());
		System.out.println("Số lượng: " + soLuongFormat);
		String thanhTienFormat = df.format(tinhThanhTien());
		System.out.println("Thành tiền: " + thanhTienFormat);
	}

}
