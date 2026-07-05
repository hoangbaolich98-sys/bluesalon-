package com.bluesalon.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Admin")
public class Admin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int adminId;

	@Column(nullable = false)
	private String tenDangNhap;

	@Column(nullable = false)
	private String matKhau;

	@Column(nullable = false, columnDefinition = "INT DEFAULT 0")
	private int soLanSaiMatKhau;

	private LocalDateTime thoiGianKhoa;
	private String maOTP;
	private LocalDateTime thoiGianHetHanOTP;

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public String getTenDangNhap() {
		return tenDangNhap;
	}

	public void setTenDangNhap(String tenDangNhap) {
		this.tenDangNhap = tenDangNhap;
	}

	public String getMatKhau() {
		return matKhau;
	}

	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}

	public int getSoLanSaiMatKhau() {
		return soLanSaiMatKhau;
	}

	public void setSoLanSaiMatKhau(int soLanSaiMatKhau) {
		this.soLanSaiMatKhau = soLanSaiMatKhau;
	}

	public LocalDateTime getThoiGianKhoa() {
		return thoiGianKhoa;
	}

	public void setThoiGianKhoa(LocalDateTime thoiGianKhoa) {
		this.thoiGianKhoa = thoiGianKhoa;
	}

	public String getMaOTP() {
		return maOTP;
	}

	public void setMaOTP(String maOTP) {
		this.maOTP = maOTP;
	}

	public LocalDateTime getThoiGianHetHanOTP() {
		return thoiGianHetHanOTP;
	}

	public void setThoiGianHetHanOTP(LocalDateTime thoiGianHetHanOTP) {
		this.thoiGianHetHanOTP = thoiGianHetHanOTP;
	}
}