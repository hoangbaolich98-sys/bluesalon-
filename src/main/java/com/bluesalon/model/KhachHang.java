package com.bluesalon.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "khach_hang")
public class KhachHang {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int khachHangId;

	@Column(nullable = false)
	private String hoTen;

	@Column(nullable = false)
	private String soDienThoai;

	@Column(nullable = false)
	private String gmail;

	// Getters va Setters
	public int getKhachHangId() {
		return khachHangId;
	}

	public void setKhachHangId(int khachHangId) {
		this.khachHangId = khachHangId;
	}

	public String getHoTen() {
		return hoTen;
	}

	public void setHoTen(String hoTen) {
		this.hoTen = hoTen;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}

	public String getGmail() {
		return gmail;
	}

	public void setGmail(String gmail) {
		this.gmail = gmail;
	}
}