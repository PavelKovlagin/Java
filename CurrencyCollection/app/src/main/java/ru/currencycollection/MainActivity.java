package ru.currencycollection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtFileName, txtName, txtValue, txtCurrency;
    Button btnSave, btnLoad, btnIncreace, btnReduce, btnClear, btnUpd, btnClearCollection;
    CurrencyCollection currencyCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currencyCollection = new CurrencyCollection();

        txtFileName = (TextView) findViewById(R.id.txtFileName);
        txtCurrency = (TextView) findViewById(R.id.txtCurrency);
        txtName = (TextView) findViewById(R.id.txtName);
        txtValue = (TextView) findViewById(R.id.txtValue);

        btnLoad = (Button) findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener(this);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        btnIncreace = (Button) findViewById(R.id.btnIncreace);
        btnIncreace.setOnClickListener(this);
        btnReduce = (Button) findViewById(R.id.btnReduce);
        btnReduce.setOnClickListener(this);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);
        btnUpd = (Button) findViewById(R.id.btnUpdate);
        btnUpd.setOnClickListener(this);
        btnClearCollection = (Button) findViewById(R.id.btnClearCollection);
        btnClearCollection.setOnClickListener(this);
    }

    public void updateColllection() {
        if (currencyCollection != null) {
            txtCurrency.setText(currencyCollection.getCurrencyCollectionsString());
            Toast.makeText(this, "Коллекция обновлена", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Коллекция пуста", Toast.LENGTH_SHORT).show();
        }
    }

    public void setValue(boolean increasce) {
        try {
            if (currencyCollection == null) currencyCollection = new CurrencyCollection();
            int var = currencyCollection.addCurrency(new Currency(txtName.getText().toString(), Double.parseDouble(txtValue.getText().toString())), increasce);

            switch (var) {
                case 0:
                    Toast.makeText(this, "Ошибка добавления валюты", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(this, "Добавлена новая валюта", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    if (increasce)
                        Toast.makeText(this, "Значение валюты увеличено", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(this, "Значение валюты уменьшено", Toast.LENGTH_SHORT).show();
                    break;
            }
            txtCurrency.setText(currencyCollection.getCurrencyCollectionsString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Неверный формат числа", Toast.LENGTH_SHORT).show();
        }
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
        }
    }
}
