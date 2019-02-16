package ru.currencycollection.currenties;

import java.io.Serializable;

public class Currency implements Serializable {

    private String name;
    private double value;

    public Currency (String name, double value, boolean increace) {
        this.name = name;
        if (increace) {
            this.value = value;
        } else {
            this.value = -value;
        }

    }

    public String getName() {
        return  this.name;
    }

    public double getValue() {
        return  this.value;
    }

    public void setValue(double value) {
        this.value += value;
    }
}
