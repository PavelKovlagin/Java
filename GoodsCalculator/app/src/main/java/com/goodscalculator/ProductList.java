package com.goodscalculator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProductList extends Activity implements View.OnClickListener {

    ListProducts listProducts = new ListProducts();

    Button btnActCabinet, btnActPromotion, btnProductAdd, btnAmountLimiter;
    TextView textAmountLimiter;
    EditText editAmountLimiter;

    String Barcode;
    public void dialogAddProduct() {
        final String[] dialogAddProduct = {"По штрих коду", "Сканировать", "Отмена"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Добавить");

        builder.setItems(dialogAddProduct, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (dialogAddProduct[which]){
                    case "По штрих коду":
                        dialogBarcode();
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

    public void dialogBarcode() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Введите штрих код");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setView(input);

        builder.setPositiveButton("Принять", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Barcode = input.getText().toString();
                Toast.makeText(getApplicationContext(), listProducts.addProduct(Barcode), Toast.LENGTH_SHORT).show();
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

    private Integer amountLim;
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
        setContentView(R.layout.activity_productlist);

        btnActCabinet = (Button) findViewById(R.id.btnActCabinet); btnActCabinet.setOnClickListener(this);
        btnActPromotion = (Button) findViewById(R.id.btnActPromotion); btnActPromotion.setOnClickListener(this);
        btnAmountLimiter = (Button) findViewById(R.id.btnAmountLimiter); btnAmountLimiter.setOnClickListener(this);
        btnProductAdd = (Button) findViewById(R.id.btnProductAdd); btnProductAdd.setOnClickListener(this);

        textAmountLimiter = (TextView) findViewById(R.id.textAmountLimiter);
        editAmountLimiter = (EditText) findViewById(R.id.editAmountLimiter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnActCabinet:
                startActivity(new Intent(this, Cabinet.class));
                break;
            case R.id.btnActPromotion:
                startActivity(new  Intent(this, Promotion.class));
                break;
            case R.id.btnAmountLimiter:
                dialogAmountLimiter();
                break;
            case R.id.btnProductAdd:
                dialogAddProduct();
                break;
        }

    }
}
