package com.bluesalon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluesalon.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

	Optional<Admin> findByTenDangNhap(String tenDangNhap);
}