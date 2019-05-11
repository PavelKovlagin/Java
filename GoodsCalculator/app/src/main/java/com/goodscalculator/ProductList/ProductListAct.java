package com.goodscalculator.ProductList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.goodscalculator.Cabinet.CabinetAct;
import com.goodscalculator.R;
import com.goodscalculator.Promotions.PromotionsAct;

public class ProductListAct extends AppCompatActivity implements View.OnClickListener {

    private ProductsCollection productsCollection = new ProductsCollection();
    private Button btnCabinetAct, btnPromotionAct, btnProductAdd, btnAmountLimiter, btnProductDelete;
    private TextView textProductsCollection, textTotalAmount;
    private EditText editAmountLimiter;
    private int amountLim = 0;
    private String host;
    private SharedPreferences sPref;

    private void checkAmountLimit() {
        double totalAmount = productsCollection.countingTotalAmount();
        amountLim = Integer.parseInt(String.valueOf(editAmountLimiter.getText()));
        if ((amountLim > 0) && (amountLim < totalAmount)) {
            textTotalAmount.setText("Общая сумма: " + String.valueOf(totalAmount));
            textTotalAmount.setTextColor(Color.RED);
        } else {
            textTotalAmount.setText("Общая сумма: " + String.valueOf(totalAmount));
            textTotalAmount.setTextColor(Color.BLACK);
        }
    }

    public void scanBar(boolean select) {
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
            if (select) {
                startActivityForResult(intent, 0);
            } else {
                startActivityForResult(intent, 1);
            }
        } catch (ActivityNotFoundException anfe) {
            dialogDownloadScanner(this, "Сканер не найден", "Установить сканер с Play Market?", "Да", "Нет").show();
        }
    }

    private void addProduct(String bar_code) {
        if (productsCollection.addProduct(host, bar_code)) {
            Toast.makeText(getApplicationContext(), "Товар добавлен в список", Toast.LENGTH_SHORT).show();
            textProductsCollection.setText(productsCollection.getProductCollectionString());
            Log.i("GSON", productsCollection.getJSONfromProductsCollection());
            checkAmountLimit();
            saveProductsCollection();
        } else {
            Toast.makeText(getApplicationContext(), "Ошибка добавления товара", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteProduct(String bar_code) {
        if (productsCollection.deleteProduct(bar_code)) {
            Toast.makeText(getApplicationContext(), "Товар удален из списка", Toast.LENGTH_SHORT).show();
            textProductsCollection.setText(productsCollection.getProductCollectionString());
            checkAmountLimit();
            saveProductsCollection();
        } else {
            Toast.makeText(getApplicationContext(), "Ошибка удаления товара", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveProductsCollection() {
        sPref = getPreferences(MODE_PRIVATE);
        Editor ed = sPref.edit();
        String productsCollectionJSON = productsCollection.getJSONfromProductsCollection();
        ed.putString("ProductsCollection", productsCollectionJSON);
        ed.commit();
        Log.i("GSON saved", productsCollectionJSON);
    }

    private void loadProductsCollection() {
        sPref = getPreferences(MODE_PRIVATE);
        String productsCollectionJSON = sPref.getString("ProductsCollection", "[]");
        if (productsCollectionJSON.equals("[]")) {
            Log.i("GSON load", "false load");
        } else {
            productsCollection.getProductsCollectionFromJSON(productsCollectionJSON);
            Log.i("GSON load", productsCollectionJSON);
        }
    }

    public AlertDialog dialogDownloadScanner(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return downloadDialog.show();
    }

    private void dialogProduct(final boolean select) {
        final String[] dialogAddProduct = {"По штрих коду", "Сканировать", "Отмена"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (select) builder.setTitle("Добавить");
        else builder.setTitle("Удалить");

        builder.setItems(dialogAddProduct, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (dialogAddProduct[which]) {
                    case "По штрих коду":
                        dialogBarcode(select);
                        break;
                    case "Сканировать":
                        scanBar(select);
                        break;
                    case "Отмена":
                        dialog.cancel();
                        break;
                }
            }
        });
        builder.show();
    }

    private void dialogBarcode(final boolean select) {

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

    private void dialogAmountLimiter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ограничитель суммы");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("Принять", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    amountLim = Integer.parseInt(input.getText().toString());
                    editAmountLimiter.setText(String.valueOf(amountLim));
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
        Log.i("LifeCycle", "onCreate");
        setContentView(R.layout.activity_productlist);

        btnCabinetAct = (Button) findViewById(R.id.btnCabinetAct);
        btnCabinetAct.setOnClickListener(this);
        btnPromotionAct = (Button) findViewById(R.id.btnPromotionsAct);
        btnPromotionAct.setOnClickListener(this);
        btnAmountLimiter = (Button) findViewById(R.id.btnAmountLimiter);
        btnAmountLimiter.setOnClickListener(this);
        btnProductAdd = (Button) findViewById(R.id.btnProductAdd);
        btnProductAdd.setOnClickListener(this);
        btnProductDelete = (Button) findViewById(R.id.btnProductDelete);
        btnProductDelete.setOnClickListener(this);

        textTotalAmount = (TextView) findViewById((R.id.textTotalAmount));
        textProductsCollection = (TextView) findViewById(R.id.textProductCollection);
        editAmountLimiter = (EditText) findViewById(R.id.editAmountLimiter);
        editAmountLimiter.setText("0");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        String bar_code = "";
        try {
            Log.i("LifeCycle", "onActivityResult");
            switch (requestCode) {
                case 0:
                    bar_code = data.getStringExtra("SCAN_RESULT");
                    Log.i("requestCode", "addProduct");
                    addProduct(bar_code);
                    break;
                case 1:
                    bar_code = data.getStringExtra("SCAN_RESULT");
                    Log.i("requestCode", "deleteProduct");
                    deleteProduct(bar_code);
                    break;
            }
        } catch (Exception e) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("LifeCycle","onCreateOptionsMenu");
        menu.add(0, 1, 2, "Очистить список");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("LifeCycle", "onOptionsItemSelected");
        switch (item.getItemId()) {
            case 1:
                productsCollection.clearCollection();
                textProductsCollection.setText(productsCollection.getProductCollectionString());
                Toast.makeText(this, "Список очищен", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        Log.i("LifeCycle","onResume");
        super.onResume();
        checkAmountLimit();
        host = getString(R.string.host);
        Log.i("Host", getString(R.string.host));
        Log.i("App_name", getResources().getString(R.string.app_name));
        loadProductsCollection();
        textProductsCollection.setText(productsCollection.getProductCollectionString());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("LifeCycle", "onStart");
    }

    @Override
    protected void onStop() {
        saveProductsCollection();
        Log.i("LifeCycle","onStop");
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        Log.i("LifeCycle","onClick");
        switch (v.getId()) {
            case R.id.btnCabinetAct:
                startActivity(new Intent(this, CabinetAct.class));
                break;
            case R.id.btnPromotionsAct:
                startActivity(new Intent(this, PromotionsAct.class));
                break;
            case R.id.btnAmountLimiter:
                dialogAmountLimiter();
                break;
            case R.id.btnProductAdd:
                dialogProduct(true);
                break;
            case R.id.btnProductDelete:
                dialogProduct(false);
                break;
        }
    }
}
