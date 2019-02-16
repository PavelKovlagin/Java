package ru.currencycollection.currenties;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ru.currencycollection.R;
import ru.currencycollection.convertor.ActConvertor;

public class ActCurrencies extends AppCompatActivity implements View.OnClickListener {

    TextView txtFileName, txtName, txtValue, txtCurrency;
    Button btnSave, btnLoad, banIncrease, btnReduce, btnClear, btnUpd, btnClearCollection, btnMenuCurrency, btnMenuValues, btnActConvertor;
    CheckBox cbMenu;
    CurrencyCollection currencyCollection;

    final int RUB = 1;
    final int UBD = 2;
    final int GRV = 3;
    final int v100 = 100;
    final int v1000 = 1000;
    final int v10000 = 10000;

    public void updateColllection() {
        if (currencyCollection != null) {
            txtCurrency.setText(currencyCollection.getCurrencyCollectionsString());
        } else {
            Toast.makeText(this, "Коллекция пуста", Toast.LENGTH_SHORT).show();
        }
    }

    public void setValue(boolean increase) {
        try {
            if (currencyCollection == null) currencyCollection = new CurrencyCollection();
            if (!currencyCollection.addCurrency(new Currency(txtName.getText().toString(), Double.parseDouble(txtValue.getText().toString()), increase)))
                Toast.makeText(this, "Валюта не три символа", Toast.LENGTH_SHORT).show();
            txtCurrency.setText(currencyCollection.getCurrencyCollectionsString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Неверный формат числа", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currencyCollection = new CurrencyCollection();

        txtFileName = (TextView) findViewById(R.id.txtFileName);
        txtCurrency = (TextView) findViewById(R.id.txtCurrency);
        txtName = (TextView) findViewById(R.id.txtName);
        txtValue = (TextView) findViewById(R.id.txtValue);

        btnLoad = (Button) findViewById(R.id.btnLoad); btnLoad.setOnClickListener(this);
        btnSave = (Button) findViewById(R.id.btnSave); btnSave.setOnClickListener(this);
        banIncrease = (Button) findViewById(R.id.btnIncreace); banIncrease.setOnClickListener(this);
        btnReduce = (Button) findViewById(R.id.btnReduce); btnReduce.setOnClickListener(this);
        btnClear = (Button) findViewById(R.id.btnClear); btnClear.setOnClickListener(this);
        btnUpd = (Button) findViewById(R.id.btnUpdate); btnUpd.setOnClickListener(this);
        btnClearCollection = (Button) findViewById(R.id.btnClearCollection); btnClearCollection.setOnClickListener(this);
        btnMenuCurrency = (Button) findViewById(R.id.btnMenuCurrency); registerForContextMenu(btnMenuCurrency);
        btnMenuValues = (Button) findViewById(R.id.btnMenuValues); btnMenuValues.setOnCreateContextMenuListener(this);
        btnActConvertor = (Button) findViewById(R.id.btnActConvertor); btnActConvertor.setOnClickListener(this);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()) {
            case R.id.btnMenuCurrency:
                menu.add(0,RUB,0,"RUB");
                menu.add(0,UBD,0,"UBD");
                menu.add(0,GRV,0,"GRV");
                break;
            case R.id.btnMenuValues:
                menu.add(0,v100,0,"100");
                menu.add(0,v1000,0,"1000");
                menu.add(0,v10000,0,"10000");
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case RUB:
                txtName.setText("RUB");
                break;
            case UBD:
                txtName.setText("UBD");
                break;
            case GRV:
                txtName.setText("GRV");
                break;
            case v100:
                txtValue.setText("100");
                break;
            case v1000:
                txtValue.setText("1000");
                break;
            case v10000:
                txtValue.setText("10000");
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        String str = "";
        switch (menuItem.getItemId()) {
            case R.id.itemRUB:
                txtValue.setText("100");
                break;
            case R.id.itemUSD:
                txtValue.setText("200");
                break;
            case R.id.itemGRV:
                txtValue.setText("300");
                break;
        }
        Toast.makeText(this, str, Toast.LENGTH_SHORT);

        txtName.setText(menuItem.getTitle());
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("currencyCollection", currencyCollection);
        outState.putString("txtCurrency", (String) txtCurrency.getText());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currencyCollection = (CurrencyCollection) savedInstanceState.getSerializable("currencyCollection");
        txtCurrency.setText(savedInstanceState.getString("txtCurrency"));
    }

    @Override
    public void onClick(View v) {
        boolean bool = true;
        switch (v.getId()) {
            case R.id.btnIncreace:
                setValue(true);
                break;
            case R.id.btnReduce:
                setValue(false);
                break;
            case R.id.btnClear:
                txtCurrency.setText("");
                break;
            case R.id.btnSave:
                try {
                    FileOutputStream fos = openFileOutput(txtFileName.getText().toString(), MODE_PRIVATE);
                    ObjectOutputStream osw = new ObjectOutputStream(fos);
                    osw.writeObject(currencyCollection);
                    osw.flush();
                    osw.close();
                    Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Ошибка сохранения", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Ошибка сохранения", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnLoad:
                try {
                    FileInputStream fin = openFileInput(txtFileName.getText().toString());
                    ObjectInputStream ois = new ObjectInputStream(fin);
                    currencyCollection = (CurrencyCollection) ois.readObject();
                    updateColllection();
                    Toast.makeText(this, "Файл загружен", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    Toast.makeText(this, "Файл не найден", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(this, "Ошибка IOException", Toast.LENGTH_SHORT).show();
                } catch (ClassNotFoundException e) {
                    Toast.makeText(this, "Класс не найден", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnUpdate:
                updateColllection();
                break;
            case R.id.btnClearCollection:
                currencyCollection = null;
                System.gc();
                Toast.makeText(this, "Коллекция очищена", Toast.LENGTH_SHORT).show();
                break;
            case  R.id.btnActConvertor:
                startActivity(new Intent(this, ActConvertor.class));
                break;
        }
    }
}
