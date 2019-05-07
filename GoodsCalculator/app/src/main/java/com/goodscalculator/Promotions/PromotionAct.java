package com.goodscalculator.Promotions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.goodscalculator.Cabinet.CabinetAct;
import com.goodscalculator.DownloadImage;
import com.goodscalculator.ProductList.ProductListAct;
import com.goodscalculator.R;

import java.util.concurrent.ExecutionException;

public class PromotionAct extends Activity implements View.OnClickListener {

    Button btnActCabinet, btnActProductList;
    ImageView imageView;

    PromotionsCollection promotionsCollection = new PromotionsCollection();

    private int imageIndex = 0;
    private String host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        btnActCabinet = (Button) findViewById(R.id.btnActCabinet);
        btnActCabinet.setOnClickListener(this);
        btnActProductList = (Button) findViewById(R.id.btnActProductList);
        btnActProductList.setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        host = getString(R.string.host);
        if (promotionsCollection.addPromotions(host)) {
            imageView.setImageBitmap(promotionsCollection.getImage(imageIndex));
        } else {
            Toast.makeText(getApplicationContext(), "Ошибка загрузки акций", Toast.LENGTH_SHORT).show();
        }
        Log.i("PromotionsString", promotionsCollection.getPromotionCollectionString());
        Log.i("PromotionSize", String.valueOf(promotionsCollection.getSize()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnActCabinet:
                startActivity(new Intent(this, CabinetAct.class));
                break;
            case R.id.btnActProductList:
                startActivity(new Intent(this, ProductListAct.class));
                break;
            case R.id.imageView:
                if (imageIndex < promotionsCollection.getSize()) {
                    imageView.setImageBitmap(promotionsCollection.getImage(imageIndex));
                    imageIndex++;
                } else {
                    imageIndex = 0;
                    imageView.setImageBitmap(promotionsCollection.getImage(imageIndex));
                    imageIndex++;
                }
                break;
        }
    }
}
