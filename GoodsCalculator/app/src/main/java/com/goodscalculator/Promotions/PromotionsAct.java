package com.goodscalculator.Promotions;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.goodscalculator.Cabinet.CabinetAct;
import com.goodscalculator.ProductList.ProductListAct;
import com.goodscalculator.R;

public class PromotionsAct extends Activity implements View.OnClickListener {

    private PromotionsCollection promotionsCollection = new PromotionsCollection();
    private Button btnCabinetAct, btnProductListAct, btnNextPromotion, btnPreviousPromotion;
    private ImageView promotionImage;
    private TextView textPromotionDescription;
    private int promotionIndex = 0;
    private String host;

    private void increasePromotionIndex() {
        if (promotionIndex < promotionsCollection.getSize()-1) {
            promotionIndex++;
        } else {
            promotionIndex = 0;
        }
        Log.i( "PromotionIndex", String.valueOf(promotionIndex));
    }

    private void decreasePromotionIndex() {
        if (promotionIndex > 0) {
            promotionIndex--;
        } else {
            promotionIndex = promotionsCollection.getSize()-1;
        }
        Log.i("PromotionIndex", String.valueOf(promotionIndex));
    }

    private void getPromotion() {
        textPromotionDescription.setText(promotionsCollection.getDescription(promotionIndex));
        promotionImage.setImageBitmap(promotionsCollection.getImage(promotionIndex));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);

        btnCabinetAct = (Button) findViewById(R.id.btnCabinetAct);
        btnCabinetAct.setOnClickListener(this);
        btnProductListAct = (Button) findViewById(R.id.btnProductsListAct);
        btnProductListAct.setOnClickListener(this);
        promotionImage = (ImageView) findViewById(R.id.imageView);
        promotionImage.setOnClickListener(this);
        btnNextPromotion = (Button) findViewById(R.id.btnNextPromotion);
        btnNextPromotion.setOnClickListener(this);
        btnPreviousPromotion = (Button) findViewById(R.id.btnPreviousPromotion);
        btnPreviousPromotion.setOnClickListener(this);
        textPromotionDescription = (TextView) findViewById(R.id.textPromotionDescription);
    }

    @Override
    protected void onStart() {
        super.onStart();
        host = getString(R.string.host);
        if (promotionsCollection.addPromotions(host)) {
            getPromotion();
        } else {
            Toast.makeText(getApplicationContext(), "Ошибка загрузки акций", Toast.LENGTH_SHORT).show();
        }
        Log.i("PromotionsString", promotionsCollection.getPromotionCollectionString());
        Log.i("PromotionSize", String.valueOf(promotionsCollection.getSize()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCabinetAct:
                startActivity(new Intent(this, CabinetAct.class));
                break;
            case R.id.btnProductsListAct:
                startActivity(new Intent(this, ProductListAct.class));
                break;
            case R.id.imageView:
                String promotionURL = promotionsCollection.getPromotionURL(promotionIndex);
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(promotionURL)));
                } catch (Exception e) {
                    Log.i("ProductionURL", "false");
                    Toast.makeText(getApplicationContext(), "Ошибка загрузки ссылки", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnPreviousPromotion:
                decreasePromotionIndex();
                getPromotion();
                break;
            case R.id.btnNextPromotion:
                increasePromotionIndex();
                getPromotion();
                break;
        }
    }
}
