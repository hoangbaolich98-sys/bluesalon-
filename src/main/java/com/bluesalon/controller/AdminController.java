package com.bluesalon.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bluesalon.model.LichHen;
import com.bluesalon.service.AdminService;
import com.bluesalon.service.LichHenService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private LichHenService lichHenService;

	// F06 - Dang nhap admin
	@PostMapping("/dang-nhap")
	public String dangNhap(@RequestParam String tenDangNhap, @RequestParam String matKhau) {
		return adminService.dangNhap(tenDangNhap, matKhau);
	}

	// F08 - Xem lich theo ngay
	@GetMapping("/lich-theo-ngay")
	public List<LichHen> xemLichTheoNgay(@RequestParam String ngay) {
		LocalDateTime thoiGian = LocalDateTime.parse(ngay);
		return lichHenService.xemLichTheoNgay(thoiGian);
	}

	// F09 - Doi trang thai
	@PostMapping("/doi-trang-thai")
	public String doiTrangThai(@RequestParam int lichHenId, @RequestParam String trangThaiMoi) {
		return lichHenService.doiTrangThai(lichHenId, trangThaiMoi);
	}

	// F10 - Them lich thu cong
	@PostMapping("/them-lich")
	public String themLichThuCong(@RequestParam String hoTen, @RequestParam String soDienThoai,
			@RequestParam String gmail, @RequestParam int dichVuId, @RequestParam String thoiGianHen) {
		LocalDateTime thoiGian = LocalDateTime.parse(thoiGianHen);
		return lichHenService.themLichThuCong(hoTen, soDienThoai, gmail, dichVuId, thoiGian);
	}

	@PostMapping("/sua-gio")
	public String suaGio(@RequestParam int lichHenId, @RequestParam String thoiGianMoi) {
		LocalDateTime thoiGian = LocalDateTime.parse(thoiGianMoi);
		return lichHenService.suaGio(lichHenId, thoiGian);
	}

	@GetMapping("/tim-theo-ten")
	public List<LichHen> timTheoTen(@RequestParam String hoTen) {
		return lichHenService.timTheoTen(hoTen);
	}

}