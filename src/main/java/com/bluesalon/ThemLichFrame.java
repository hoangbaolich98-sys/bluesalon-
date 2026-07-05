package com.bluesalon;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

public class ThemLichFrame extends JFrame {

	private JTextField txtHoTen, txtSoDienThoai, txtGmail;
	private JComboBox<String> cbDichVu, cbGio;
	private JSpinner spNgay;
	private JButton btnLuu, btnHuy;
	private MainFrame mainFrame;

	private static final String API = "http://localhost:8081";

	public ThemLichFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setTitle("Them lich thu cong");
		setSize(400, 350);
		setLocationRelativeTo(mainFrame);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout(10, 10));

		// === Form nhap lieu ===
		JPanel panelForm = new JPanel(new GridLayout(6, 2, 10, 10));
		panelForm.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

		panelForm.add(new JLabel("Ho ten:"));
		txtHoTen = new JTextField();
		panelForm.add(txtHoTen);

		panelForm.add(new JLabel("So dien thoai:"));
		txtSoDienThoai = new JTextField();
		panelForm.add(txtSoDienThoai);

		panelForm.add(new JLabel("Gmail:"));
		txtGmail = new JTextField();
		panelForm.add(txtGmail);

		panelForm.add(new JLabel("Dich vu:"));
		cbDichVu = new JComboBox<>(
				new String[] { "1-Cat toc", "2-Nhuom toc", "3-Goi dau", "4-Tao kieu", "5-Duong toc" });
		panelForm.add(cbDichVu);

		panelForm.add(new JLabel("Ngay hen:"));
		spNgay = new JSpinner(new SpinnerDateModel());
		spNgay.setEditor(new JSpinner.DateEditor(spNgay, "dd/MM/yyyy"));
		spNgay.setValue(new java.util.Date());
		panelForm.add(spNgay);

		panelForm.add(new JLabel("Gio hen:"));
		cbGio = new JComboBox<>(new String[] { "09:00", "10:00", "11:00", "12:00", "13:00", "15:00", "16:00", "17:00",
				"18:00", "19:00" });
		panelForm.add(cbGio);

		add(panelForm, BorderLayout.CENTER);

		// === Nut bam ===
		JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		btnLuu = new JButton("Luu");
		btnHuy = new JButton("Huy");
		panelBtn.add(btnLuu);
		panelBtn.add(btnHuy);
		add(panelBtn, BorderLayout.SOUTH);

		// === Action listeners ===
		btnHuy.addActionListener(e -> dispose());
		btnLuu.addActionListener(e -> luuLich());

		setVisible(true);
	}

	private void luuLich() {
		String hoTen = txtHoTen.getText().trim();
		String soDienThoai = txtSoDienThoai.getText().trim();
		String gmail = txtGmail.getText().trim();

		if (hoTen.isEmpty() || soDienThoai.isEmpty() || gmail.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui long nhap day du thong tin!");
			return;
		}

		// Lay dichVuId tu ComboBox (vi du "1-Cat toc" -> lay so dau)
		String dichVuSelected = (String) cbDichVu.getSelectedItem();
		int dichVuId = Integer.parseInt(dichVuSelected.split("-")[0]);

		java.util.Date date = (java.util.Date) spNgay.getValue();
		String ngay = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date);
		String gio = (String) cbGio.getSelectedItem();
		String thoiGianHen = ngay + "T" + gio + ":00";

		try {
			URL url = new URL(API + "/api/admin/them-lich");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			String body = "hoTen=" + URLEncoder.encode(hoTen, "UTF-8") + "&soDienThoai="
					+ URLEncoder.encode(soDienThoai, "UTF-8") + "&gmail=" + URLEncoder.encode(gmail, "UTF-8")
					+ "&dichVuId=" + dichVuId + "&thoiGianHen=" + URLEncoder.encode(thoiGianHen, "UTF-8");
			conn.getOutputStream().write(body.getBytes("UTF-8"));

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String ketQua = br.readLine();
			br.close();

			JOptionPane.showMessageDialog(this, ketQua);
			if (ketQua.contains("thanh cong")) {
				mainFrame.taiDuLieuHomNay(); // refresh bang chinh
				dispose(); // dong form
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Loi: " + ex.getMessage());
		}
	}
}