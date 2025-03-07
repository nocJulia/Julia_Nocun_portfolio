package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class EncryptionDecryption extends JFrame {
    private JPanel panel1;
    private JLabel ChooseWhatToDo;
    private JButton szyfrowanieButton;
    private JButton deszyfrowanieButton;
    private JButton szyfrowaniePlikButton;
    private JButton deszyfrowaniePlikButton;

    public EncryptionDecryption() {
        szyfrowanieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = JOptionPane.showInputDialog(null, "Proszę podać wiadomość do zaszyfrowania:");
                String[] keys = checkKeys();
                DES des = new DES();
                des.encrypt(message.getBytes(), keys[0], keys[1], keys[2]);
                String encryptedMessage = DES.byteArrayToHex(des.getCiphertextBytes());

                JTextArea textArea = new JTextArea(10, 30);
                textArea.setText(encryptedMessage);
                textArea.setEditable(false);

                JOptionPane.showMessageDialog(null, new JScrollPane(textArea),
                        "Oto zaszyfrowana wiadomość:", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        szyfrowaniePlikButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        FileInputStream fileInputStream = new FileInputStream(selectedFile);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = fileInputStream.read(buffer)) != -1) { // pętla kończy się gdy .read() zwróci -1 czyli gdy będzie koniec pliku
                            byteArrayOutputStream.write(buffer, 0, bytesRead); // w bytesRead zapisujamy liczbę odczytanych bajtów, w buforze odczytane bajty
                        }
                        byte[] fileBytes = byteArrayOutputStream.toByteArray();
                        byteArrayOutputStream.close();
                        fileInputStream.close();

                        String[] keys = checkKeys();
                        DES des = new DES();
                        des.encrypt(fileBytes, keys[0], keys[1], keys[2]);
                        byte[] encryptedBytes = des.getCiphertextBytes();

                        FileOutputStream fileOutputStream = new FileOutputStream(selectedFile);
                        fileOutputStream.write(encryptedBytes);
                        fileOutputStream.close();

                        JOptionPane.showMessageDialog(null, "Plik zaszyfrowany pomyślnie.",
                                "Sukces", JOptionPane.INFORMATION_MESSAGE);

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });



        deszyfrowaniePlikButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        FileInputStream fileInputStream = new FileInputStream(selectedFile);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                            byteArrayOutputStream.write(buffer, 0, bytesRead);
                        }
                        byte[] fileBytes = byteArrayOutputStream.toByteArray();
                        byteArrayOutputStream.close();
                        fileInputStream.close();

                        String[] keys = checkKeys();
                        DES des = new DES();
                        des.decrypt(fileBytes, keys[0], keys[1], keys[2]);
                        byte[] decryptedBytes = des.getBytes();

                        FileOutputStream fileOutputStream = new FileOutputStream(selectedFile);
                        fileOutputStream.write(decryptedBytes);
                        fileOutputStream.close();

                        JOptionPane.showMessageDialog(null, "Plik odszyfrowany pomyślnie.",
                                "Sukces", JOptionPane.INFORMATION_MESSAGE);

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });



        deszyfrowanieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = JOptionPane.showInputDialog(null, "Proszę podać wiadomość do odszyfrowania:");
                String[] keys = checkKeys();
                DES des = new DES();
                byte[] messageBytes = DES.hexStringToByteArray(message);
                des.decrypt(messageBytes, keys[0], keys[1], keys[2]);
                String decryptedMessage = des.getPlainText();

                JTextArea textArea = new JTextArea(10, 30);
                textArea.setText(decryptedMessage);
                textArea.setEditable(false);

                JOptionPane.showMessageDialog(null, new JScrollPane(textArea),
                        "Oto odszyfrowana wiadomość:", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        EncryptionDecryption ED = new EncryptionDecryption();
        ED.setContentPane(ED.panel1);
        ED.setTitle("DES-X");
        ED.setBounds(600, 200, 400, 200);
        ED.setVisible(true);
        ED.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public String[] checkKeys() {
        String key1;
        do {
            key1 = JOptionPane.showInputDialog(null, "Proszę podać klucz główny (16 znaków):");
        }
        while (key1 == null || key1.length() != 16);
        String key2;
        do {
            key2 = JOptionPane.showInputDialog(null, "Proszę podać pierwszy klucz dodatkowy (16 znaków):");
        }
        while (key2 == null || key2.length() != 16);
        String key3;
        do {
            key3 = JOptionPane.showInputDialog(null, "Proszę podać drugi klucz dodatkowy (16 znaków):");
        }
        while (key3 == null || key3.length() != 16);
        return new String[]{key1, key2, key3};
    }


}

