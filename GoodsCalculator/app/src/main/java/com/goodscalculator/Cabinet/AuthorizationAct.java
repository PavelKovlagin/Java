package com.goodscalculator.Cabinet;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.goodscalculator.R;

public class AuthorizationAct extends Activity implements View.OnClickListener {

    User user;
    String host;
    String loginLink;
    Button btnLogin;
    EditText editPassword, editLogin;
    TextView textRegistration;

    public void login(String login, String password) {
        user = new User(login, password);
        if (user.doLogin(host, loginLink)) {
            user.saveUserToFile(this);
            super.finish();
        } else {
            Toast.makeText(this, "Ошибка авторизации", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        host = getString(R.string.host);
        loginLink = getString(R.string.loginLink);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        editLogin = (EditText) findViewById(R.id.editLogin);
        editPassword = (EditText) findViewById(R.id.editPassword);
        textRegistration = (TextView) findViewById(R.id.textRegistration);
        textRegistration.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                String login = editLogin.getText().toString();
                String password = editPassword.getText().toString();
                login(login, password);
                break;
            case R.id.textRegistration:
                Intent registrationAct = new Intent(this, RegistrationAct.class);
                startActivity(registrationAct);
                break;
        }
    }
}
