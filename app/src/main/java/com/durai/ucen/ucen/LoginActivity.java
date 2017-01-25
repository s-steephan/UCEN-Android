package com.durai.ucen.ucen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String ROOT_URL = "https://ucen.herokuapp.com";
    EditText t_username, t_password;
    Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        t_username = (EditText) findViewById(R.id.t_username);
        t_password = (EditText) findViewById(R.id.t_password);
        login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String username = t_username.getText().toString();
        String password = t_password.getText().toString();

        getToken(username, password);

    }
    private void getToken(String username, String password){
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL)
                .build();

        UcenAPI api = adapter.create(UcenAPI.class);

        api.getUser(username, password,new Callback<Token>() {
            public static final String PREFS_NAME = "Login_Token";
                    @Override
                    public void success(Token result, Response response) {
                        t_username.setText("");
                        t_password.setText("");
                        String token = result.getToken();
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("token", token);
                        editor.apply();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError cause) {
                        t_password.setText("");
                        String errorDescription = "Wrong Username or Password";
                        if (cause.getResponse() == null) {
                                errorDescription = "No Internet Connection";
                            }
                        Toast.makeText(LoginActivity.this, errorDescription, Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

}
