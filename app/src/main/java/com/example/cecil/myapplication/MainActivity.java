package com.example.cecil.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cecil.myapplication.AccoountActivity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button maboutus;
    Button mLoginBtn, mfindhotel;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mLoginBtn = findViewById(R.id.loginBtn);
        mfindhotel = findViewById(R.id.button2);


        maboutus = findViewById(R.id.aboutus);
        maboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "this is information about App developers", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, About_Us.class);
                startActivity(intent);
            }
        });


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginIntent();
            }
        });

        if (haveNetwork()){
            mfindhotel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "Map Is Opening", Toast.LENGTH_SHORT).show();
                    Intent a = new Intent(MainActivity.this, MapsActivity.class);
                    startActivity(a);
                }
            });
        }
        else {
            mfindhotel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "device is not connected to the internet", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private boolean haveNetwork(){
        boolean have_WIFI=false;
        boolean have_mobileData=false;
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos=connectivityManager.getAllNetworkInfo();

        for (NetworkInfo info:networkInfos){
            if (info.getTypeName().equalsIgnoreCase("WIFI"))
                if (info.isConnected())
                    have_WIFI=true;

            if (info.getTypeName().equalsIgnoreCase("Mobile"))
                if (info.isConnected())
                    have_mobileData=true;
        }
        return have_mobileData || have_WIFI;
    }

    private void loginIntent() {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);

    }

}
