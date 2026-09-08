package quanLyTruongHoc;

import java.time.LocalDate;

public abstract class Nguoi {
	private String hoTen;
	private int namSinh;
	private String diaChi;
	
	public Nguoi(String hoTen, int namSinh, String diaChi) {
		super();
		this.hoTen = hoTen;
		this.namSinh = namSinh;
		this.diaChi = diaChi;
	}
	
	public String getHoTen() {
		return hoTen;
	}
	public void setHoTen(String hoTen) {
		this.hoTen = hoTen;
	}
	public int getNamSinh() {
		return namSinh;
	}
	public void setNamSinh(int namSinh) {
		this.namSinh = namSinh;
	}
	public String getDiaChi() {
		return diaChi;
	}
	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}
	
	public int tinhTuoi() {
		LocalDate currentDate = LocalDate.now();
		int tuoi = currentDate.getYear() - this.namSinh;
		return tuoi;
	}
	
	void hienThiThongTin() {
		System.out.println("Tên: " + getHoTen() + " Tuổi: " + tinhTuoi() + " Địa chỉ: " + getDiaChi());
	}
}
