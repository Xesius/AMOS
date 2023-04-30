package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class main extends AppCompatActivity {
    private TextView userName;
    private ListView lv;
    private ArrayList<Entry> test;
    private UsersAdapter ia;
    BottomNavigationView bottomNavigationView;
    SharedPreferences sp;
    MediaPlayer mp;
    SearchView sv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        test = new ArrayList<>();
        lv = findViewById(R.id.searchbard);
         sv = findViewById(R.id.sr);
        Entry newEntry = new Entry("test", "testing", "test123");
        test.add(newEntry);
        newEntry = new Entry("meest", "testing", "test123");
        test.add(newEntry);

        ia = new UsersAdapter(this , test);
        lv.setAdapter(ia);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ia.getFilter().filter(newText);
                return true;
            }

        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
                                  {
                                      @Override
                                      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                          RelativeLayout tw = (RelativeLayout) view;
                                          TextView tv = tw.findViewById(R.id.textView);
                                          TextView hid = tw.findViewById(R.id.hidden);

                                          if (hid.getText().toString() == "hidden")
                                          {
                                              tv.setText(ia.getItem(position).getPass());
                                              hid.setText("visib");
                                          }
                                          else
                                          {
                                              tv.setText("*****************");
                                              hid.setText("hidden");
                                          }

                                      }
                                  }
        );
        bottomNavigationView = findViewById(R.id.btmNav);
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
                    /*startActivity(new Intent(getApplicationContext(), add.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();*/
                    View view1 = LayoutInflater.from(main.this).inflate(R.layout.dialog_add, null);
                    TextInputEditText nam = view1.findViewById(R.id.nmin);
                    TextInputEditText pas = view1.findViewById(R.id.passmein);
                    TextInputEditText desc = view1.findViewById(R.id.descin);
                    AlertDialog ad = new MaterialAlertDialogBuilder(main.this)
                            .setTitle("Add")
                            .setView(view1)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ia.now.add(new Entry(nam.getText().toString(), desc.getText().toString(), pas.getText().toString()));
                                    bottomNavigationView.setSelectedItemId(R.id.bottom_search);
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    bottomNavigationView.setSelectedItemId(R.id.bottom_search);
                                    dialog.dismiss();
                                }
                            }).create();
                    ad.show();
                    return true;
                case R.id.bottom_settings:

                    someActivityResultLauncher.launch(new Intent(getApplicationContext(), settings.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    return true;
            }

            return false;
        });

    }
    private ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                        // There are no request codes
                        bottomNavigationView.setSelectedItemId(R.id.bottom_search);
                }
            });


}