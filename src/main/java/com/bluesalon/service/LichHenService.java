package com.bluesalon.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluesalon.model.DichVu;
import com.bluesalon.model.KhachHang;
import com.bluesalon.model.LichHen;
import com.bluesalon.repository.DichVuRepository;
import com.bluesalon.repository.KhachHangRepository;
import com.bluesalon.repository.LichHenRepository;

@Service
public class LichHenService {

	@Autowired
	private LichHenRepository lichHenRepository;

	@Autowired
	private KhachHangRepository khachHangRepository;

	@Autowired
	private DichVuRepository dichVuRepository;

	@Autowired
	private EmailService emailService;

	private static final ZoneId JST = ZoneId.of("Asia/Tokyo");

	public List<DichVu> layDanhSachDichVu() {
		return dichVuRepository.findAll();
	}

	@Transactional
	public String datLich(String hoTen, String soDienThoai, String gmail, int dichVuId, LocalDateTime thoiGianHen) {

// Kiem tra du lieu trong
		if (hoTen == null || hoTen.isEmpty()) {
			return "Loi: Ho ten khong duoc de trong!";
		}
		if (soDienThoai == null || soDienThoai.isEmpty()) {
			return "Loi: So dien thoai khong duoc de trong!";
		}
		if (gmail == null || gmail.isEmpty()) {
			return "Loi: Gmail khong duoc de trong!";
		}

// Kiem tra ngay dat lich - dung ZonedDateTime de nhat quan voi JST
		ZonedDateTime homNayJST = ZonedDateTime.now(JST);
		ZonedDateTime toiDaJST = homNayJST.plusDays(30);

		// thoiGianHen tu frontend la local time JST, gan timezone JST de so sanh chinh
		// xac
		ZonedDateTime thoiGianHenJST = thoiGianHen.atZone(JST);

		if (thoiGianHenJST.isBefore(homNayJST)) {
			return "Loi: Khong the dat lich trong qua khu!";
		}
		if (thoiGianHenJST.isAfter(toiDaJST)) {
			return "Loi: Chi duoc dat lich trong vong 30 ngay!";
		}
		DayOfWeek dow = thoiGianHen.getDayOfWeek();
		if (dow == DayOfWeek.MONDAY || dow == DayOfWeek.WEDNESDAY) {
			return "Không thể đặt lịch thứ 2 và thứ 4 ! ";
		}
		// Lay dich vu TRUOC de biet thoi luong
		DichVu dichVu = dichVuRepository.findById(dichVuId).orElse(null);
		if (dichVu == null) {
			return "Loi: Dich vu khong ton tai!";
		}
		LocalDateTime ketThucMoi = thoiGianHen.plusMinutes(dichVu.getThoiGian());
		// Lay danh sach lich "Chua toi" trong ngay do
		LocalDateTime batDauNgay = thoiGianHen.toLocalDate().atStartOfDay();
		LocalDateTime ketThucNgay = thoiGianHen.toLocalDate().atTime(23, 59, 59);
		List<LichHen> lichTrongNgay = lichHenRepository.findByThoiGianHenBetweenAndTrangThaiLich(batDauNgay,
				ketThucNgay, "Chua toi");
		// Kiem tra chong lan thoi gian (thay cho existsByThoiGianHenAndTrangThaiLich
		// cu)
		for (LichHen lich : lichTrongNgay) {
			LocalDateTime batDauCu = lich.getThoiGianHen();
			LocalDateTime ketThucCu = lich.getThoiGianHen().plusMinutes(lich.getDichVu().getThoiGian());
			if (batDauCu.isBefore(ketThucMoi) && thoiGianHen.isBefore(ketThucCu)) {
				return "Loi: Gio nay trung voi lich khac, vui long chon gio khac!";
			}
		}
// Luu khach hang
		KhachHang khachHang = new KhachHang();
		khachHang.setHoTen(hoTen);
		khachHang.setSoDienThoai(soDienThoai);
		khachHang.setGmail(gmail);
		khachHangRepository.save(khachHang);

// Luu lich hen
		LichHen lichHen = new LichHen();
		lichHen.setKhachHang(khachHang);
		lichHen.setDichVu(dichVu);
		lichHen.setThoiGianHen(thoiGianHen);
		lichHen.setTrangThaiLich("Chua toi");
		lichHen.setNguonDatLich("ONLINE");
		lichHenRepository.save(lichHen);

		emailService.sendMailDatLich(gmail, hoTen, dichVu.getTenDichVu(), dichVu.getThoiGian(), thoiGianHen);
		return "Dat lich thanh cong!";
	}

	@Transactional
	public String themLichThuCong(String hoTen, String soDienThoai, String gmail, int dichVuId,
			LocalDateTime thoiGianHen) {

		if (hoTen == null || hoTen.isEmpty()) {
			return "Loi: Ho ten khong duoc de trong!";
		}
		if (soDienThoai == null || soDienThoai.isEmpty()) {
			return "Loi: So dien thoai khong duoc de trong!";
		}
		if (gmail == null || gmail.isEmpty()) {
			return "Loi: Gmail khong duoc de trong!";
		}
		DayOfWeek dow = thoiGianHen.getDayOfWeek();

		if (dow == DayOfWeek.MONDAY || dow == DayOfWeek.WEDNESDAY) {

			return "Không thể đặt lịch thứ 2 và thứ 4 ! ";

		}

		ZonedDateTime homNayJST = ZonedDateTime.now(JST);
		ZonedDateTime toiDaJST = homNayJST.plusDays(30);
		ZonedDateTime thoiGianHenJST = thoiGianHen.atZone(JST);

		if (thoiGianHenJST.isBefore(homNayJST)) {
			return "Loi: Khong the dat lich trong qua khu!";
		}
		if (thoiGianHenJST.isAfter(toiDaJST)) {
			return "Loi: Chi duoc dat lich trong vong 30 ngay!";
		}

		DichVu dichVu = dichVuRepository.findById(dichVuId).orElse(null);
		if (dichVu == null) {
			return "Loi: Dich vu khong ton tai!";
		}
		LocalDateTime ketThucMoi = thoiGianHen.plusMinutes(dichVu.getThoiGian());
		// Lay danh sach lich "Chua toi" trong ngay do
		LocalDateTime batDauNgay = thoiGianHen.toLocalDate().atStartOfDay();
		LocalDateTime ketThucNgay = thoiGianHen.toLocalDate().atTime(23, 59, 59);
		List<LichHen> lichTrongNgay = lichHenRepository.findByThoiGianHenBetweenAndTrangThaiLich(batDauNgay,
				ketThucNgay, "Chua toi");
		// Kiem tra chong lan thoi gian (thay cho existsByThoiGianHenAndTrangThaiLich
		// cu)
		for (LichHen lich : lichTrongNgay) {
			LocalDateTime batDauCu = lich.getThoiGianHen();
			LocalDateTime ketThucCu = lich.getThoiGianHen().plusMinutes(lich.getDichVu().getThoiGian());
			if (batDauCu.isBefore(ketThucMoi) && thoiGianHen.isBefore(ketThucCu)) {
				return "Loi: Gio nay trung voi lich khac, vui long chon gio khac!";
			}
		}

		KhachHang khachHang = new KhachHang();
		khachHang.setHoTen(hoTen);
		khachHang.setSoDienThoai(soDienThoai);
		khachHang.setGmail(gmail);
		khachHangRepository.save(khachHang);

		LichHen lichHen = new LichHen();
		lichHen.setKhachHang(khachHang);
		lichHen.setDichVu(dichVu);
		lichHen.setThoiGianHen(thoiGianHen);
		lichHen.setTrangThaiLich("Chua toi");
		lichHen.setNguonDatLich("ADMIN");
		lichHenRepository.save(lichHen);
		return "Dat lich thanh cong!";
	}

	public String huyLich(String gmail, int lichHenId) {

		List<LichHen> danhSachLich = lichHenRepository.findByKhachHang_GmailAndTrangThaiLich(gmail, "Chua toi");

		if (danhSachLich.isEmpty()) {
			return "Loi: Khong tim thay lich hen!";
		}

		LichHen lichCan = null;
		for (LichHen lich : danhSachLich) {
			if (lich.getLichHenId() == lichHenId) {
				lichCan = lich;
				break;
			}
		}

		if (lichCan == null) {
			return "Loi: Khong tim thay lich hen nay!";
		}

		lichCan.setTrangThaiLich("Da huy");
		lichHenRepository.save(lichCan);
		emailService.sendMailHuyLich(gmail, lichCan.getKhachHang().getHoTen(), lichCan.getDichVu().getTenDichVu(),
				lichCan.getThoiGianHen());

		return "Huy lich thanh cong!";
	}

	// Tim lich "Chua toi" theo Gmail
	public List<LichHen> timLichTheoGmail(String gmail) {
		return lichHenRepository.findByKhachHang_GmailAndTrangThaiLich(gmail, "Chua toi");
	}

	public List<LichHen> xemLichTheoNgay(LocalDateTime ngay) {
		LocalDateTime batDau = ngay.toLocalDate().atStartOfDay();
		LocalDateTime ketThuc = ngay.toLocalDate().atTime(23, 59, 59);
		// Bỏ filter trangThaiLich - trả về tất cả
		return lichHenRepository.findByThoiGianHenBetween(batDau, ketThuc);
	}

	public List<LichHen> xemGioTrongTheoNgay(LocalDateTime ngay) {
		LocalDateTime batDau = ngay.toLocalDate().atStartOfDay();
		LocalDateTime ketThuc = ngay.toLocalDate().atTime(23, 59, 59);
		// Chi lay lich "Chua toi" - dung cho trang khach hang kiem tra gio trong
		return lichHenRepository.findByThoiGianHenBetweenAndTrangThaiLich(batDau, ketThuc, "Chua toi");
	}

	@Transactional
	public String suaGio(int lichHenId, LocalDateTime thoiGianMoi) {
		LichHen lichHen = lichHenRepository.findById(lichHenId).orElse(null);
		if (lichHen == null) {
			return "Loi: Khong tim thay lich hen!";
		}
		// Buoc 2: kiem tra thu 2, thu 4
		DayOfWeek dow = thoiGianMoi.getDayOfWeek();
		if (dow == DayOfWeek.MONDAY || dow == DayOfWeek.WEDNESDAY) {
			return "Khong the dat lich thu 2 va thu 4!";
		}
		// Kiem tra ngay qua khu va gioi han 30 ngay
		ZonedDateTime homNayJST = ZonedDateTime.now(JST);
		ZonedDateTime toiDaJST = homNayJST.plusDays(30);
		ZonedDateTime thoiGianMoiJST = thoiGianMoi.atZone(JST);

		if (thoiGianMoiJST.isBefore(homNayJST)) {
			return "Loi: Khong the sua lich ve qua khu!";
		}
		if (thoiGianMoiJST.isAfter(toiDaJST)) {
			return "Loi: Chi duoc sua lich trong vong 30 ngay!";
		}
		LocalDateTime ketThucMoi = thoiGianMoi.plusMinutes(lichHen.getDichVu().getThoiGian());
		// Lay danh sach lich "Chua toi" trong ngay do
		LocalDateTime batDauNgay = thoiGianMoi.toLocalDate().atStartOfDay();
		LocalDateTime ketThucNgay = thoiGianMoi.toLocalDate().atTime(23, 59, 59);
		List<LichHen> lichTrongNgay = lichHenRepository.findByThoiGianHenBetweenAndTrangThaiLich(batDauNgay,
				ketThucNgay, "Chua toi");
		for (LichHen lich : lichTrongNgay) {
			if (lich.getLichHenId() == lichHenId) {
				continue;
			}
			LocalDateTime batDauCu = lich.getThoiGianHen();
			LocalDateTime ketThucCu = lich.getThoiGianHen().plusMinutes(lich.getDichVu().getThoiGian());
			if (batDauCu.isBefore(ketThucMoi) && thoiGianMoi.isBefore(ketThucCu)) {
				return "Loi: Gio nay trung voi lich khac, vui long chon gio khac!";
			}
		}
		// Buoc 4: luu gio cu truoc khi cap nhat
		LocalDateTime thoiGianCu = lichHen.getThoiGianHen();

		// Cap nhat gio moi
		lichHen.setThoiGianHen(thoiGianMoi);
		lichHenRepository.save(lichHen);

		// Buoc 5: gui email thong bao
		emailService.sendMailSuaGio(lichHen.getKhachHang().getGmail(), lichHen.getKhachHang().getHoTen(),
				lichHen.getDichVu().getTenDichVu(), thoiGianCu, thoiGianMoi);

		return "Sua gio thanh cong!";
	}

	public String doiTrangThai(int lichHenId, String trangThaiMoi) {

		LichHen lichHen = lichHenRepository.findById(lichHenId).orElse(null);

		if (lichHen == null) {
			return "Loi: Khong tim thay lich hen!";
		}

		lichHen.setTrangThaiLich(trangThaiMoi);
		lichHenRepository.save(lichHen);

		return "Doi trang thai thanh cong!";
	}

	public List<LichHen> timTheoTen(String hoTen) {
		return lichHenRepository.findByKhachHang_HoTenContainingIgnoreCase(hoTen);
	}

}