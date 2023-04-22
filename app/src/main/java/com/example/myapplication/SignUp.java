package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    private EditText userName, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign);

        userName = findViewById(R.id.username);
        password = findViewById(R.id.password);

    }


    public void sign_up(View view) {
        DBManager myUser = new DBManager(SignUp.this);
        if ((userName.getText().length() > 0) && (password.getText().toString().length()>0))
        {
            if(myUser.doesExist(userName.getText().toString())==0)
            {
                myUser.insertUser(userName.getText().toString(), password.getText().toString());

                Intent intent = new Intent(this, login.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(this, "Username exists!", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "No data detected!", Toast.LENGTH_SHORT).show();
        }

    }

    public void switchToLogin(View view) {
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }
}