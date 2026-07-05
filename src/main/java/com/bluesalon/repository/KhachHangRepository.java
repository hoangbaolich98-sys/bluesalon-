package com.bluesalon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluesalon.model.KhachHang;

public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {

	Optional<KhachHang> findByGmail(String gmail);
}