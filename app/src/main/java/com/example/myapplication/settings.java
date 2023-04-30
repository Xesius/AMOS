package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class settings extends AppCompatActivity {

    ImageView ivDog;
    Bitmap bitmap1;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        BottomNavigationView bottomNavigationView = findViewById(R.id.btmNav);
        bottomNavigationView.setSelectedItemId(R.id.bottom_settings);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_search:
                case R.id.bottom_add:
                    /*startActivity(new Intent(getApplicationContext(), main.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();*/

                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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

}