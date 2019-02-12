package ru.currencycollection;

import java.io.Serializable;
import java.util.ArrayList;

public class CurrencyCollection implements Serializable {

    private ArrayList<Currency> currencyCollections = new ArrayList<Currency>();

    public int addCurrency(Currency currency, boolean increace) {
        if (currency.getName().length() == 3 )
        {
            for (Currency curColl : currencyCollections) {
                if (curColl.getName().equals(currency.getName())) {
                    if (increace) curColl.increaceValue(currency.getValue());
                    else curColl.reduceValue(currency.getValue());
                    return 2; //валюта обновлена
                }
            }
            currencyCollections.add(currency);
            return  1; //Добавлена н новая валюта
        }
        else
            return 0; //Валюта не три символа
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
