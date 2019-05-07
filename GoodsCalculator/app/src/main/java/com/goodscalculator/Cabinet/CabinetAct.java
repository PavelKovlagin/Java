package com.goodscalculator.Cabinet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.goodscalculator.R;
import com.goodscalculator.ProductList.ProductListAct;
import com.goodscalculator.Promotions.PromotionAct;

public class CabinetAct extends Activity implements View.OnClickListener {

    Button btnActPromotion, btnActListProduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cabinet);

        btnActListProduction = (Button) findViewById(R.id.btnActProductList); btnActListProduction.setOnClickListener(this);
        btnActPromotion = (Button) findViewById(R.id.btnActPromotion); btnActPromotion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnActPromotion:
                startActivity(new Intent(this, PromotionAct.class));
                break;
            case R.id.btnActProductList:
                startActivity(new Intent(this, ProductListAct.class));
                break;
        }
    }
}
