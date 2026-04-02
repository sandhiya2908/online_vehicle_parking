package com.parking;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    private JTextField username;
    private JPasswordField password;
    private JButton loginBtn, registerBtn, adminBtn;

    public LoginFrame() {
        setTitle("Vehicle Parking System - Login");
        setSize(420, 320);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("Vehicle Parking System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(60, 20, 300, 30);
        add(title);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 80, 100, 30);
        add(userLabel);

        username = new JTextField();
        username.setBounds(160, 80, 180, 30);
        add(username);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 120, 100, 30);
        add(passLabel);

        password = new JPasswordField();
        password.setBounds(160, 120, 180, 30);
        add(password);

        loginBtn = new JButton("Login");
        loginBtn.setBounds(50, 190, 100, 30);
        add(loginBtn);

        registerBtn = new JButton("Register");
        registerBtn.setBounds(160, 190, 100, 30);
        add(registerBtn);

        adminBtn = new JButton("Admin Login");
        adminBtn.setBounds(270, 190, 120, 30);
        add(adminBtn);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        loginBtn.addActionListener(e -> loginUser());
        registerBtn.addActionListener(e -> {
            new RegisterFrame();
            dispose();
        });
        adminBtn.addActionListener(e -> {
            String adminUser = JOptionPane.showInputDialog(this, "Enter admin username:");
            String adminPass = JOptionPane.showInputDialog(this, "Enter admin password:");
            // very simple admin check for demo (in real app verify from DB)
            if ("admin".equals(adminUser) && "admin".equals(adminPass)) {
                new AdminDashboard();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid admin credentials");
            }
        });
    }

    private void loginUser() {
        String user = username.getText().trim();
        String pass = new String(password.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields");
            return;
        }

        try (Connection con = DatabaseConnection.getConnection()) {
            PreparedStatement pst = con.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            pst.setString(1, user);
            pst.setString(2, pass);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                new UserDashboard(user);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }
}
