package com.example.rick.bikepark;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btLogin = (Button) findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                TextView tLogin = findViewById(R.id.tLogin);
                TextView tSenha = findViewById(R.id.tSenha);
                String login = tLogin.getText().toString();
                String senha = tSenha.getText().toString();
                if (login.equals("admin@bikepark.com")&& senha.equals("admin")){
                   alert("Login autorizado");
                    Intent intentMap = new Intent(LoginActivity.this, BikeMapActivity.class);
                    startActivity(intentMap);
                }
                else{
                    alert("Login invalido");
                }
            }
        });
    }

    private void alert(String s){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
}
