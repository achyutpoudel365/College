package com.example.cecil.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.Manifest;

import com.example.cecil.myapplication.AccoountActivity.LoginActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddhotelActivity extends AppCompatActivity implements LocationListener {


    //for data store in firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText mhotel;
    String mprovider;
    private LocationManager locationManager;

    public String no;


    private Button mbtn2;
    boolean abc = false;
    public String str, a, b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addhotel);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        mprovider = locationManager.getBestProvider(criteria, false);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location location = locationManager.getLastKnownLocation(mprovider);
        locationManager.requestLocationUpdates(mprovider, 15000, 1, this);

        if (location != null)
            onLocationChanged(location);

        if (mprovider !=null && !mprovider.equals("")){

            if (ActivityCompat.checkSelfPermission(AddhotelActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                return;
            }

            else
                Toast.makeText(getBaseContext(), "No location provider found", Toast.LENGTH_SHORT).show();
        }


        mhotel = findViewById(R.id.hotelnamaskar);
        mbtn2 = findViewById(R.id.adding);

        mbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = mhotel.getText().toString();
                Map<String, Object> hotels = new HashMap<>();
                hotels.put("longitude",b);
                hotels.put("latitude",a);
                hotels.put("name",str);

                // Add a new document with a generated ID

                db.collection("hotelCount")
                        .document("hotels")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                no = documentSnapshot.getString("number");
                                System.out.println("String is "+no);
                                Integer num = Integer.parseInt(no)+1;
                                no =String.valueOf(num);

                                Map<String, Object> addno = new HashMap<>();
                                addno.put("number",no);

                                db.collection("hotelCount")
                                        .document("hotels")
                                        .set(addno);


                            }
                        });

                db.collection("hotels").document("hotel "+no)
                        .set(hotels);

                Toast.makeText(AddhotelActivity.this, "Hotel successfully added", Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public void onLocationChanged(Location location) {

        a = String.valueOf(location.getLatitude());
        b = String.valueOf(location.getLongitude());

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

