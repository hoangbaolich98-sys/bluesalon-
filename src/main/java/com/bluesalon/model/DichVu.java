package com.bluesalon.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dich_vu")
public class DichVu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int dichVuId;

	@Column(nullable = false)
	private String tenDichVu;

	@Column(nullable = false)
	private int giaDichVu;

	@Column(nullable = false)
	private int thoiGian;

	public int getDichVuId() {
		return dichVuId;
	}

	public void setDichVuId(int dichVuId) {
		this.dichVuId = dichVuId;
	}

	public String getTenDichVu() {
		return tenDichVu;
	}

	public void setTenDichVu(String tenDichVu) {
		this.tenDichVu = tenDichVu;
	}

	public int getGiaDichVu() {
		return giaDichVu;
	}

	public void setGiaDichVu(int giaDichVu) {
		this.giaDichVu = giaDichVu;
	}

	public int getThoiGian() {
		return thoiGian;
	}

	public void setThoiGian(int thoiGian) {
		this.thoiGian = thoiGian;
	}
}