package com.goodscalculator.ProductList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.goodscalculator.Cabinet.AuthorizationAct;
import com.goodscalculator.Cabinet.CabinetAct;
import com.goodscalculator.Cabinet.User;
import com.goodscalculator.R;
import com.goodscalculator.Promotions.PromotionsAct;
import com.goodscalculator.Servers.Server;
import com.goodscalculator.Servers.ServersAct;

import java.util.ArrayList;

public class ProductListAct extends AppCompatActivity implements View.OnClickListener {

    private ProductsCollection productsCollection;
    private Server server;
    private User user;
    private String host, usersLink, insertProdutsLink;
    private Button btnCabinetAct, btnPromotionAct, btnProductAdd, btnAmountLimiter, btnProductDelete;
    private TextView textTotalAmount, textServerName;
    private EditText editAmountLimiter;
    private RecyclerView recyclerViewProductsList;
    private int amountLim = 0;
    private ProductAdapter productAdapter;
    private Intent authorizationAct;

    private class ProductAdapter extends RecyclerView.Adapter<ProductHolder> {
        private ArrayList<Product> products;

        public ProductAdapter(ArrayList<Product> productsCollection) {
            this.products = productsCollection;
        }

        @NonNull
        @Override
        public ProductHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.list_item_product, viewGroup, false);
            return new ProductHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductHolder productHolder, int i) {
            Product product = products.get(i);
            productHolder.bindCrime(product);
        }

        @Override
        public int getItemCount() {
            return products.size();
        }
    }

    private class ProductHolder extends RecyclerView.ViewHolder {

        private TextView mProductNameTextView;
        private TextView mProductPriceTextView;
        private ImageButton mBtnProductDelete;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            mProductNameTextView = (TextView) itemView.findViewById(R.id.txtRVProductName);
            mProductPriceTextView = (TextView) itemView.findViewById(R.id.txtRVProductPrice);
            mBtnProductDelete = (ImageButton) itemView.findViewById(R.id.btnRVProductDelete);
        }

        public void bindCrime(final Product product) {
            mProductNameTextView.setText(product.getName());
            mProductPriceTextView.setText(String.valueOf(product.getPrice()));
            mBtnProductDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogDeleteProduct(product.getBar_code());
                }
            });
        }
    }

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

    private void addProduct(String bar_code) {
        if (productsCollection.addProduct(server.getIp_address(), server.getProductDetailsLink(), bar_code)) {
            Toast.makeText(getApplicationContext(), "Товар добавлен в список", Toast.LENGTH_SHORT).show();
            displayRecyclerViewProductsList();
            Log.i("GSON", productsCollection.getJSONfromProductsCollection());
            checkAmountLimit();
            productsCollection.saveProductsCollectionToFile(this);
        } else {
            Toast.makeText(getApplicationContext(), "Ошибка добавления товара", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteProduct(String bar_code) {
        if (productsCollection.deleteProduct(bar_code)) {
            Toast.makeText(getApplicationContext(), "Товар удален из списка", Toast.LENGTH_SHORT).show();
            displayRecyclerViewProductsList();
            checkAmountLimit();
            productsCollection.saveProductsCollectionToFile(this);
        } else {
            Toast.makeText(getApplicationContext(), "Ошибка удаления товара", Toast.LENGTH_SHORT).show();
        }
    }

    private void scanBar(boolean select) {
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

    private void dialogDeleteProduct(final String bar_code) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Удалить товар?");

        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteProduct(bar_code);
            }
        });
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
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

    private void displayRecyclerViewProductsList() {
        productAdapter = new ProductAdapter(productsCollection.getProductsCollection());
        recyclerViewProductsList.setAdapter(productAdapter);
        if (productsCollection.getSize() > 0)
            recyclerViewProductsList.smoothScrollToPosition(recyclerViewProductsList.getAdapter().getItemCount() - 1);
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ProductListCollection, LifeCycle", "onCreate");
        setContentView(R.layout.activity_productslist);

        recyclerViewProductsList = (RecyclerView) findViewById(R.id.recyclerViewProductsList);
        recyclerViewProductsList.setLayoutManager(new LinearLayoutManager(this));

        btnCabinetAct = (Button) findViewById(R.id.btnCabinetAct);
        btnCabinetAct.setOnClickListener(this);
        btnPromotionAct = (Button) findViewById(R.id.btnPromotionsAct);
        btnPromotionAct.setOnClickListener(this);
        btnAmountLimiter = (Button) findViewById(R.id.btnAmountLimiter);
        btnAmountLimiter.setOnClickListener(this);
        btnProductAdd = (Button) findViewById(R.id.btnProductAdd);
        btnProductAdd.setOnClickListener(this);
        btnProductDelete = (Button) findViewById(R.id.btnRVProductDelete);
        btnProductDelete.setOnClickListener(this);

        textTotalAmount = (TextView) findViewById((R.id.textTotalAmount));
        textServerName = (TextView) findViewById(R.id.textServerName);
        editAmountLimiter = (EditText) findViewById(R.id.editAmountLimiter);
        editAmountLimiter.setText("0");

    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        String bar_code = "";
        try {
            Log.i("ProductListCollection, LifeCycle", "onActivityResult");
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

    @SuppressLint("LongLogTag")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("ProductListCollection, LifeCycle", "onCreateOptionsMenu");
        menu.add(0, 1, 2, "Очистить список");
        menu.add(0, 2, 2, "Выбор сервера");
        menu.add(0, 3, 2, "Сохранить список товаров");
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("LongLogTag")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("ProductListCollection, LifeCycle", "onOptionsItemSelected");
        switch (item.getItemId()) {
            case 1:
                productsCollection.clearCollection();
                displayRecyclerViewProductsList();
                Toast.makeText(this, "Список очищен", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                startActivity(new Intent(this, ServersAct.class));
                break;
            case 3:
                if (user.doLogin(host, usersLink)) {
                    if (productsCollection.getSize() > 0) {
                        productsCollection.savePurchase(host, insertProdutsLink, user.getId_user());
                    } else {
                        Toast.makeText(this, "Список товаров пуст", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    startActivity(authorizationAct);
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("ProductListCollection, LifeCycle", "onResume");
        host = getString(R.string.host);
        usersLink = getString(R.string.loginLink);
        insertProdutsLink = getString(R.string.insertProductsLink);
        authorizationAct = new Intent(this, AuthorizationAct.class);
        productsCollection = new ProductsCollection();
        productsCollection.loadProductsCollectionsFromFile(this);
        server = new Server();
        if (server.loadServerFromFile(this)) {
            textServerName.setText("Server: " + server.getName());
        } else {
            textServerName.setText(getString(R.string.serverNotFound));
        }
        user = new User();
        user.loadUserFromFile(this);
        checkAmountLimit();
        displayRecyclerViewProductsList();
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("ProductListCollection, LifeCycle", "onStart");
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onStop() {
        Log.i("ProductListCollection, LifeCycle", "onStop");
        super.onStop();
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onClick(View v) {
        Log.i("ProductListCollection, LifeCycle", "onClick");
        switch (v.getId()) {
            case R.id.btnCabinetAct:
                startActivity(new Intent(this, CabinetAct.class));
                super.finish();
                break;
            case R.id.btnPromotionsAct:
                startActivity(new Intent(this, PromotionsAct.class));
                super.finish();
                break;
            case R.id.btnAmountLimiter:
                dialogAmountLimiter();
                break;
            case R.id.btnProductAdd:
                dialogProduct(true);
                break;
            case R.id.btnRVProductDelete:
                dialogProduct(false);
                break;
        }
    }
}
