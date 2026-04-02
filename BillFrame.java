// package com.parking;

// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.io.File;
// import java.io.FileWriter;
// import java.io.IOException;
// import java.time.LocalDateTime;
// import java.time.format.DateTimeFormatter;

// public class BillFrame extends JFrame {
//     private final String billText;

//     public BillFrame(String username, String slotNo, String vehicleType, int hours, double amount, String method) {
//         setTitle("Payment Receipt");
//         setSize(420, 460);
//         setLocationRelativeTo(null);
//         setDefaultCloseOperation(DISPOSE_ON_CLOSE);

//         DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//         String time = dtf.format(LocalDateTime.now());

//         StringBuilder sb = new StringBuilder();
//         sb.append("--- Vehicle Parking Receipt ---\n");
//         sb.append("Date/Time : ").append(time).append("\n");
//         sb.append("User      : ").append(username).append("\n");
//         sb.append("Slot No   : ").append(slotNo).append("\n");
//         sb.append("Vehicle   : ").append(vehicleType).append("\n");
//         sb.append("Duration  : ").append(hours).append(" hour(s)\n");
//         sb.append("Rate/hr   : Rs. 50.00\n");
//         sb.append("Amount    : Rs. ").append(String.format("%.2f", amount)).append("\n");
//         sb.append("Payment   : ").append(method).append("\n");
//         sb.append("-------------------------------\n");
//         sb.append("Thank you for using our service!\n");

//         billText = sb.toString();

//         JTextArea area = new JTextArea(billText);
//         area.setEditable(false);
//         area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
//         JScrollPane scroll = new JScrollPane(area);
//         add(scroll, BorderLayout.CENTER);

//         JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//         JButton saveBtn = new JButton("Save Receipt");
//         JButton closeBtn = new JButton("Close");
//         bottom.add(saveBtn);
//         bottom.add(closeBtn);
//         add(bottom, BorderLayout.SOUTH);

//         saveBtn.addActionListener((ActionEvent e) -> saveReceiptToFile());
//         closeBtn.addActionListener((ActionEvent e) -> dispose());

//         // Auto-save a receipt file in the working directory for convenience
//         try {
//             autoSaveReceipt();
//         } catch (IOException ignored) {
//             // If auto-save fails, user can still save manually
//         }

//         setVisible(true);
//     }

//     private void autoSaveReceipt() throws IOException {
//         String dir = System.getProperty("user.dir");
//         DateTimeFormatter tf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
//         String filename = String.format("receipt_%s.txt", tf.format(LocalDateTime.now()));
//         File file = new File(dir, filename);
//         try (FileWriter fw = new FileWriter(file)) {
//             fw.write(billText);
//         }
//     }

//     private void saveReceiptToFile() {
//         JFileChooser chooser = new JFileChooser();
//         chooser.setDialogTitle("Save Receipt As");
//         int ret = chooser.showSaveDialog(this);
//         if (ret == JFileChooser.APPROVE_OPTION) {
//             File file = chooser.getSelectedFile();
//             // ensure .txt extension for clarity
//             if (!file.getName().toLowerCase().endsWith(".txt")) {
//                 file = new File(file.getAbsolutePath() + ".txt");
//             }

//             try {
//                 java.nio.file.Files.writeString(file.toPath(), billText, java.nio.charset.StandardCharsets.UTF_8);
//                 JOptionPane.showMessageDialog(this, "Receipt saved to " + file.getAbsolutePath());
//             } catch (Throwable t) {
//                 // show detailed error and write temp log for inspection
//                 String msg = "Failed to save receipt: " + t.getClass().getName() + ": " + t.getMessage();
//                 StringBuilder sb = new StringBuilder(msg).append("\n\n");
//                 for (StackTraceElement el : t.getStackTrace()) sb.append(el.toString()).append("\n");

//                 JTextArea ta = new JTextArea(sb.toString());
//                 ta.setEditable(false);
//                 ta.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
//                 JScrollPane sp = new JScrollPane(ta);
//                 sp.setPreferredSize(new Dimension(600, 300));
//                 JOptionPane.showMessageDialog(this, sp, "Save Error", JOptionPane.ERROR_MESSAGE);

//                 try {
//                     File tmp = File.createTempFile("receipt_save_error_", ".log");
//                     try (FileWriter fw = new FileWriter(tmp)) {
//                         fw.write(sb.toString());
//                     }
//                     JOptionPane.showMessageDialog(this, "Error log saved to: " + tmp.getAbsolutePath());
//                 } catch (IOException io) {
//                     // ignore
//                 }
//             }
//         }
//     }

    
// }


package com.parking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BillFrame extends JFrame {

    private final String billText;

    public BillFrame(String username, String slotNo, String vehicleType,
                     int hours, double amount, String method,
                     LocalDateTime start, LocalDateTime end) {

        setTitle("Parking Receipt");
        setSize(450, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        StringBuilder sb = new StringBuilder();
        sb.append("----------- PARKING RECEIPT -----------\n");
        sb.append("User          : ").append(username).append("\n");
        sb.append("Slot Number   : ").append(slotNo).append("\n");
        sb.append("Vehicle Type  : ").append(vehicleType).append("\n");
        sb.append("Start Time    : ").append(start.format(dtf)).append("\n");
        sb.append("End Time      : ").append(end.format(dtf)).append("\n");
        sb.append("Duration      : ").append(hours).append(" hour(s)\n");
        sb.append("Rate per Hour : Rs. 50.00\n");
        sb.append("Total Amount  : Rs. ").append(String.format("%.2f", amount)).append("\n");
        sb.append("Payment Mode  : ").append(method).append("\n");
        sb.append("----------------------------------------\n");
        sb.append("Thank you for using the Parking System!\n");

        billText = sb.toString();

        JTextArea area = new JTextArea(billText);
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(area);
        add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Save Receipt");
        JButton closeBtn = new JButton("Close");
        bottom.add(saveBtn);
        bottom.add(closeBtn);
        add(bottom, BorderLayout.SOUTH);

        saveBtn.addActionListener((ActionEvent e) -> saveReceipt());
        closeBtn.addActionListener((ActionEvent e) -> dispose());

        // Auto-save receipt
        try {
            autoSave();
        } catch (IOException ignored) {}

        setVisible(true);
    }

    private void autoSave() throws IOException {
        String dir = System.getProperty("user.dir");
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        File file = new File(dir, "receipt_" + tf.format(LocalDateTime.now()) + ".txt");

        try (FileWriter fw = new FileWriter(file)) {
            fw.write(billText);
        }
    }

    private void saveReceipt() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Receipt");
        int ret = chooser.showSaveDialog(this);

        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            if (!file.getName().toLowerCase().endsWith(".txt")) {
                file = new File(file.getAbsolutePath() + ".txt");
            }

            try {
                FileWriter fw = new FileWriter(file);
                fw.write(billText);
                fw.close();

                JOptionPane.showMessageDialog(this,
                        "Receipt saved to:\n" + file.getAbsolutePath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Could not save: " + ex.getMessage());
            }
        }
    }
}
