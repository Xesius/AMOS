package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class add extends AppCompatActivity {
    private TextView userName;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        userName = findViewById(R.id.userName);
        sp=getSharedPreferences("details", 0);
        Resources res = getResources();
        userName.setText(String.format(res.getString(R.string.hey_user), sp.getString("user", null)));
        YoYo.with(Techniques.Landing).duration(2000).repeat(0).playOn(userName);

        BottomNavigationView bottomNavigationView = findViewById(R.id.btmNav);
        bottomNavigationView.setSelectedItemId(R.id.bottom_add);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_search:
                    startActivity(new Intent(getApplicationContext(), main.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                    return true;
                case R.id.bottom_add:

                    return true;
                case R.id.bottom_settings:
                    startActivity(new Intent(getApplicationContext(), settings.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });
    }
}