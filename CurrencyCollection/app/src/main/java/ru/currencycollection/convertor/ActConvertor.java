package ru.currencycollection.convertor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ru.currencycollection.currenties.ActCurrencies;
import ru.currencycollection.R;

public class ActConvertor extends AppCompatActivity implements View.OnClickListener {

    Button btnActMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_currencies);

        btnActMain = (Button) findViewById(R.id.btnActMain); btnActMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnActMain:
                startActivity(new Intent(this, ActCurrencies.class));
        }
    }
}
