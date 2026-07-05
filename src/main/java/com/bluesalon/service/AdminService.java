package com.bluesalon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bluesalon.model.Admin;
import com.bluesalon.repository.AdminRepository;

@Service
public class AdminService {

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// F06 - Dang nhap admin
	public String dangNhap(String tenDangNhap, String matKhau) {

		// Buoc 1: Tim admin theo username
		Admin admin = adminRepository.findByTenDangNhap(tenDangNhap).orElse(null);

		// Buoc 2: Khong tim thay
		if (admin == null) {
			return "Loi: Tai khoan khong ton tai!";
		}

		// Buoc 3: Kiem tra mat khau bang BCrypt
		if (!passwordEncoder.matches(matKhau, admin.getMatKhau())) {
			return "Loi: Mat khau khong chinh xac!";
		}

		// Buoc 4: Dang nhap thanh cong
		return "Dang nhap thanh cong!";
	}
}