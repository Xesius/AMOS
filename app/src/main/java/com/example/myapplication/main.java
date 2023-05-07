package com.example.myapplication;

import static android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
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
import java.util.concurrent.Executor;

public class main extends AppCompatActivity {
    private TextView userName;
    private ListView lv;
    private ArrayList<Entry> test;
    private UsersAdapter ia;
    BottomNavigationView bottomNavigationView;
    SharedPreferences sp;
    BiometricManager biometricManager;
    BiometricPrompt.PromptInfo promptInfo;
    TextView tv;
    TextView hid;
    int pos_for_auth;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    SearchView sv;
    DBManager bibi;
    String user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        bibi = new DBManager(main.this);
        if (musicStatus.music == null) {
            musicStatus.music = new Intent(this, MyService.class);
            startService(musicStatus.music);
        }



        userName = findViewById(R.id.userName);
        sp=getSharedPreferences("details", 0);
        Resources res = getResources();
        user_name = sp.getString("user", null);
        userName.setText(String.format(res.getString(R.string.hey_user), user_name));
        YoYo.with(Techniques.Landing).duration(2000).repeat(0).playOn(userName);
        lv = findViewById(R.id.searchbard);
         sv = findViewById(R.id.sr);

        test = bibi.getRecipes(user_name);

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

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(main.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                tv.setText(ia.getItem(pos_for_auth).getPass());
                hid.setText("visib");
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric CHECK")
                .setSubtitle("Please put your finger to test")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
                                  {
                                      @Override
                                      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                          RelativeLayout tw = (RelativeLayout) view;
                                          tv = tw.findViewById(R.id.textView);
                                          hid = tw.findViewById(R.id.hidden);
                                          pos_for_auth = position;
                                          if (hid.getText().toString().contains("hidde"))
                                          {
                                              biometricManager = BiometricManager.from(main.this);
                                              switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
                                                  case BiometricManager.BIOMETRIC_SUCCESS:
                                                      Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                                                      biometricPrompt.authenticate(promptInfo);

                                                      break;
                                                  case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                                                      Log.e("MY_APP_TAG", "No biometric features available on this device.");
                                                      tv.setText(ia.getItem(pos_for_auth).getPass());
                                                      hid.setText("visib");
                                                      break;
                                                  case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                                                      Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                                                      tv.setText(ia.getItem(pos_for_auth).getPass());
                                                      hid.setText("visib");
                                                      break;
                                                  case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                                                      tv.setText(ia.getItem(pos_for_auth).getPass());
                                                      hid.setText("visib");
                                                      break;
                                              }

                                          }
                                          else
                                          {
                                              tv.setText("*****************");
                                              hid.setText("hidden");
                                          }

                                      }
                                  }
        );

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Entry item = ia.orig.get(position);
                test.remove(item);
                ia.removeEntry(item);
                bibi.removeEntry(item);
                return true;
            }
        });

        bottomNavigationView = findViewById(R.id.btmNav);
        bottomNavigationView.setSelectedItemId(R.id.bottom_search);


        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_search:
                    return true;
                case R.id.bottom_add:
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
                                    ia.now.add(new Entry(nam.getText().toString(), desc.getText().toString(), pas.getText().toString(), user_name));
                                    bibi.insertNewPass(nam.getText().toString(), desc.getText().toString(), pas.getText().toString(), user_name);
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
                    finish();
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