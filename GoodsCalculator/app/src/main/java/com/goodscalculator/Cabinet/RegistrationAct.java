package com.goodscalculator.Cabinet;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.goodscalculator.R;

public class RegistrationAct extends Activity implements View.OnClickListener {

    private User user;
    private EditText editLogin, editPassword, editConfirmationPassword;
    private Button btnRegister;

    private void registerUser(String login, String password, String confirmationPassword) {
        String strErrors = "";
        boolean errors = false;
        if (!confirmationPassword.equals(password)) {
            strErrors = strErrors + "Пароли не совпадают \n";
            errors = true;
        }
        if (login.length() < 8) {
            strErrors = strErrors + "Логин должен быть больше 8 символов \n ";
            errors = true;
        }
        if (password.length() < 8) {
            strErrors = strErrors + "Пароль должен быть больше 8 символов \n";
            errors = true;
        }
        if (errors) {
            Toast.makeText(this,strErrors,Toast.LENGTH_SHORT).show();
            Log.i("RegistrationAct", "false register user");
        } else {
            user = new User(login, password);
            if (user.doRegister(getString(R.string.host), getString(R.string.registerLink))) {
                Log.i("RegistrationAct", user.toString());
                super.finish();
            } else {
                Toast.makeText(this, "Ошибка регистрации", Toast.LENGTH_SHORT).show();
                Log.e("RegistrationAct", "false");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editLogin = (EditText) findViewById(R.id.editLogin);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editConfirmationPassword = (EditText) findViewById(R.id.editConfirmationPassword);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                registerUser(
                        editLogin.getText().toString(),
                        editPassword.getText().toString(),
                        editConfirmationPassword.getText().toString());
                break;
        }
    }
}
