package com.parking;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class BookSlotFrame extends JFrame {
    private JComboBox<String> slotBox;
    private JComboBox<String> vehicleTypeBox;
    private JButton bookBtn;
    private String username;

    public BookSlotFrame(String user) {
        this.username = user;
        setTitle("Book Slot");
        setSize(420, 300);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel lbl = new JLabel("Select Slot:");
        lbl.setBounds(40, 60, 100, 30);
        add(lbl);

        slotBox = new JComboBox<>();
        slotBox.setBounds(150, 60, 200, 30);
        add(slotBox);

        JLabel vLbl = new JLabel("Vehicle Type:");
        vLbl.setBounds(40, 100, 100, 30);
        add(vLbl);

        vehicleTypeBox = new JComboBox<>(new String[]{"2 Wheeler", "4 Wheeler"});
        vehicleTypeBox.setBounds(150, 100, 200, 30);
        add(vehicleTypeBox);

        bookBtn = new JButton("Book");
        bookBtn.setBounds(150, 160, 100, 30);
        add(bookBtn);

        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        loadAvailableSlots();

        bookBtn.addActionListener(e -> bookSlot());
    }

    private void loadAvailableSlots() {
    try (Connection con = DatabaseConnection.getConnection()) {
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT slot_no, location FROM slots WHERE status='Available'");
        
        while (rs.next()) {
            String slotView = rs.getString("slot_no") + " (" + rs.getString("location") + ")";
            slotBox.addItem(slotView);
        }
        if (slotBox.getItemCount() == 0) {
            slotBox.addItem("No Available Slots");
            bookBtn.setEnabled(false);
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
    }
}


    private void bookSlot() {
    String selected = (String) slotBox.getSelectedItem();
    if (selected == null || selected.equals("No Available Slots")) return;

    String slot = selected.split(" ")[0];  // Extract A1 from "A1 (location)"

    try (Connection con = DatabaseConnection.getConnection()) {

        PreparedStatement check = con.prepareStatement("SELECT status FROM slots WHERE slot_no=?");
        check.setString(1, slot);
        ResultSet rs = check.executeQuery();

        if (rs.next() && rs.getString("status").equalsIgnoreCase("Available")) {

            PreparedStatement upd = con.prepareStatement("UPDATE slots SET status='Booked' WHERE slot_no=?");
            upd.setString(1, slot);
            upd.executeUpdate();

            String vehicleType = (String) vehicleTypeBox.getSelectedItem();

            // NEW: Save start_time = NOW()
            PreparedStatement ins = con.prepareStatement(
                "INSERT INTO bookings(username, slot_no, vehicle_type, payment_status, start_time) " +
                "VALUES(?, ?, ?, 'Pending', NOW())"
            );
            ins.setString(1, username);
            ins.setString(2, slot);
            ins.setString(3, vehicleType);
            ins.executeUpdate();

            JOptionPane.showMessageDialog(this, "Slot booked! Proceed to payment.");

            new PaymentGateway(username, slot, vehicleType);
            dispose();

        } else {
            JOptionPane.showMessageDialog(this, "Slot no longer available!");
        }

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
    }
}

}
