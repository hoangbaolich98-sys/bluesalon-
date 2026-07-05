package com.bluesalon.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bluesalon.model.DichVu;
import com.bluesalon.model.LichHen;
import com.bluesalon.service.LichHenService;

@RestController
@RequestMapping("/api")
public class LichHenController {

	@Autowired
	private LichHenService lichHenService;

	// F01 - Lay danh sach dich vu
	@GetMapping("/dich-vu")
	public List<DichVu> layDanhSachDichVu() {
		return lichHenService.layDanhSachDichVu();
	}

	// F04 - Dat lich
	@PostMapping("/dat-lich")
	public String datLich(@RequestParam String hoTen, @RequestParam String soDienThoai, @RequestParam String gmail,
			@RequestParam int dichVuId, @RequestParam String thoiGianHen) {
		LocalDateTime thoiGian = LocalDateTime.parse(thoiGianHen);
		return lichHenService.datLich(hoTen, soDienThoai, gmail, dichVuId, thoiGian);
	}

	// F05 - Huy lich
	@PostMapping("/huy-lich")
	public String huyLich(@RequestParam String gmail, @RequestParam int lichHenId) {
		return lichHenService.huyLich(gmail, lichHenId);
	}

	// Tim lich hen theo Gmail (trang thai "Chua toi")
	@GetMapping("/lich-cua-toi")
	public List<LichHen> lichCuaToi(@RequestParam String gmail) {
		return lichHenService.timLichTheoGmail(gmail);
	}

	// F02 - Xem lich theo ngay (de hien thi gio trong)
	@GetMapping("/lich-theo-ngay")
	public List<LichHen> xemLichTheoNgay(@RequestParam String ngay) {
		LocalDateTime thoiGian = LocalDateTime.parse(ngay);
		return lichHenService.xemLichTheoNgay(thoiGian);
	}
}