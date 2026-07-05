package com.bluesalon.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "lich_hen")
public class LichHen {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int lichHenId;

	@ManyToOne
	@JoinColumn(name = "KhachHangId")
	private KhachHang khachHang;

	@ManyToOne
	@JoinColumn(name = "DichVuId")
	private DichVu dichVu;

	@Column(nullable = false)
	private LocalDateTime thoiGianHen;

	@Column(nullable = false)
	private String trangThaiLich;

	@Column(nullable = false)
	private String nguonDatLich;

	public int getLichHenId() {
		return lichHenId;
	}

	public void setLichHenId(int lichHenId) {
		this.lichHenId = lichHenId;
	}

	public KhachHang getKhachHang() {
		return khachHang;
	}

	public void setKhachHang(KhachHang khachHang) {
		this.khachHang = khachHang;
	}

	public DichVu getDichVu() {
		return dichVu;
	}

	public void setDichVu(DichVu dichVu) {
		this.dichVu = dichVu;
	}

	public LocalDateTime getThoiGianHen() {
		return thoiGianHen;
	}

	public void setThoiGianHen(LocalDateTime thoiGianHen) {
		this.thoiGianHen = thoiGianHen;
	}

	public String getTrangThaiLich() {
		return trangThaiLich;
	}

	public void setTrangThaiLich(String trangThaiLich) {
		this.trangThaiLich = trangThaiLich;
	}

	public String getNguonDatLich() {
		return nguonDatLich;
	}

	public void setNguonDatLich(String nguonDatLich) {
		this.nguonDatLich = nguonDatLich;
	}
}