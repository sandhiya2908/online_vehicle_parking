// // // package com.parking;

// // // import javax.swing.*;
// // // import java.awt.*;
// // // import java.sql.*;

// // // public class PaymentGateway extends JFrame {
// // //     private static final double HOURLY_RATE = 50.0;

// // //     private final String username;
// // //     private final String slotNo;
// // //     private final String vehicleType;
// // //     private final JSpinner durationSpinner;
// // //     private final JLabel amountValueLabel;
// // //     private final JComboBox<String> paymentMethodBox;

// // //     public PaymentGateway(String user, String slot, String vehicleType) {
// // //         this.username = user;
// // //         this.slotNo = slot;
// // //         this.vehicleType = vehicleType;

// // //         setTitle("Payment Gateway");
// // //         setSize(460, 320);
// // //         setLayout(new BorderLayout(10, 10));
// // //         setLocationRelativeTo(null);
// // //         setDefaultCloseOperation(DISPOSE_ON_CLOSE);

// // //         JLabel header = new JLabel("Confirm Parking for Slot " + slot, SwingConstants.CENTER);
// // //         header.setFont(header.getFont().deriveFont(Font.BOLD, 16f));
// // //         add(header, BorderLayout.NORTH);

// // //         JPanel formPanel = new JPanel(new GridBagLayout());
// // //         formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
// // //         add(formPanel, BorderLayout.CENTER);

// // //         GridBagConstraints gbc = new GridBagConstraints();
// // //         gbc.insets = new Insets(8, 8, 8, 8);
// // //         gbc.anchor = GridBagConstraints.WEST;
// // //         gbc.fill = GridBagConstraints.HORIZONTAL;

// // //         // Duration row
// // //         gbc.gridx = 0;
// // //         gbc.gridy = 0;
// // //         formPanel.add(new JLabel("Parking Duration (hours):"), gbc);

// // //         durationSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 24, 1));
// // //         durationSpinner.addChangeListener(e -> updateAmount());
// // //         gbc.gridx = 1;
// // //         formPanel.add(durationSpinner, gbc);

// // //         // Amount row
// // //         gbc.gridx = 0;
// // //         gbc.gridy = 1;
// // //         formPanel.add(new JLabel("Amount to Pay:"), gbc);

// // //         amountValueLabel = new JLabel();
// // //         amountValueLabel.setFont(amountValueLabel.getFont().deriveFont(Font.BOLD));
// // //         gbc.gridx = 1;
// // //         formPanel.add(amountValueLabel, gbc);

// // //         // Payment method row
// // //         gbc.gridx = 0;
// // //         gbc.gridy = 2;
// // //         formPanel.add(new JLabel("Payment Method:"), gbc);

// // //         paymentMethodBox = new JComboBox<>(new String[]{
// // //                 "Select Method",
// // //                 "UPI",
// // //                 "Credit Card",
// // //                 "Debit Card",
// // //                 "Net Banking",
// // //                 "Cash"
// // //         });
// // //         gbc.gridx = 1;
// // //         formPanel.add(paymentMethodBox, gbc);

// // //         JButton pay = new JButton("Proceed to Pay");
// // //         add(pay, BorderLayout.SOUTH);

// // //         pay.addActionListener(e -> processPayment());

// // //         updateAmount();
// // //         setVisible(true);
// // //     }

// // //     private void updateAmount() {
// // //         int hours = (int) durationSpinner.getValue();
// // //         double amount = calculateAmount(hours);
// // //         amountValueLabel.setText(formatAmount(amount));
// // //     }

// // //     private double calculateAmount(int hours) {
// // //         return hours * HOURLY_RATE;
// // //     }

// // //     private String formatAmount(double amount) {
// // //         return String.format("Rs. %.2f", amount);
// // //     }

// // //     private void processPayment() {
// // //         int hours = (int) durationSpinner.getValue();
// // //         if (hours <= 0) {
// // //             JOptionPane.showMessageDialog(this, "Please select a valid duration.");
// // //             return;
// // //         }

// // //         if (paymentMethodBox.getSelectedIndex() == 0) {
// // //             JOptionPane.showMessageDialog(this, "Please choose a payment method.");
// // //             return;
// // //         }

// // //         String method = (String) paymentMethodBox.getSelectedItem();
// // //         double amount = calculateAmount(hours);

// // //         int confirm = JOptionPane.showConfirmDialog(
// // //                 this,
// // //                 String.format("Pay %s via %s?", formatAmount(amount), method),
// // //                 "Confirm Payment",
// // //                 JOptionPane.YES_NO_OPTION
// // //         );

// // //         if (confirm != JOptionPane.YES_OPTION) {
// // //             return;
// // //         }

// // //         try (Connection con = DatabaseConnection.getConnection()) {
// // //             PreparedStatement pst = con.prepareStatement("UPDATE bookings SET payment_status='Success' WHERE username=? AND slot_no=?");
// // //             pst.setString(1, username);
// // //             pst.setString(2, slotNo);
// // //             pst.executeUpdate();

// // //             PreparedStatement upd = con.prepareStatement("UPDATE slots SET status='Occupied' WHERE slot_no=?");
// // //             upd.setString(1, slotNo);
// // //             upd.executeUpdate();

// // //             // Show and save bill/receipt after successful payment
// // //             try {
// // //                 new BillFrame(username, slotNo, vehicleType, hours, amount, method);
// // //             } catch (Exception bfEx) {
// // //                 // If the bill UI fails, still notify the user
// // //                 JOptionPane.showMessageDialog(this, "Payment of " + formatAmount(amount) + " via " + method + " successful! (Receipt UI failed)");
// // //             }
// // //             JOptionPane.showMessageDialog(this, String.format("Payment of %s via %s successful! Receipt Generated.", formatAmount(amount), method));
// // //             // close payment window
// // //             dispose();
// // //         } catch (Exception ex) {
// // //             ex.printStackTrace();
// // //             JOptionPane.showMessageDialog(this, "Payment failed: " + ex.getMessage());
// // //         }
// // //     }
// // // }


// // package com.parking;

// // import javax.swing.*;
// // import java.awt.*;
// // import java.sql.*;
// // import java.time.LocalDateTime;
// // import java.time.LocalTime;
// // import java.time.LocalDate;
// // import java.time.format.DateTimeFormatter;

// // public class PaymentGateway extends JFrame {

// //     private static final double HOURLY_RATE = 50.0;

// //     private final String username;
// //     private final String slotNo;
// //     private final String vehicleType;

// //     private JSpinner durationSpinner;
// //     private JComboBox<String> paymentMethodBox;

// //     private JSpinner dateSpinner;
// //     private JSpinner timeSpinner;
// //     private JLabel amountValueLabel;

// //     public PaymentGateway(String user, String slot, String vehicleType) {
// //         this.username = user;
// //         this.slotNo = slot;
// //         this.vehicleType = vehicleType;

// //         setTitle("Payment Gateway");
// //         setSize(520, 430);
// //         setLocationRelativeTo(null);
// //         setLayout(new GridBagLayout());
// //         setDefaultCloseOperation(DISPOSE_ON_CLOSE);

// //         GridBagConstraints gbc = new GridBagConstraints();
// //         gbc.insets = new Insets(8, 8, 8, 8);
// //         gbc.fill = GridBagConstraints.HORIZONTAL;

// //         JLabel title = new JLabel("Confirm Parking for Slot " + slot, SwingConstants.CENTER);
// //         title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
// //         gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
// //         add(title, gbc);

// //         gbc.gridwidth = 1;

// //         // Select Date
// //         gbc.gridx = 0; gbc.gridy = 1;
// //         add(new JLabel("Parking Date:"), gbc);

// //         dateSpinner = new JSpinner(new SpinnerDateModel());
// //         JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
// //         dateSpinner.setEditor(dateEditor);
// //         gbc.gridx = 1;
// //         add(dateSpinner, gbc);

// //         // Select Time
// //         gbc.gridx = 0; gbc.gridy = 2;
// //         add(new JLabel("Start Time (HH:mm):"), gbc);

// //         timeSpinner = new JSpinner(new SpinnerDateModel());
// //         JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
// //         timeSpinner.setEditor(timeEditor);
// //         gbc.gridx = 1;
// //         add(timeSpinner, gbc);

// //         // Duration
// //         gbc.gridx = 0; gbc.gridy = 3;
// //         add(new JLabel("Parking Duration (hours):"), gbc);

// //         durationSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 48, 1));
// //         durationSpinner.addChangeListener(e -> updateAmount());
// //         gbc.gridx = 1;
// //         add(durationSpinner, gbc);

// //         // Amount
// //         gbc.gridx = 0; gbc.gridy = 4;
// //         add(new JLabel("Amount to Pay:"), gbc);

// //         amountValueLabel = new JLabel("Rs. 50.00");
// //         amountValueLabel.setFont(amountValueLabel.getFont().deriveFont(Font.BOLD));
// //         gbc.gridx = 1;
// //         add(amountValueLabel, gbc);

// //         // Payment method
// //         gbc.gridx = 0; gbc.gridy = 5;
// //         add(new JLabel("Payment Method:"), gbc);

// //         paymentMethodBox = new JComboBox<>(new String[]{
// //                 "Select Method", "UPI", "Credit Card", "Debit Card", "Net Banking", "Cash"
// //         });
// //         gbc.gridx = 1;
// //         add(paymentMethodBox, gbc);

// //         // Button
// //         JButton payButton = new JButton("Proceed to Pay");
// //         payButton.addActionListener(e -> processPayment());
// //         gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
// //         add(payButton, gbc);

// //         updateAmount();
// //         setVisible(true);
// //     }

// //     private void updateAmount() {
// //         int hours = (int) durationSpinner.getValue();
// //         double amount = hours * HOURLY_RATE;
// //         amountValueLabel.setText("Rs. " + amount);
// //     }

// //     private void processPayment() {
// //         int hours = (int) durationSpinner.getValue();
// //         if (paymentMethodBox.getSelectedIndex() == 0) {
// //             JOptionPane.showMessageDialog(this, "Please select a payment method");
// //             return;
// //         }

// //         String method = (String) paymentMethodBox.getSelectedItem();

// //         // Extract Date and Time selections
// //         LocalDate date = LocalDate.parse(new java.text.SimpleDateFormat("yyyy-MM-dd").format(dateSpinner.getValue()));
// //         LocalTime time = LocalTime.parse(new java.text.SimpleDateFormat("HH:mm").format(timeSpinner.getValue()));
// //         LocalDateTime startDateTime = LocalDateTime.of(date, time);
// //         LocalDateTime endDateTime = startDateTime.plusHours(hours);

// //         try (Connection con = DatabaseConnection.getConnection()) {

// //             // Update booking
// //             PreparedStatement pst = con.prepareStatement(
// //                 "UPDATE bookings SET payment_status='Success', start_time=?, end_time=? WHERE username=? AND slot_no=?"
// //             );
// //             pst.setTimestamp(1, Timestamp.valueOf(startDateTime));
// //             pst.setTimestamp(2, Timestamp.valueOf(endDateTime));
// //             pst.setString(3, username);
// //             pst.setString(4, slotNo);
// //             pst.executeUpdate();

// //             // Update slot to occupied
// //             PreparedStatement upd = con.prepareStatement("UPDATE slots SET status='Occupied' WHERE slot_no=?");
// //             upd.setString(1, slotNo);
// //             upd.executeUpdate();

// //             JOptionPane.showMessageDialog(this,
// //                 "Payment Successful!\nStart: " + startDateTime + "\nEnd: " + endDateTime);

// //             dispose();

// //         } catch (Exception ex) {
// //             JOptionPane.showMessageDialog(this, "Payment Error: " + ex.getMessage());
// //         }
// //     }
// // }


// package com.parking;

// import javax.swing.*;
// import java.awt.*;
// import java.sql.*;
// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.time.LocalTime;
// import java.time.format.DateTimeFormatter;

// public class PaymentGateway extends JFrame {

//     private static final double HOURLY_RATE = 50.0;

//     private final String username;
//     private final String slotNo;
//     private final String vehicleType;

//     private JSpinner durationSpinner;
//     private JComboBox<String> paymentMethodBox;

//     private JSpinner dateSpinner;
//     private JSpinner timeSpinner;
//     private JLabel amountValueLabel;

//     public PaymentGateway(String user, String slot, String vehicleType) {
//         this.username = user;
//         this.slotNo = slot;
//         this.vehicleType = vehicleType;

//         setTitle("Payment Gateway");
//         setSize(520, 430);
//         setLocationRelativeTo(null);
//         setLayout(new GridBagLayout());
//         setDefaultCloseOperation(DISPOSE_ON_CLOSE);

//         GridBagConstraints gbc = new GridBagConstraints();
//         gbc.insets = new Insets(8, 8, 8, 8);
//         gbc.fill = GridBagConstraints.HORIZONTAL;

//         JLabel title = new JLabel("Confirm Parking for Slot " + slot, SwingConstants.CENTER);
//         title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
//         gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
//         add(title, gbc);

//         gbc.gridwidth = 1;

//         // Select Date
//         gbc.gridx = 0; gbc.gridy = 1;
//         add(new JLabel("Parking Date:"), gbc);

//         dateSpinner = new JSpinner(new SpinnerDateModel());
//         JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
//         dateSpinner.setEditor(dateEditor);
//         gbc.gridx = 1;
//         add(dateSpinner, gbc);

//         // Select Time
//         gbc.gridx = 0; gbc.gridy = 2;
//         add(new JLabel("Start Time (HH:mm):"), gbc);

//         timeSpinner = new JSpinner(new SpinnerDateModel());
//         JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
//         timeSpinner.setEditor(timeEditor);
//         gbc.gridx = 1;
//         add(timeSpinner, gbc);

//         // Duration
//         gbc.gridx = 0; gbc.gridy = 3;
//         add(new JLabel("Parking Duration (hours):"), gbc);

//         durationSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 48, 1));
//         durationSpinner.addChangeListener(e -> updateAmount());
//         gbc.gridx = 1;
//         add(durationSpinner, gbc);

//         // Amount
//         gbc.gridx = 0; gbc.gridy = 4;
//         add(new JLabel("Amount to Pay:"), gbc);

//         amountValueLabel = new JLabel("Rs. 50.00");
//         amountValueLabel.setFont(amountValueLabel.getFont().deriveFont(Font.BOLD));
//         gbc.gridx = 1;
//         add(amountValueLabel, gbc);

//         // Payment method
//         gbc.gridx = 0; gbc.gridy = 5;
//         add(new JLabel("Payment Method:"), gbc);

//         paymentMethodBox = new JComboBox<>(new String[]{
//                 "Select Method", "UPI", "Credit Card", "Debit Card", "Net Banking", "Cash"
//         });
//         gbc.gridx = 1;
//         add(paymentMethodBox, gbc);

//         // Pay Button
//         JButton payButton = new JButton("Proceed to Pay");
//         payButton.addActionListener(e -> processPayment());
//         gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
//         add(payButton, gbc);

//         updateAmount();
//         setVisible(true);
//     }

//     private void updateAmount() {
//         int hours = (int) durationSpinner.getValue();
//         double amount = hours * HOURLY_RATE;
//         amountValueLabel.setText("Rs. " + amount);
//     }

//     private void processPayment() {
//         int hours = (int) durationSpinner.getValue();

//         if (paymentMethodBox.getSelectedIndex() == 0) {
//             JOptionPane.showMessageDialog(this, "Please select a payment method");
//             return;
//         }

//         String method = (String) paymentMethodBox.getSelectedItem();

//         // Extract Date and Time
//         LocalDate date = LocalDate.parse(new java.text.SimpleDateFormat("yyyy-MM-dd")
//                 .format(dateSpinner.getValue()));

//         LocalTime time = LocalTime.parse(new java.text.SimpleDateFormat("HH:mm")
//                 .format(timeSpinner.getValue()));

//         LocalDateTime startDateTime = LocalDateTime.of(date, time);
//         LocalDateTime endDateTime = startDateTime.plusHours(hours);

//         double amount = hours * HOURLY_RATE;

//         try (Connection con = DatabaseConnection.getConnection()) {

//             // Update booking with start_time and end_time
//             PreparedStatement pst = con.prepareStatement(
//                 "UPDATE bookings SET payment_status='Success', start_time=?, end_time=? " +
//                         "WHERE username=? AND slot_no=? AND payment_status='Pending'"
//             );

//             pst.setTimestamp(1, Timestamp.valueOf(startDateTime));
//             pst.setTimestamp(2, Timestamp.valueOf(endDateTime));
//             pst.setString(3, username);
//             pst.setString(4, slotNo);
//             pst.executeUpdate();

//             // Update slot as occupied
//             PreparedStatement upd = con.prepareStatement(
//                     "UPDATE slots SET status='Occupied' WHERE slot_no=?");
//             upd.setString(1, slotNo);
//             upd.executeUpdate();

//             // Show receipt
//             new BillFrame(username, slotNo, vehicleType,
//                     hours, amount, method, startDateTime, endDateTime);

//             JOptionPane.showMessageDialog(this,
//                     "Payment Successful!\nStart: " + startDateTime +
//                             "\nEnd: " + endDateTime);

//             dispose();

//         } catch (Exception ex) {
//             JOptionPane.showMessageDialog(this, "Payment Error: " + ex.getMessage());
//         }
//     }
// }


package com.parking;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class PaymentGateway extends JFrame {

    private static final double HOURLY_RATE = 50.0;

    private final String username;
    private final String slotNo;

    private JComboBox<String> vehicleTypeBox;
    private JSpinner dateSpinner;
    private JSpinner timeSpinner;
    private JSpinner durationSpinner;
    private JLabel amountLabel;
    private JComboBox<String> paymentMethodBox;

    public PaymentGateway(String user, String slot, String vehicleType) {
        this.username = user;
        this.slotNo = slot;

        setTitle("Payment Gateway - Slot " + slot);
        setSize(480, 420);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Font labelFont = new Font("Arial", Font.PLAIN, 15);

        // ------- VEHICLE TYPE -------
        JLabel vtLbl = new JLabel("Vehicle Type:");
        vtLbl.setBounds(40, 40, 150, 30);
        vtLbl.setFont(labelFont);
        add(vtLbl);

        vehicleTypeBox = new JComboBox<>(new String[]{
                "Two Wheeler", "Four Wheeler"
        });
        vehicleTypeBox.setBounds(200, 40, 200, 30);
        if (vehicleType != null) vehicleTypeBox.setSelectedItem(vehicleType);
        add(vehicleTypeBox);

        // ------- PARKING DATE -------
        JLabel dateLbl = new JLabel("Parking Date:");
        dateLbl.setBounds(40, 90, 150, 30);
        dateLbl.setFont(labelFont);
        add(dateLbl);

        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setBounds(200, 90, 200, 30);
        add(dateSpinner);

        // ------- START TIME -------
        JLabel timeLbl = new JLabel("Start Time (HH:mm):");
        timeLbl.setBounds(40, 140, 150, 30);
        timeLbl.setFont(labelFont);
        add(timeLbl);

        timeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setBounds(200, 140, 200, 30);
        add(timeSpinner);

        // ------- PARKING DURATION -------
        JLabel durLbl = new JLabel("Parking Duration (hours):");
        durLbl.setBounds(40, 190, 200, 30);
        durLbl.setFont(labelFont);
        add(durLbl);

        durationSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 48, 1));
        durationSpinner.setBounds(250, 190, 60, 30);
        durationSpinner.addChangeListener(e -> updateAmount());
        add(durationSpinner);

        // ------- AMOUNT -------
        JLabel amtLbl = new JLabel("Amount to Pay:");
        amtLbl.setBounds(40, 240, 200, 30);
        amtLbl.setFont(labelFont);
        add(amtLbl);

        amountLabel = new JLabel("Rs. 50.00");
        amountLabel.setBounds(200, 240, 200, 30);
        amountLabel.setFont(new Font("Arial", Font.BOLD, 15));
        add(amountLabel);

        // ------- PAYMENT METHOD -------
        JLabel methodLbl = new JLabel("Payment Method:");
        methodLbl.setBounds(40, 290, 200, 30);
        methodLbl.setFont(labelFont);
        add(methodLbl);

        paymentMethodBox = new JComboBox<>(new String[]{
                "Select Method", "UPI", "Credit Card", "Debit Card", "Net Banking", "Cash"
        });
        paymentMethodBox.setBounds(200, 290, 200, 30);
        add(paymentMethodBox);

        // ------- PAY BUTTON -------
        JButton payBtn = new JButton("Proceed to Pay");
        payBtn.setBounds(140, 340, 200, 40);
        payBtn.addActionListener(e -> processPayment());
        add(payBtn);

        updateAmount();
        setVisible(true);
    }

    private void updateAmount() {
        int hrs = (int) durationSpinner.getValue();
        double amount = hrs * HOURLY_RATE;
        amountLabel.setText("Rs. " + amount);
    }

    private void processPayment() {
        if (paymentMethodBox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Select a payment method");
            return;
        }

        String vehicleType = (String) vehicleTypeBox.getSelectedItem();
        String method = (String) paymentMethodBox.getSelectedItem();
        int hrs = (int) durationSpinner.getValue();
        double amount = hrs * HOURLY_RATE;

        // Extract date + time
        LocalDate date = LocalDate.parse(new java.text.SimpleDateFormat("yyyy-MM-dd")
                .format(dateSpinner.getValue()));

        LocalTime time = LocalTime.parse(new java.text.SimpleDateFormat("HH:mm")
                .format(timeSpinner.getValue()));

        LocalDateTime start = LocalDateTime.of(date, time);
        LocalDateTime end = start.plusHours(hrs);

        try (Connection con = DatabaseConnection.getConnection()) {

            // Update booking record
            PreparedStatement pst = con.prepareStatement(
                "UPDATE bookings SET vehicle_type=?, payment_status='Success', start_time=?, end_time=? " +
                        "WHERE username=? AND slot_no=? AND payment_status='Pending'"
            );

            pst.setString(1, vehicleType);
            pst.setTimestamp(2, Timestamp.valueOf(start));
            pst.setTimestamp(3, Timestamp.valueOf(end));
            pst.setString(4, username);
            pst.setString(5, slotNo);
            pst.executeUpdate();

            // Make slot occupied
            PreparedStatement pst2 = con.prepareStatement(
                    "UPDATE slots SET status='Occupied' WHERE slot_no=?");
            pst2.setString(1, slotNo);
            pst2.executeUpdate();

            // Receipt
            new BillFrame(username, slotNo, vehicleType, hrs, amount, method, start, end);

            JOptionPane.showMessageDialog(this,
                    "Payment Successful!\nStart: " + start + "\nEnd: " + end);

            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Payment Error: " + ex.getMessage());
        }
    }
}
