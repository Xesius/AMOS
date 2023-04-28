package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Locale;

public class main extends AppCompatActivity {
    private TextView userName;
    private ListView lv;
    private ArrayList<User> test;
    private UsersAdapter ia;
    SharedPreferences sp;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        test = new ArrayList<User>();
        ia = new UsersAdapter(this , test);
        lv = findViewById(R.id.searchbard);
        lv.setAdapter(ia);
        SearchView sv = findViewById(R.id.sr);
        sv.clearFocus();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                User newUser = new User("Nathan", "San Diego");

                ia.add(newUser);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
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
    private void filterList(String text)
    {
        ArrayList<User> new_l = new ArrayList<User>();
        for (User item : test)
        {
            if (item.pass.toLowerCase().contains(text.toLowerCase())) {
                new_l.add(item);
            }
        }

        if (new_l.isEmpty())
        {
            Toast.makeText(this, "No DATA", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ia.setFiltered(new_l);
        }
    }
}