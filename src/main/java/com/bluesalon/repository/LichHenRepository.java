package com.bluesalon.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluesalon.model.LichHen;

public interface LichHenRepository extends JpaRepository<LichHen, Integer> {

	List<LichHen> findByKhachHang_GmailAndTrangThaiLich(String gmail, String trangThaiLich);

	List<LichHen> findByThoiGianHenBetweenAndTrangThaiLich(LocalDateTime start, LocalDateTime end,
			String trangThaiLich);

	boolean existsByThoiGianHenAndTrangThaiLich(LocalDateTime thoiGianHen, String trangThaiLich);

	List<LichHen> findByThoiGianHenBetween(LocalDateTime start, LocalDateTime end);

	List<LichHen> findByKhachHang_HoTenContainingIgnoreCase(String hoTen);
}