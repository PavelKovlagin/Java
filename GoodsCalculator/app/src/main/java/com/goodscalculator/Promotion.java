package com.goodscalculator;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Promotion extends Activity implements View.OnClickListener {

    Button btnActCabinet, btnActProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        btnActCabinet = (Button) findViewById(R.id.btnActCabinet); btnActCabinet.setOnClickListener(this);
        btnActProductList = (Button) findViewById(R.id.btnActProductList); btnActProductList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnActCabinet:
                startActivity(new Intent(this, Cabinet.class));
                break;
            case R.id.btnActProductList:
                startActivity(new Intent(this, ProductList.class));
                break;
        }
    }
}
