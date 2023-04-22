package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity {

    private EditText userName, password;
    Context context;
    SharedPreferences sp;
    int countUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        userName = findViewById(R.id.username);
        password = findViewById(R.id.password);
        sp=getSharedPreferences("details", 0);
    }


    public void loginClick(View view) {
        DBManager myUser = new DBManager(login.this);
        countUsers = myUser.checkForUser(userName.getText().toString(),password.getText().toString());

        if (countUsers==1)
        {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("user", userName.getText().toString());
            editor.apply();
            Intent intent = new Intent(this, main.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "No such user!", Toast.LENGTH_SHORT).show();
        }
    }
    public void switchToSign(View view) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
}
