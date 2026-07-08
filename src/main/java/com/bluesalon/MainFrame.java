package com.bluesalon;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainFrame extends JFrame {
	private String token;
	private JLabel lblThongKe;
	private JTable table;
	private JScrollPane scrollPane;
	private JButton btnDoiTrangThai;
	private JButton btnThemLich;
	private JButton btnLamMoi;
	private JTextField txtTimKiem;
	private JButton btnTim;
	private DefaultTableModel tableModel;
	private JSpinner spinnerNgay;

	private static final String API = "http://localhost:8081";

	public MainFrame(String token) {
		this.token = token;
		setTitle("BlueSalon - Quan ly lich");
		setSize(900, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 10));

		// === NORTH: thong ke + tim kiem ===
		JPanel panelNorth = new JPanel(new BorderLayout(10, 5));
		panelNorth.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

		lblThongKe = new JLabel("Dang tai...");
		lblThongKe.setFont(new Font("Arial", Font.BOLD, 14));
		panelNorth.add(lblThongKe, BorderLayout.NORTH);

		JPanel panelTim = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelTim.add(new JLabel("Tim theo ten khach:"));
		txtTimKiem = new JTextField(20);
		panelTim.add(txtTimKiem);
		btnTim = new JButton("Tim");
		panelTim.add(btnTim);
		JButton btnXemHomNay = new JButton("Xem hom nay");
		panelTim.add(btnXemHomNay);
		panelTim.add(new JLabel("Ngay:"));
		spinnerNgay = new JSpinner(new SpinnerDateModel());
		spinnerNgay.setEditor(new JSpinner.DateEditor(spinnerNgay, "dd/MM/yyyy"));
		spinnerNgay.setValue(new java.util.Date());
		panelTim.add(spinnerNgay);
		JButton btnXemTheoNgay = new JButton("Xem theo ngay");
		panelTim.add(btnXemTheoNgay);
		btnXemTheoNgay.addActionListener(e -> xemTheoNgay());
		panelNorth.add(panelTim, BorderLayout.SOUTH);

		add(panelNorth, BorderLayout.NORTH);

		// === CENTER: bang lich hen ===
		String[] columns = { "ID", "Ten khach", "SDT", "Dich vu", "Thoi gian", "Trang thai", "Nguon" };
		tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		table = new JTable(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getColumnModel().getColumn(0).setPreferredWidth(40);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(130);
		table.getColumnModel().getColumn(5).setPreferredWidth(80);
		table.getColumnModel().getColumn(6).setPreferredWidth(60);
		scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		// === SOUTH: cac nut ===
		JPanel panelSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		btnLamMoi = new JButton("Lam moi");
		btnDoiTrangThai = new JButton("Doi trang thai");
		JButton btnSuaGio = new JButton("Sua gio");
		btnThemLich = new JButton("Them lich thu cong");
		panelSouth.add(btnLamMoi);
		panelSouth.add(btnDoiTrangThai);
		panelSouth.add(btnSuaGio);
		panelSouth.add(btnThemLich);
		add(panelSouth, BorderLayout.SOUTH);

		// === ACTION LISTENERS ===
		btnLamMoi.addActionListener(e -> taiDuLieuHomNay());
		btnXemHomNay.addActionListener(e -> taiDuLieuHomNay());
		btnTim.addActionListener(e -> timTheoTen());
		txtTimKiem.addActionListener(e -> timTheoTen()); // Enter key

		btnDoiTrangThai.addActionListener(e -> doiTrangThai());
		btnSuaGio.addActionListener(e -> suaGio());
		btnThemLich.addActionListener(e -> new ThemLichFrame(this)); // Load data ngay khi mo
		taiDuLieuHomNay();
		setVisible(true);
	}

	private void xemTheoNgay() {
		java.util.Date date = (java.util.Date) spinnerNgay.getValue();
		String ngay = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date);
		try {
			URL url = new URL(API + "/api/admin/lich-theo-ngay?ngay=" + ngay + "T00:00:00" + "&token=" + token);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null)
				sb.append(line);
			br.close();

			JSONArray arr = new JSONArray(sb.toString());
			tableModel.setRowCount(0);

			int chuaToi = 0, daXong = 0, daHuy = 0;
			for (int i = 0; i < arr.length(); i++) {
				JSONObject o = arr.getJSONObject(i);
				String trangThai = o.getString("trangThaiLich");
				if (trangThai.equals("Chua toi"))
					chuaToi++;
				else if (trangThai.equals("Da xong"))
					daXong++;
				else if (trangThai.equals("Da huy"))
					daHuy++;

				String thoiGian = o.getString("thoiGianHen").replace("T", " ");
				tableModel.addRow(new Object[] { o.getInt("lichHenId"), o.getJSONObject("khachHang").getString("hoTen"),
						o.getJSONObject("khachHang").getString("soDienThoai"),
						o.getJSONObject("dichVu").getString("tenDichVu"), thoiGian, trangThai,
						o.getString("nguonDatLich") });
			}
			lblThongKe.setText(String.format("Ngay %s: Tong %d lich | Chua toi: %d | Da xong: %d | Da huy: %d", ngay,
					arr.length(), chuaToi, daXong, daHuy));

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Loi: " + ex.getMessage());
		}
	}

	public void taiDuLieuHomNay() {
		String ngayHomNay = LocalDate.now().toString(); // YYYY-MM-DD
		try {
			URL url = new URL(API + "/api/admin/lich-theo-ngay?ngay=" + ngayHomNay + "T00:00:00" + "&token=" + token);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null)
				sb.append(line);
			br.close();

			JSONArray arr = new JSONArray(sb.toString());
			tableModel.setRowCount(0); // Xoa data cu

			int chuaToi = 0, daXong = 0, daHuy = 0;

			for (int i = 0; i < arr.length(); i++) {
				JSONObject o = arr.getJSONObject(i);
				String trangThai = o.getString("trangThaiLich");
				if (trangThai.equals("Chua toi"))
					chuaToi++;
				else if (trangThai.equals("Da xong"))
					daXong++;
				else if (trangThai.equals("Da huy"))
					daHuy++;

				String thoiGian = o.getString("thoiGianHen").replace("T", " ");
				tableModel.addRow(new Object[] { o.getInt("lichHenId"), o.getJSONObject("khachHang").getString("hoTen"),
						o.getJSONObject("khachHang").getString("soDienThoai"),
						o.getJSONObject("dichVu").getString("tenDichVu"), thoiGian, trangThai,
						o.getString("nguonDatLich") });
			}

			int tong = arr.length();
			lblThongKe.setText(String.format("Hom nay: Tong %d lich | Chua toi: %d | Da xong: %d | Da huy: %d", tong,
					chuaToi, daXong, daHuy));

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Loi tai du lieu: " + ex.getMessage());
		}
	}

	private void timTheoTen() {
		String keyword = txtTimKiem.getText().trim();
		if (keyword.isEmpty()) {
			taiDuLieuHomNay();
			return;
		}
		try {
			URL url = new URL(
					API + "/api/admin/tim-theo-ten?hoTen=" + URLEncoder.encode(keyword, "UTF-8") + "&token=" + token);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null)
				sb.append(line);
			br.close();

			JSONArray arr = new JSONArray(sb.toString());
			tableModel.setRowCount(0);

			for (int i = 0; i < arr.length(); i++) {
				JSONObject o = arr.getJSONObject(i);
				String thoiGian = o.getString("thoiGianHen").replace("T", " ");
				tableModel.addRow(new Object[] { o.getInt("lichHenId"), o.getJSONObject("khachHang").getString("hoTen"),
						o.getJSONObject("khachHang").getString("soDienThoai"),
						o.getJSONObject("dichVu").getString("tenDichVu"), thoiGian, o.getString("trangThaiLich"),
						o.getString("nguonDatLich") });
			}
			lblThongKe.setText("Tim kiem '" + keyword + "': " + arr.length() + " ket qua");

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Loi: " + ex.getMessage());
		}
	}

	private void doiTrangThai() {
		int row = table.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Vui long chon 1 lich hen!");
			return;
		}
		int lichHenId = (int) tableModel.getValueAt(row, 0);
		String trangThaiHienTai = (String) tableModel.getValueAt(row, 5);

		if (!trangThaiHienTai.equals("Chua toi")) {
			JOptionPane.showMessageDialog(this, "Chi doi duoc lich o trang thai 'Chua toi'!");
			return;
		}

		String[] options = { "Da xong", "Da huy" };
		String chon = (String) JOptionPane.showInputDialog(this, "Chon trang thai moi:", "Doi trang thai",
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		if (chon == null)
			return;

		try {
			URL url = new URL(API + "/api/admin/doi-trang-thai");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			String body = "lichHenId=" + lichHenId + "&trangThaiMoi=" + URLEncoder.encode(chon, "UTF-8") + "&token="
					+ token;
			conn.getOutputStream().write(body.getBytes("UTF-8"));

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String ketQua = br.readLine();
			br.close();

			JOptionPane.showMessageDialog(this, ketQua);
			taiDuLieuHomNay(); // Refresh
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Loi: " + ex.getMessage());
		}
	}

	private void suaGio() {
		int row = table.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Vui long chon 1 lich hen!");
			return;
		}
		int lichHenId = (int) tableModel.getValueAt(row, 0);
		String trangThai = (String) tableModel.getValueAt(row, 5);
		if (!trangThai.equals("Chua toi")) {
			JOptionPane.showMessageDialog(this, "Chi sua duoc lich o trang thai 'Chua toi'!");
			return;
		}

		// Dialog chon gio moi
		String[] gioList = { "09:00", "10:00", "11:00", "12:00", "13:00", "15:00", "16:00", "17:00", "18:00", "19:00" };
		JSpinner spNgay = new JSpinner(new SpinnerDateModel());
		spNgay.setEditor(new JSpinner.DateEditor(spNgay, "dd/MM/yyyy"));
		spNgay.setValue(new java.util.Date());
		JComboBox<String> cbGio = new JComboBox<>(gioList);

		JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
		panel.add(new JLabel("Ngay moi:"));
		panel.add(spNgay);
		panel.add(new JLabel("Gio moi:"));
		panel.add(cbGio);

		int result = JOptionPane.showConfirmDialog(this, panel, "Chon gio moi", JOptionPane.OK_CANCEL_OPTION);
		if (result != JOptionPane.OK_OPTION)
			return;

		java.util.Date date = (java.util.Date) spNgay.getValue();
		String ngay = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date);
		String gio = (String) cbGio.getSelectedItem();
		String thoiGianMoi = ngay + "T" + gio + ":00";

		try {
			URL url = new URL(API + "/api/admin/sua-gio");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			String body = "lichHenId=" + lichHenId + "&thoiGianMoi=" + URLEncoder.encode(thoiGianMoi, "UTF-8")
					+ "&token=" + token;
			conn.getOutputStream().write(body.getBytes("UTF-8"));
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String ketQua = br.readLine();
			br.close();
			JOptionPane.showMessageDialog(this, ketQua);
			taiDuLieuHomNay();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Loi: " + ex.getMessage());
		}
	}

	public String getToken() {
		return token;
	}
}