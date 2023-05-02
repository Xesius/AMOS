package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.materialswitch.MaterialSwitch;

public class settings extends AppCompatActivity {

    ImageView ivDog;
    Bitmap bitmap1;
    Button button;
    SharedPreferences sp;
    MaterialSwitch ms;
    AirplaneModeChangeReceiver airplaneModeChangeReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ms = findViewById(R.id.switch1);
        ms.setChecked(musicStatus.if_running);
        ms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (musicStatus.if_running)
                {
                    //Intent music = new Intent(settings.this,MyService.class);
                    stopService(musicStatus.music);
                    musicStatus.if_running = false;
                }
                else
                {
                    //Intent music = new Intent(settings.this,MyService.class);
                    startService(musicStatus.music);
                    musicStatus.if_running = true;
                }
            }
        });
        airplaneModeChangeReceiver = new AirplaneModeChangeReceiver();
        BottomNavigationView bottomNavigationView = findViewById(R.id.btmNav);
        bottomNavigationView.setSelectedItemId(R.id.bottom_settings);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_search:
                case R.id.bottom_add:
                    startActivity(new Intent(getApplicationContext(), main.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();

                    //finish();
                    //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    return true;
                case R.id.bottom_settings:
                    return true;
            }
            return false;
        });
        SensorManager sensorMng = (SensorManager)getSystemService(SENSOR_SERVICE);
        Sensor tmp = sensorMng.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        TextView temp = findViewById(R.id.textView2);
        temp.setText(String.valueOf(tmp.getPower()));
        button = (Button) findViewById(R.id.button);
        ivDog = findViewById(R.id.imageView);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }

        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // camera mode result
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0)
        {
            if(resultCode!= RESULT_CANCELED)
            {
                Bundle bundle = data.getExtras();
                bitmap1 = (Bitmap) bundle.get("data");
                ivDog.setImageBitmap(bitmap1);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        registerReceiver(airplaneModeChangeReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(airplaneModeChangeReceiver);
    }

    public void logOut(View view) {
        sp=getSharedPreferences("details", 0);
        sp.edit().clear();
        sp.edit().apply();
        startActivity(new Intent(getApplicationContext(), SignUp.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();

    }

    public void changeMusic(View view) {
        Button bt = (Button) view;
        bt.setText("test");
    }

}