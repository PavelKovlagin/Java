package com.goodscalculator.Cabinet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.goodscalculator.R;
import com.goodscalculator.ProductList.ProductListAct;
import com.goodscalculator.Promotions.PromotionsAct;

public class CabinetAct extends AppCompatActivity implements View.OnClickListener {

    private Button btnActPromotion, btnActListProduction;
    private TextView txtUserName;
    private User user;
    private String host, loginLink;
    private Intent authorizationAct, registrationAct;

    private void dialogAuthorizationAndRegistration() {
        final String[] dialogAddProduct = {"Авторизоваться", "Зарегистрироваться", "Отмена"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Вы не авторизованы");

        builder.setItems(dialogAddProduct, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (dialogAddProduct[which]) {
                    case "Авторизоваться":
                        startActivity(authorizationAct);
                        break;
                    case "Зарегистрироваться":
                        startActivity(registrationAct);
                        break;
                    case "Отмена":
                        dialog.cancel();
                        break;
                }
            }
        });
        builder.show();
    }

    private void loadUser() {
        user = new User();
        if (user.loadUserFromFile(this) && user.doLogin(host, loginLink)) {
            Log.i("CabinetLogin, loadUser", user.toString());
            txtUserName.setText(user.getLogin());
        } else {
            Log.e("CabinetAct, loadUser", "false");
            dialogAuthorizationAndRegistration();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cabinet);

        txtUserName = (TextView) findViewById(R.id.textUserName);
        btnActListProduction = (Button) findViewById(R.id.btnProductsListAct);
        btnActListProduction.setOnClickListener(this);
        btnActPromotion = (Button) findViewById(R.id.btnPromotionsAct);
        btnActPromotion.setOnClickListener(this);
    }

    @SuppressLint("LongLogTag")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("CabinetAct, LifeCycle", "onCreateOptionsMenu");
        menu.add(0, 0, 2, "Зарегистрироваться");
        menu.add(0, 1, 2, "Авторизоваться");
        menu.add(0, 2, 2, "Выйти");

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("LongLogTag")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("CabinetAct, LifeCycle", "onOptionsItemSelected");
        switch (item.getItemId()) {
            case 0:
                startActivity(registrationAct);
                break;
            case 1:
                startActivity(authorizationAct);
                break;
            case 2:
                user.logOut(this);
                startActivity(authorizationAct);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        authorizationAct = new Intent(this, AuthorizationAct.class);
        registrationAct = new Intent(this, RegistrationAct.class);
        host = getString(R.string.host);
        loginLink = getString(R.string.loginLink);
        loadUser();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPromotionsAct:
                startActivity(new Intent(this, PromotionsAct.class));
                super.finish();
                break;
            case R.id.btnProductsListAct:
                startActivity(new Intent(this, ProductListAct.class));
                super.finish();
                break;
        }
    }
}
