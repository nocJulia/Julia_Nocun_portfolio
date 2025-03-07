package org.example;

import org.example.exceptions.DaoException;

public interface Dao<T> extends AutoCloseable {

    T read() throws DaoException;

    void write(T obj) throws DaoException;

    void setFileName(String filename);
}
