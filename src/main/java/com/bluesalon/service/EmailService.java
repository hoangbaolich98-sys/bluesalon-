package com.bluesalon.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	private static final String SDT_SALON = "070-9123-4567";

	public void sendMailDatLich(String gmail, String hoTen, String tenDichVu, int thoiGianPhut,
			LocalDateTime thoiGianHen) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(gmail);
		message.setFrom("gmailbluesalon@gmail.com");
		message.setSubject("Xac nhan dat lich thanh cong");
		message.setText("Xin chao " + hoTen + "\n" + "Ban da dat lich thanh cong: " + tenDichVu + "\n"
				+ "Thoi gian hen: " + thoiGianHen.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n"
				+ "Thoi gian du kien: " + thoiGianPhut + " phut\n" + "Moi thac mac vui long lien he salon qua so: "
				+ SDT_SALON + "\n" + "Cam on ban da su dung dich vu cua chung toi!");
		mailSender.send(message);
	}

	public void sendMailHuyLich(String gmail, String hoTen, String tenDichVu, LocalDateTime thoiGianHen) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(gmail);
		message.setFrom("gmailbluesalon@gmail.com");
		message.setSubject("Ban da huy lich thanh cong");
		message.setText("Xin chao " + hoTen + "\n" + "Ban da huy lich thanh cong: " + tenDichVu + "\n"
				+ "Thoi gian hen: " + thoiGianHen.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n"
				+ "Neu muon dat lai lich moi, vui long truy cap lai trang dat lich hoac lien he salon qua so: "
				+ SDT_SALON + "\n" + "Cam on ban da su dung dich vu cua chung toi!");
		mailSender.send(message);
	}

	public void sendMailSuaGio(String gmail, String hoTen, String tenDichVu, LocalDateTime thoiGianHenCu,
			LocalDateTime thoiGianHenMoi) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(gmail);
		message.setFrom("gmailbluesalon@gmail.com");
		message.setSubject("Ban da doi lich thanh cong");
		message.setText("Xin chao " + hoTen + "\n" + "Ban da doi lich thanh cong: " + tenDichVu + "\n"
				+ "Thoi gian hen cu: " + thoiGianHenCu.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n"
				+ "Thoi gian hen moi: " + thoiGianHenMoi.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n"
				+ "Moi thac mac vui long lien he salon qua so: " + SDT_SALON + "\n"
				+ "Cam on ban da su dung dich vu cua chung toi!");
		mailSender.send(message);
	}

}