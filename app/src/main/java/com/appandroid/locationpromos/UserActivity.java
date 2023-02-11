package com.appandroid.locationpromos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.appandroid.locationpromos.adapter.MyAdapter;
import com.appandroid.locationpromos.adapter.MyAdapter2;
import com.appandroid.locationpromos.database.dbhelper;
import com.appandroid.locationpromos.geofence.GeofenceBroadcastReceiver;
import com.appandroid.locationpromos.models.BrandModel;
import com.appandroid.locationpromos.models.notification_user;
import com.appandroid.locationpromos.utils.SharedPref;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;


public class UserActivity extends AppCompatActivity implements LocationListener {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    SwipeRefreshLayout refresh ;
    MyAdapter2 myAdapter;
    dbhelper dbhelper;
    TextView tvPosition;
    private LocationManager lm ;int CODE_APPEL=103;

    public ArrayList<BrandModel> mDataList= new ArrayList<BrandModel>();
    public ArrayList<notification_user> mDataList2= new ArrayList<notification_user>();
    private List<Geofence> geofenceList = new ArrayList<>();
    private GeofencingClient geofencingClient;
    private PendingIntent geofencePendingIntent;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_login) {
            SharedPref.saveSharedSetting(UserActivity.this,"userconnect","False");
            Intent logout = new Intent(UserActivity.this,LoginActivity.class);
            startActivity(logout);
            finish();
            return true;


        }else if (id == R.id.nav_settings) {

            Intent settings = new Intent(UserActivity.this,Settings.class);
            startActivity(settings);
          //  finish();
            return true;


        }


        return super.onOptionsItemSelected(item);
    }
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }
    private void removeGeofence() {
        geofencingClient.removeGeofences(getGeofencePendingIntent())
                .addOnSuccessListener(this, aVoid -> {
                   // Toast.makeText(getApplicationContext()
                    //        , "Geofencing has been removed", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(this, e -> {
                    Toast.makeText(getApplicationContext()
                            , "Geofencing could not be removed", Toast.LENGTH_SHORT).show();
                });
    }

    @SuppressLint("MissingPermission")
    private void addGeofence() {
        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(this, aVoid -> {
                    Toast.makeText(getApplicationContext()
                            , "Geofencing has started", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(this, e -> {
                    Toast.makeText(getApplicationContext()
                            , "Geofencing failed"+e.getMessage(), Toast.LENGTH_SHORT).show();

                });
    }
    @Override
    public void onBackPressed() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("are you sure want to exit the application?")
                .setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        dbhelper = new dbhelper(getApplicationContext());
        recyclerView=(RecyclerView) findViewById(R.id.recyclerViewuser);
        subProviders();
        geofencingClient = LocationServices.getGeofencingClient(this);


tvPosition = (TextView) findViewById(R.id.tvposition);
        mDataList = dbhelper.getallbrand( );
        mDataList2= dbhelper.getAllNotification();
        geofenceList.clear();
        removeGeofence();
        if(!mDataList2.isEmpty()) {
            for (notification_user n : mDataList2) {

                BrandModel b = dbhelper.getBrandById(n.getBrand_id());
                geofenceList.add(new Geofence.Builder()
                        // Set the request ID of the geofence. This is a string to identify this
                        // geofence.
                        .setRequestId(n.getUser_id() + "/" + n.getBrand_id())

                        .setCircularRegion(
                                Double.valueOf(b.getLatitude()),
                                Double.valueOf(b.getLogitude()),
                                200
                        )
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER /*|
                                Geofence.GEOFENCE_TRANSITION_EXIT*/)
                        .build());
            }
            addGeofence();
        }
        refresh = (SwipeRefreshLayout) findViewById(R.id.swiperefreshuser);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mDataList = dbhelper.getallbrand();
                mDataList2= dbhelper.getAllNotification();
                myAdapter = new MyAdapter2(UserActivity.this, mDataList,mDataList2,dbhelper);
                recyclerView.setAdapter(myAdapter);
                LinearLayoutManager mLinearLayoutManagert = new LinearLayoutManager(getApplicationContext());
                mLinearLayoutManagert.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(mLinearLayoutManagert);
                geofenceList.clear();
                removeGeofence();
                if(!mDataList2.isEmpty()) {
                    for (notification_user n : mDataList2) {

                        BrandModel b = dbhelper.getBrandById(n.getBrand_id());
                        geofenceList.add(new Geofence.Builder()
                                // Set the request ID of the geofence. This is a string to identify this
                                // geofence.
                                .setRequestId(n.getUser_id() + "/" + n.getBrand_id())

                                .setCircularRegion(
                                        Double.valueOf(b.getLatitude()),
                                        Double.valueOf(b.getLogitude()),
                                        200
                                )
                                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER /*|
                                Geofence.GEOFENCE_TRANSITION_EXIT*/)
                                .build());
                    }
                    addGeofence();
                }

                refresh.setRefreshing(false);

            }
        });
        myAdapter = new MyAdapter2(UserActivity.this, mDataList,mDataList2,dbhelper);
        recyclerView.setAdapter(myAdapter);
        LinearLayoutManager mLinearLayoutManagert = new LinearLayoutManager(getApplicationContext());
        mLinearLayoutManagert.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagert);
        TextInputEditText searchcontact = (TextInputEditText) findViewById(R.id.searchuser);
        searchcontact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (UserActivity.this).myAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        tvPosition.setText("latitude: "+latitude+" longitude "+longitude);

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
    protected void subProviders() {
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, CODE_APPEL);
            return;
        }
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        if(lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER))
            lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1000, 0, this);
        if(lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);

      //  map();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (lm!=null) lm.removeUpdates(this);
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==CODE_APPEL) subProviders();
    }

}