package ru.jdbc;


public interface DAO<T> {
    int INSERT(T model);
    T SELECT();
    boolean UPDATE(T model);
    boolean DELETE(int i);
}
