package com.goodscalculator.ProductList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.goodscalculator.Cabinet.CabinetAct;
import com.goodscalculator.R;
import com.goodscalculator.Promotions.PromotionAct;

public class ProductListAct extends Activity implements View.OnClickListener {

    ProductsCollection productsCollection = new ProductsCollection();

    Button btnActCabinet, btnActPromotion, btnProductAdd, btnAmountLimiter, btnProductDelete;
    TextView textAmountLimiter, textProductsCollection, textTotalAmount;
    EditText editAmountLimiter;

    private Integer amountLim = 0;

    private String host;

    private  void checkAmountLimit() {
        double totalAmount = productsCollection.countingTotalAmount();
        amountLim = Integer.parseInt(String.valueOf(editAmountLimiter.getText()));
        if ((amountLim > 0) && (amountLim < totalAmount)) {
            textTotalAmount.setText("Общая сумма:" + String.valueOf(totalAmount));
            textTotalAmount.setTextColor(Color.RED);
        } else {
            textTotalAmount.setText("Общая сумма:" + String.valueOf(totalAmount));
            textTotalAmount.setTextColor(Color.BLACK);
        }
    }

    private void addProduct(String bar_code) {
        if (productsCollection.addProduct(host, bar_code)) {
            Toast.makeText(getApplicationContext(), "Товар добавлен в список", Toast.LENGTH_SHORT).show();
            textProductsCollection.setText(productsCollection.getProductCollectionString());
            Log.i("GSON", productsCollection.getJSONfromProductsCollection());
            checkAmountLimit();
        } else {
            Toast.makeText(getApplicationContext(), "Ошибка добавления товара", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteProduct(String bar_code) {
        if (productsCollection.deleteProduct(bar_code)) {
            Toast.makeText(getApplicationContext(), "Товар удален из списка", Toast.LENGTH_SHORT).show();
            textProductsCollection.setText(productsCollection.getProductCollectionString());
            checkAmountLimit();
        } else {
            Toast.makeText(getApplicationContext(), "Ошибка удаления товара", Toast.LENGTH_SHORT).show();
        }
    }

    SharedPreferences sPref;

    public void saveProductsCollection() {
        sPref = getPreferences(MODE_PRIVATE);
        Editor ed = sPref.edit();
        String productsCollectionJSON = productsCollection.getJSONfromProductsCollection();
        ed.putString("ProductsCollection", productsCollectionJSON);
        ed.commit();
        Log.i("GSON saved", productsCollectionJSON);
    }

    public void loadProductsCollection() {
        sPref = getPreferences(MODE_PRIVATE);
        String productsCollectionJSON = sPref.getString("ProductsCollection", "[]");
        if (productsCollectionJSON.equals("[]")) {
            Log.i("GSON load", "false load");
        } else {
            productsCollection.getProductsCollectionFromJSON(productsCollectionJSON);
            Log.i("GSON load", productsCollectionJSON);
        }
    }

    public void dialogAddProduct(final boolean select) {
        final String[] dialogAddProduct = {"По штрих коду", "Сканировать", "Отмена"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (select) builder.setTitle("Добавить");
        else builder.setTitle("Удалить");

        builder.setItems(dialogAddProduct, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (dialogAddProduct[which]){
                    case "По штрих коду":
                        dialogBarcode(select);
                        break;
                    case "Сканировать":
                        Toast.makeText(getApplicationContext(), "Здесь будет сканирование", Toast.LENGTH_SHORT).show();
                        break;
                    case "Отмена":
                        dialog.cancel();
                        break;
                }
            }
        });
        builder.show();
    }

    public void dialogBarcode(final boolean select) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Введите штрих код");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setView(input);

        builder.setPositiveButton("Принять", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String bar_code = input.getText().toString();
                if (select) addProduct(bar_code);
                else deleteProduct(bar_code);

            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void dialogAmountLimiter() {
        AlertDialog.Builder  builder =new  AlertDialog.Builder(this);
        builder.setTitle("Ограничитель суммы");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("Принять", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    amountLim = Integer.parseInt(input.getText().toString());
                    editAmountLimiter.setText(amountLim.toString());
                    checkAmountLimit();
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Слишком большое число", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("LifeCycle", "Create");
        setContentView(R.layout.activity_productlist);

        btnActCabinet = (Button) findViewById(R.id.btnActCabinet); btnActCabinet.setOnClickListener(this);
        btnActPromotion = (Button) findViewById(R.id.btnActPromotion); btnActPromotion.setOnClickListener(this);
        btnAmountLimiter = (Button) findViewById(R.id.btnAmountLimiter); btnAmountLimiter.setOnClickListener(this);
        btnProductAdd = (Button) findViewById(R.id.btnProductAdd); btnProductAdd.setOnClickListener(this);
        btnProductDelete = (Button) findViewById(R.id.btnProductDelete); btnProductDelete.setOnClickListener(this);

        textTotalAmount = (TextView) findViewById((R.id.textTotalAmount));
        textProductsCollection = (TextView) findViewById(R.id.textProductCollection);
        textAmountLimiter = (TextView) findViewById(R.id.textAmountLimiter);
        editAmountLimiter = (EditText) findViewById(R.id.editAmountLimiter);
        editAmountLimiter.setText("0");
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAmountLimit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        host = getString(R.string.host);
        Log.i("Host", getString(R.string.host));
        Log.i("App_name", getResources().getString(R.string.app_name));
        loadProductsCollection();
        textProductsCollection.setText(productsCollection.getProductCollectionString());
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveProductsCollection();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnActCabinet:
                startActivity(new Intent(this, CabinetAct.class));
                break;
            case R.id.btnActPromotion:
                startActivity(new  Intent(this, PromotionAct.class));
                break;
            case R.id.btnAmountLimiter:
                dialogAmountLimiter();
                break;
            case R.id.btnProductAdd:
                dialogAddProduct(true);
                break;
            case R.id.btnProductDelete:
                dialogAddProduct(false);
                break;
        }
    }
}