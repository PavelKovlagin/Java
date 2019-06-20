package com.goodscalculator.Cabinet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.goodscalculator.ProductList.ProductsCollection;
import com.goodscalculator.R;
import com.goodscalculator.ProductList.ProductListAct;
import com.goodscalculator.Servers.Server;

import java.util.ArrayList;

public class CabinetAct extends AppCompatActivity implements View.OnClickListener {

    private PurchaseCollection purchaseCollection = new PurchaseCollection();
    private User user;
    private PurchaseAdapter purchaseAdapter;
    private RecyclerView recyclerViewPurchase;
    private Button btnPromotionsAct, btnProductListAct;
    private TextView txtUserName;
    private String host, loginLink, purchasesLink;
    private Intent authorizationAct, registrationAct, productListAct;

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
            txtUserName.setText("UserName");
            Log.e("CabinetAct, loadUser", "false");
            dialogAuthorizationAndRegistration();
        }
    }

    private void displayRecyclerViewPurchase() {
        purchaseAdapter = new CabinetAct.PurchaseAdapter(purchaseCollection.getPurchaseCollection());
        recyclerViewPurchase.setAdapter(purchaseAdapter);
    }

    private class PurchaseAdapter extends RecyclerView.Adapter<CabinetAct.PurchaseHolder> {
        private ArrayList<Purchase> purchases;

        public PurchaseAdapter(ArrayList<Purchase> purchaseCollection) {
            this.purchases = purchaseCollection;
        }

        @NonNull
        @Override
        public CabinetAct.PurchaseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.list_item_purchase, viewGroup, false);
            return new CabinetAct.PurchaseHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CabinetAct.PurchaseHolder purchaseHolder, int i) {
            Purchase purchase = purchases.get(i);
            purchaseHolder.bindCrime(purchase);
        }

        @Override
        public int getItemCount() {
            return purchases.size();
        }
    }

    private class PurchaseHolder extends RecyclerView.ViewHolder {

        private TextView mServerName;
        private TextView mPurchaseDate;
        private TextView mAmount;
        private ImageButton mBtnPurchaseChange;

        public PurchaseHolder(@NonNull View itemView) {
            super(itemView);
            mServerName = (TextView) itemView.findViewById((R.id.txtRVServerName));
            mPurchaseDate = (TextView) itemView.findViewById(R.id.txtRVPurchaseDate);
            mAmount = (TextView) itemView.findViewById(R.id.txtRVAmount);
            mBtnPurchaseChange = (ImageButton) itemView.findViewById(R.id.btnRVPurchaseChange);

        }

        public void bindCrime(final Purchase purchase) {

            mServerName.setText(purchase.getServerName());
            mPurchaseDate.setText(purchase.getPurchaseDate());
            mAmount.setText(String.valueOf(purchase.getAmount()));
            mBtnPurchaseChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Server server = new Server();
                    ProductsCollection productsCollection = new ProductsCollection();
                    if (productsCollection.getPurchaseProductsFromDB(host, getString(R.string.purchaseProductsLink), purchase.getId_purchase()) &&
                    server.getServerFromDB(host, getString(R.string.serverLink), purchase.getId_server())) {
                        saveServer(server);
                        saveProductsCollection(productsCollection);
                        startActivity(productListAct);
                        finish();
                    }

                    Log.i("CabinetAct, purchase", purchase.toString());
                }
            });
        }
    }

    private void saveServer(Server server) {
        server.saveServerToFile(this);
    }

    private void saveProductsCollection(ProductsCollection productsCollection) {
        productsCollection.saveProductsCollectionToFile(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cabinet);

        recyclerViewPurchase = (RecyclerView) findViewById(R.id.recyclerViewPurchases);
        recyclerViewPurchase.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        txtUserName = (TextView) findViewById(R.id.textUserName);
        btnProductListAct = (Button) findViewById(R.id.btnProductsListAct);
        btnProductListAct.setOnClickListener(this);
        btnPromotionsAct = (Button) findViewById(R.id.btnPromotionsAct);
        btnPromotionsAct.setOnClickListener(this);
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
        productListAct = new Intent(this, ProductListAct.class);
        host = getString(R.string.host);
        loginLink = getString(R.string.loginLink);
        purchasesLink = getString(R.string.purchasesLink);

        loadUser();
        if (user.doLogin(host, loginLink)) {
            purchaseCollection.getPurchasesFromDB(host, purchasesLink, user.getId_user());
            displayRecyclerViewPurchase();
        } else {
            Toast.makeText(this, "Нет  пользователя", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPromotionsAct:
                startActivity(productListAct);
                super.finish();
                break;
            case R.id.btnProductsListAct:
                startActivity(new Intent(this, ProductListAct.class));
                super.finish();
                break;
        }
    }
}
