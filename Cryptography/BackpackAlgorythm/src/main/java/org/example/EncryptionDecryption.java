package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.math.BigInteger;

public class EncryptionDecryption extends JFrame {
    private JPanel panel1;
    private JLabel ChooseWhatToDo;
    private JButton szyfrowaniePlikButton;
    private JButton deszyfrowaniePlikButton;
    private JButton wygenerujKluczeButton;
    private JTextField prywatnyTextField;
    private JTextField publicznyTextField;

    private Plecakowy plecakowy;

    public EncryptionDecryption() {

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

                        plecakowy.encrypt(fileBytes);
                        BigInteger[] cipherText = plecakowy.cipherText;

                        FileOutputStream fileOutputStream = new FileOutputStream(selectedFile);
                        String s = "";
                        for (int i = 0; i < cipherText.length; i++) {
                            s += cipherText[i] + "\n";
                        }
                        fileOutputStream.write(s.getBytes());
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
                    try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))){
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line);
                            stringBuilder.append("\n");
                        }
                        String fileContent = stringBuilder.toString();
                        String[] wiersze = fileContent.split("\n");
                        BigInteger[] cipher = new BigInteger[wiersze.length];
                        for (int i = 0; i < wiersze.length; i++) {
                            cipher[i] = new BigInteger(wiersze[i]);
                        }
                        plecakowy.decrypt(cipher);
                        byte[] plainText = plecakowy.plainText;

                        FileOutputStream fileOutputStream = new FileOutputStream(selectedFile);
                        fileOutputStream.write(plainText);
                        fileOutputStream.close();

                        JOptionPane.showMessageDialog(null, "Plik odszyfrowany pomyślnie.",
                                "Sukces", JOptionPane.INFORMATION_MESSAGE);

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        wygenerujKluczeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    plecakowy = new Plecakowy();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                prywatnyTextField.setText("Prywatny: " + Plecakowy.BIToHexString(plecakowy.privateKey));
                publicznyTextField.setText("Publiczny: " + Plecakowy.BIToHexString(plecakowy.publicKey));
            }
        });
    }

    public static void main(String[] args) {
        EncryptionDecryption ED = new EncryptionDecryption();
        ED.setContentPane(ED.panel1);
        ED.setTitle("Plecakowy");
        ED.setBounds(600, 200, 600, 300);
        ED.setVisible(true);
        ED.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

