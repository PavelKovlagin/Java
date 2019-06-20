package com.goodscalculator.Cabinet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Purchase {

    private int id_purchase;
    private int id_user;
    private int id_server;
    private String serverName;
    private Date purchaseDate;
    private double amount;

    public int getId_purchase() {
        return id_purchase;
    }

    public int getId_server() {
        return id_server;
    }

    public String getServerName() {
        return serverName;
    }

    public String getPurchaseDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");;
        return dateFormat.format(this.purchaseDate);
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Покупка №" + this.id_purchase +
                ", идентификатор пользователя: " + this.id_user +
                ", идентификатор сервера: " + this.id_server +
                ", название сервера: " + this.serverName +
                ", дата покупки: " + this.purchaseDate + ", сумма: " + this.amount;
    }
}
