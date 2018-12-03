package ru.model;

import ru.other.libFormat;

public class AutoModel {

    public AutoModel() {
    }

    public AutoModel(int id_classAuto, String marka, String model) {
        this.id_classAuto = id_classAuto;
        this.marka = marka;
        this.model = model;
    }

    private int id_auto;
    private int id_classAuto;
    private String classAuto;
    private String marka;
    private String model;

    public void setId_auto(int id_auto) {
        this.id_auto = id_auto;
    }

    public void setId_classAuto(int id_classAuto) {
        this.id_classAuto = id_classAuto;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setClassAuto(String classAuto) {
        this.classAuto = classAuto;
    }

    public int getId_auto() {
        return id_auto;
    }

    public int getId_classAuto() {
        return id_classAuto;
    }

    public String getMarka() {
        return marka;
    }

    public String getModel() {
        return model;
    }

    public String getClassAuto() {
        return classAuto;
    }

    @Override
    public String toString()
    {
        return libFormat.format(id_auto, 3) + "|" + libFormat.format(classAuto, 10) + "|" + libFormat.format(marka , 10) + "|" + libFormat.format(model,10) + "|";
    }
}
