package ru.model;

import ru.other.libFormat;

public class classAutoModel {

    private int id_class;
    private String className;
    private String prim;

    public classAutoModel() {

    }

    public classAutoModel(String className, String prim) {
        this.className = className;
        this.prim = prim;
    }

    public int getId_class() {
        return id_class;
    }

    public String getClassName() {
        return className;
    }

    public String getPrim() {
        return prim;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setId_class(int id_class) {
        this.id_class = id_class;
    }

    public void setPrim(String prim) {
        this.prim = prim;
    }

    @Override
    public String toString()
    {
        return libFormat.format(String.valueOf(id_class), 3) + "|" + libFormat.format(className, 20) + "|" + libFormat.format(prim, 100);
    }
}
