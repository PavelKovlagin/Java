package ru.currencycollection.currenties;

import java.io.Serializable;
import java.util.ArrayList;

public class CurrencyCollection implements Serializable {

    private ArrayList<Currency> currencyCollections = new ArrayList<Currency>();

    public boolean addCurrency(Currency currency) {
        if (currency.getName().length() == 3 )
        {
            for (Currency curColl : currencyCollections) {
                if (curColl.getName().equals(currency.getName())) {
                    curColl.setValue(currency.getValue());
                    return true; //валюта обновлена
                }
            }
            currencyCollections.add(currency);
            return  true; //Добавлена н новая валюта
        }
        else
            return false; //Валюта не три символа
    }

    public ArrayList<Currency> getCurrencyCollections() {
        return  currencyCollections;
    }

    public String getCurrencyCollectionsString() {
        String collection = "";
        for (Currency curCol : currencyCollections) {
            collection = collection + curCol.getName() + " " + curCol.getValue() + "\n";
        }
        return collection;
    }
}
