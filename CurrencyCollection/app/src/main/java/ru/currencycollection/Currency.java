package ru.currencycollection;

import java.io.Serializable;

public class Currency implements Serializable {

    private String name;
    private double value;

    public Currency (String name, double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return  this.name;
    }

    public double getValue() {
        return  this.value;
    }

    public void increaceValue(double value) {
        this.value += value;
    }

    public void reduceValue(double value) { this.value -= value; }
}
