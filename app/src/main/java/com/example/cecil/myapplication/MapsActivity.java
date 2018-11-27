package com.example.cecil.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;

import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    public String a, b;
    String mprovider;
    private LocationManager locationManager;

    String la[] = new String[100];
    String lo[] = new String[100];

    public boolean mapLoaded;
    public boolean firstTime;

    public Integer no;

    public GoogleMap ownMarker, hotelMarker;
    private Button b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mapLoaded = false;
        firstTime = true;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.gmap);
        mapFragment.getMapAsync(this);


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

            if (ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                return;
            }

            else
                Toast.makeText(getBaseContext(), "No location provider found", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        ownMarker = googleMap;
        ownMarker.setMapType(ownMarker.MAP_TYPE_HYBRID);

        hotelMarker = googleMap;
        mapLoaded = true;

    }

    @Override
    public void onLocationChanged(Location location) {

        if(mapLoaded){
            ownMarker.clear();
            a = String.valueOf(location.getLatitude());
            b = String.valueOf(location.getLongitude());
            System.out.println("Location is "+location);
            LatLng own = new LatLng(location.getLatitude(), location.getLongitude());
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.personmarker);
            Bitmap b=bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
            ownMarker.addMarker(new MarkerOptions().position(own).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

            //.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))

            if(firstTime){
                CameraPosition asd = new CameraPosition.Builder()
                        .target(own)
                        .zoom(15)
                        .bearing(location.getBearing())
                        .build();

                CameraUpdate cu = CameraUpdateFactory.newCameraPosition(asd);
                ownMarker.animateCamera(cu);
                firstTime = false;
            }

        }


        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

///////////

        db.collection("hotelCount")
                .document("hotels")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String num = documentSnapshot.getString("number");
                            no = Integer.parseInt(num);
                            System.out.println("no is "+no);

                            FirebaseFirestore dbs = FirebaseFirestore.getInstance();
                            for(int i=1; i<=no ; i++){
                                dbs.collection("hotels")
                                        .document("hotel "+i)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                if(documentSnapshot.exists()){
                                                    System.out.println("test "+documentSnapshot.getString("latitude"));
                                                    String lat = documentSnapshot.getString("latitude");
                                                    String lon = documentSnapshot.getString("longitude");
                                                    String nameofHotel = documentSnapshot.getString("name");

                                                    System.out.println("testt "+la);

                                                    BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.hotelmarker);
                                                    Bitmap b=bitmapdraw.getBitmap();
                                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);

                                                    Double latt = Double.parseDouble(lat);
                                                    Double lonn = Double.parseDouble(lon);
                                                    System.out.println("tests "+latt);
                                                    LatLng hotel = new LatLng(latt,lonn);
                                                    hotelMarker.addMarker(new MarkerOptions().position(hotel).title(nameofHotel)
                                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                                                }

                                        }
                                    });
                            }

                        }
                    }
                });
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
