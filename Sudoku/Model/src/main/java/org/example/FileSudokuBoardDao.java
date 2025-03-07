package org.example;

//import org.example.exceptions.FileException;
//
//import java.io.*;

//public class FileSudokuBoardDao implements Dao<SudokuBoard>, AutoCloseable {
//    private String fileName;
//    private ObjectInputStream ois;
//    private ObjectOutputStream oos;
//
//    public FileSudokuBoardDao(String fileName) {
//        this.fileName = fileName;
//        this.ois = null;
//        this.oos = null;
//    }
//
//    public void setFileName(String fileName) {
//        this.fileName = fileName;
//    }
//
//    @Override
//    public SudokuBoard read() throws FileException {
//        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
//            this.ois = ois;
//            return (SudokuBoard) ois.readObject();
//        } catch (IOException | ClassNotFoundException e) {
//            throw new FileException(e);
//        } finally {
//            try {
//                ois.close();
//            } catch (IOException e) {
//                throw new FileException(e);
//            }
//        }
//    }
//
//    @Override
//    public void write(SudokuBoard obj) throws FileException {
//        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
//            this.oos = oos;
//            oos.writeObject(obj);
//        } catch (IOException e) {
//            try {
//                throw new FileException(e);
//            } catch (FileException ex) {
//                throw new RuntimeException(ex);
//            }
//        } finally {
//            try {
//                oos.close();
//            } catch (IOException e) {
//                throw new FileException(e);
//            }
//        }
//    }
//
//    @Override
//    public void close() throws IOException {
//        if (ois != null) {
//            ois.close();
//        }
//        if (oos != null) {
//            oos.close();
//        }
//    }
//}
