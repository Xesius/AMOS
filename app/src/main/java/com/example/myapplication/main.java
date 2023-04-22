package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class main extends AppCompatActivity {
    private TextView userName;
    SharedPreferences sp;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.btmNav);
        bottomNavigationView.setSelectedItemId(R.id.bottom_search);
        userName = findViewById(R.id.userName);
        sp=getSharedPreferences("details", 0);
        Resources res = getResources();
        userName.setText(String.format(res.getString(R.string.hey_user), sp.getString("user", null)));
        YoYo.with(Techniques.Landing).duration(2000).repeat(0).playOn(userName);
        /*mp = MediaPlayer.create();
        mp.start();
        mp.setLooping(true);
        AudioManager am = (AudioManager) this.getSystemService();
        am.setStreamVolume(AudioManager.STREAM_SYSTEM, am, 0);*/
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_search:
                    return true;
                case R.id.bottom_add:
                    startActivity(new Intent(getApplicationContext(), add.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
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