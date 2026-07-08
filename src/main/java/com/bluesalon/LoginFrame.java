package com.bluesalon;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class LoginFrame extends JFrame {

	private JLabel lblUsername;
	private JLabel lblPassword;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JButton btnDangNhap;

	public LoginFrame() {
		setTitle("BlueSalon - Dang nhap Admin");
		setSize(350, 150);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		// Khoi tao component
		lblUsername = new JLabel("Username:");
		lblPassword = new JLabel("Password:");
		txtUsername = new JTextField(20);
		txtPassword = new JPasswordField(20);
		btnDangNhap = new JButton("Dang nhap");

		// Panel chinh
		JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
		panel.add(lblUsername);
		panel.add(txtUsername);
		panel.add(lblPassword);
		panel.add(txtPassword);
		panel.add(new JLabel());
		panel.add(btnDangNhap);
		add(panel, BorderLayout.CENTER);

		// Xu ly dang nhap
		btnDangNhap.addActionListener(e -> {
			String username = txtUsername.getText();
			String password = new String(txtPassword.getPassword());

			try {
				URL url = new URL("http://localhost:8081/api/admin/dang-nhap");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);

				String params = "tenDangNhap=" + URLEncoder.encode(username, "UTF-8") + "&matKhau="
						+ URLEncoder.encode(password, "UTF-8");
				conn.getOutputStream().write(params.getBytes());

				String ketQua = new String(conn.getInputStream().readAllBytes());

				if (ketQua.startsWith("OK:")) {
					String token = ketQua.substring(3);
					SwingUtilities.invokeLater(() -> {
						new MainFrame(token);
						dispose();
					});
				} else {
					JOptionPane.showMessageDialog(this, ketQua, "Loi", JOptionPane.ERROR_MESSAGE);
				}

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Khong ket noi duoc server!", "Loi", JOptionPane.ERROR_MESSAGE);
			}
		});

		setVisible(true);
	}

	public static void main(String[] args) {
		new LoginFrame();
	}
}