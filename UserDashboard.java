// package com.parking;

// import javax.swing.*;
// import java.awt.*;
// import java.sql.*;

// public class UserDashboard extends JFrame {
//     private String username;
//     private JButton searchSlot, bookSlot, cancelBooking, logout, myBooking;

//     public UserDashboard(String user) {
//         this.username = user;
//         setTitle("User Dashboard - " + user);
//         setSize(420, 360);
//         setLayout(new GridLayout(5, 1, 10, 10));
//         setLocationRelativeTo(null);

//         searchSlot = new JButton("Search Available Slots");
//         bookSlot = new JButton("Book Slot");
//         myBooking = new JButton("My Booking");
//         cancelBooking = new JButton("Cancel Booking");
//         logout = new JButton("Logout");

//         add(searchSlot);
//         add(bookSlot);
//         add(myBooking);
//         add(cancelBooking);
//         add(logout);

//         setVisible(true);
//         setDefaultCloseOperation(EXIT_ON_CLOSE);

//         searchSlot.addActionListener(e -> searchAvailableSlots());
//         bookSlot.addActionListener(e -> new BookSlotFrame(username));
//         myBooking.addActionListener(e -> showMyBooking());
//         cancelBooking.addActionListener(e -> cancelBooking());
//         logout.addActionListener(e -> {
//             new LoginFrame();
//             dispose();
//         });
//     }

//     private void searchAvailableSlots() {
//     try (Connection con = DatabaseConnection.getConnection()) {

//         PreparedStatement pst = con.prepareStatement(
//             "SELECT slot_no, location FROM slots WHERE status='Available' ORDER BY slot_no"
//         );
//         ResultSet rs = pst.executeQuery();

//         String[] columns = {"Slot No", "Location"};
//         java.util.List<String[]> rows = new java.util.ArrayList<>();

//         while (rs.next()) {
//             rows.add(new String[]{
//                 rs.getString("slot_no"),
//                 rs.getString("location")
//             });
//         }

//         if (rows.isEmpty()) {
//             JOptionPane.showMessageDialog(this, "No available slots!");
//             return;
//         }

//         JTable table = new JTable(rows.toArray(new String[0][]), columns);
//         table.setRowHeight(25);

//         JScrollPane scroll = new JScrollPane(table);
//         scroll.setPreferredSize(new Dimension(380, 250));

//         JOptionPane.showMessageDialog(this, scroll, "Available Slots", JOptionPane.INFORMATION_MESSAGE);

//     } catch (Exception e) {
//         JOptionPane.showMessageDialog(this, "DB Error: " + e.getMessage());
//     }
// }



//     private void showMyBooking() {
//     try (Connection con = DatabaseConnection.getConnection()) {

//         PreparedStatement pst = con.prepareStatement(
//             "SELECT slot_no, payment_status, start_time FROM bookings WHERE username=?"
//         );
//         pst.setString(1, username);
//         ResultSet rs = pst.executeQuery();

//         String[] cols = {"Slot No", "Payment", "Start Time"};
//         java.util.List<String[]> rows = new java.util.ArrayList<>();

//         while (rs.next()) {
//             rows.add(new String[]{
//                 rs.getString("slot_no"),
//                 rs.getString("payment_status"),
//                 rs.getString("start_time")
//             });
//         }

//         if (rows.isEmpty()) {
//             JOptionPane.showMessageDialog(this, "No bookings found");
//             return;
//         }

//         JTable table = new JTable(rows.toArray(new String[0][]), cols);
//         table.setRowHeight(25);

//         JScrollPane scroll = new JScrollPane(table);
//         scroll.setPreferredSize(new Dimension(380, 250));

//         JOptionPane.showMessageDialog(
//             this, scroll, "My Bookings", JOptionPane.INFORMATION_MESSAGE
//         );

//     } catch (Exception ex) {
//         ex.printStackTrace();
//         JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
//     }
// }


//     private void cancelBooking() {
//         try (Connection con = DatabaseConnection.getConnection()) {
//             PreparedStatement pst = con.prepareStatement("SELECT slot_no FROM bookings WHERE username=?");
//             pst.setString(1, username);
//             ResultSet rs = pst.executeQuery();
//             if (rs.next()) {
//                 String slot = rs.getString(1);
//                 PreparedStatement del = con.prepareStatement("DELETE FROM bookings WHERE username=?");
//                 del.setString(1, username);
//                 del.executeUpdate();
//                 PreparedStatement upd = con.prepareStatement("UPDATE slots SET status='Available' WHERE slot_no=?");
//                 upd.setString(1, slot);
//                 upd.executeUpdate();
//                 JOptionPane.showMessageDialog(this, "Booking cancelled and slot freed");
//             } else {
//                 JOptionPane.showMessageDialog(this, "No booking to cancel");
//             }
//         } catch (Exception ex) {
//             ex.printStackTrace();
//             JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
//         }
//     }
// }


package com.parking;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import com.parking.CancelBookingFrame;


public class UserDashboard extends JFrame {

    private String currentUser;
    private JTable slotTable;
    private DefaultTableModel slotModel;

    public UserDashboard(String username) {
        this.currentUser = username;

        setTitle("User Dashboard - Welcome " + username);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel title = new JLabel("User Dashboard");
        title.setBounds(300, 15, 400, 40);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        add(title);

        // TABLE FOR SLOTS
        slotModel = new DefaultTableModel(
                new Object[]{"Slot No", "Status", "Location"}, 0
        );
        slotTable = new JTable(slotModel);

        JScrollPane scroll = new JScrollPane(slotTable);
        scroll.setBounds(20, 70, 500, 350);
        add(scroll);

        // Load slots on start
        loadSlots();

        // VIEW SLOTS BUTTON
        JButton refreshBtn = new JButton("Refresh Slots");
        refreshBtn.setBounds(550, 70, 200, 40);
        refreshBtn.addActionListener(e -> loadSlots());
        add(refreshBtn);

        // BOOK SLOT
        JButton bookBtn = new JButton("Book Slot");
        bookBtn.setBounds(550, 130, 200, 40);
        bookBtn.addActionListener(e -> bookSelectedSlot());
        add(bookBtn);

        // CANCEL BOOKING (NEW SELECTIVE CANCEL SCREEN)
        JButton cancelBtn = new JButton("Cancel Booking");
        cancelBtn.setBounds(550, 190, 200, 40);
        cancelBtn.addActionListener(e -> new CancelBookingFrame(currentUser));
        add(cancelBtn);

        // VIEW MY BOOKINGS
        JButton viewBookingsBtn = new JButton("My Bookings");
        viewBookingsBtn.setBounds(550, 250, 200, 40);
        viewBookingsBtn.addActionListener(e -> showUserBookings());
        add(viewBookingsBtn);

        // LOGOUT
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(550, 310, 200, 40);
        logoutBtn.addActionListener(e -> logout());
        add(logoutBtn);

        setVisible(true);
    }

    // LOAD ALL SLOTS INTO TABLE
    private void loadSlots() {
        slotModel.setRowCount(0);

        try (Connection con = DatabaseConnection.getConnection()) {
            PreparedStatement pst = con.prepareStatement(
                    "SELECT slot_no, status, location FROM slots ORDER BY slot_no"
            );

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                slotModel.addRow(new Object[]{
                        rs.getString("slot_no"),
                        rs.getString("status"),
                        rs.getString("location")
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading slots: " + ex.getMessage());
        }
    }

    // BOOK SLOT
private void bookSelectedSlot() {
    int row = slotTable.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Please select a slot!");
        return;
    }

    String slotNo = (String) slotModel.getValueAt(row, 0);
    String status = (String) slotModel.getValueAt(row, 1);

    if (!status.equalsIgnoreCase("Available")) {
        JOptionPane.showMessageDialog(this, "This slot is not available!");
        return;
    }

    try (Connection con = DatabaseConnection.getConnection()) {

        // Create initial booking entry with pending payment
        PreparedStatement pst = con.prepareStatement(
                "INSERT INTO bookings (username, slot_no, payment_status) " +
                        "VALUES (?, ?, 'Pending')"
        );
        pst.setString(1, currentUser);
        pst.setString(2, slotNo);
        pst.executeUpdate();

        JOptionPane.showMessageDialog(this, "Slot Added! Proceed to Payment");

        // No vehicle type passed
        new PaymentGateway(currentUser, slotNo, null);

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this,
                "Booking failed: " + ex.getMessage());
    }
}


    // SHOW USER BOOKINGS
    private void showUserBookings() {
        JFrame frame = new JFrame("My Bookings");
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(this);

        DefaultTableModel userModel = new DefaultTableModel(
                new Object[]{"Slot No", "Start Time", "End Time", "Payment"}, 0
        );
        JTable userTable = new JTable(userModel);

        try (Connection con = DatabaseConnection.getConnection()) {

            PreparedStatement pst = con.prepareStatement(
                    "SELECT slot_no, start_time, end_time, payment_status " +
                            "FROM bookings WHERE username=? ORDER BY id DESC"
            );
            pst.setString(1, currentUser);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                userModel.addRow(new Object[]{
                        rs.getString("slot_no"),
                        rs.getString("start_time"),
                        rs.getString("end_time"),
                        rs.getString("payment_status")
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading bookings: " + ex.getMessage());
        }

        frame.add(new JScrollPane(userTable));
        frame.setVisible(true);
    }

    // LOGOUT
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
                this, "Are you sure you want to logout?",
                "Logout", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame();
        }
    }
}
