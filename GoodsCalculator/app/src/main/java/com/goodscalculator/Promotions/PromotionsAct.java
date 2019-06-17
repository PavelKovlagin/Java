package com.goodscalculator.Promotions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.goodscalculator.Cabinet.CabinetAct;
import com.goodscalculator.ProductList.ProductListAct;
import com.goodscalculator.R;
import com.goodscalculator.Servers.Server;
import com.goodscalculator.Servers.ServersAct;

public class PromotionsAct extends AppCompatActivity implements View.OnClickListener {

    private PromotionsCollection promotionsCollection = new PromotionsCollection();
    private Button btnCabinetAct, btnProductListAct, btnNextPromotion, btnPreviousPromotion;
    private ImageView promotionImage;
    private TextView textPromotionDescription, textServerName;
    private int promotionIndex = 0;
    private Server server = new Server();
    private SharedPreferences sPref;

    private void load() {
        if (server.loadServerFromFile(this)) {
            if (promotionsCollection.addPromotions(server.getIp_address(), server.getPromotionsLink())) {
                textServerName.setText("Server: " + server.getName());
                getPromotion();
                btnNextPromotion.setEnabled(true);
                btnPreviousPromotion.setEnabled(true);
                promotionImage.setEnabled(true);
            } else {
                Toast.makeText(getApplicationContext(), "Ошибка загрузки акций", Toast.LENGTH_SHORT).show();
                btnNextPromotion.setEnabled(false);
                btnPreviousPromotion.setEnabled(false);
                textPromotionDescription.setText("");
                promotionImage.setImageBitmap(null);
                promotionImage.setEnabled(false);
            }
            Log.i("ProductServerLoad", server.toString());
        } else {
            Toast.makeText(this, "Ошибка загрузки сервера", Toast.LENGTH_SHORT).show();
            textServerName.setText(getString(R.string.serverNotFound));
        }
    }

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

    @SuppressLint("LongLogTag")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("Promotions, LifeCycle", "onCreateOptionsMenu");;
        menu.add(0, 1, 2, "Выбор сервера");
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("LongLogTag")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("Promotions, LifeCycle", "onOptionsItemSelected");
        switch (item.getItemId()) {
            case 1:
                startActivity(new Intent(this, ServersAct.class));
                break;
        }
        return super.onOptionsItemSelected(item);
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
        textServerName = (TextView) findViewById(R.id.textServerName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCabinetAct:
                startActivity(new Intent(this, CabinetAct.class));
                super.finish();
                break;
            case R.id.btnProductsListAct:
                startActivity(new Intent(this, ProductListAct.class));
                super.finish();
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
