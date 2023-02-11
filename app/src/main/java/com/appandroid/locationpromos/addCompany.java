package com.appandroid.locationpromos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appandroid.locationpromos.database.dbhelper;
import com.appandroid.locationpromos.utils.BitmapConvert;
import com.appandroid.locationpromos.utils.SharedPref;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class addCompany extends AppCompatActivity implements LocationListener {
    Button btnupload,btnlocation;

    TextView textlocation;
    dbhelper dbhelper;
    ImageView image;
    int REQUEST_IMAGE_CAPTURE = 33;
    private Bitmap bitmap;
    byte [] bytesimage;
    SupportMapFragment mapFragment;
    GoogleMap googleMap;
    String path;
    LocationManager lm;
    int CODE_APPEL=103;
    LatLng pointt;
    ArrayList<LatLng> points ;
    TextInputEditText company_nameedt , notificationedt, descriptionedt;
    MaterialButton btncomp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company);
        dbhelper = new dbhelper(getApplicationContext());
        btnupload = (Button) findViewById(R.id.btnupload);
        btnlocation = (Button) findViewById(R.id.btnlongitude);
        textlocation = (TextView) findViewById(R.id.textlocationaddcompany);
        points= new ArrayList<LatLng>();
        btncomp= (MaterialButton) findViewById(R.id.addcompanybtn);
        image = (ImageView) findViewById(R.id.imageselected);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getView().setVisibility(View.GONE);
        company_nameedt = (TextInputEditText)  findViewById(R.id.company_name_edit_text);
        notificationedt = (TextInputEditText)  findViewById(R.id.notification_edit_text);
        descriptionedt = (TextInputEditText)  findViewById(R.id.description_edit_text);
        subProviders();

        btncomp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean noErrors = true;
                String company_nametst = company_nameedt.getText().toString();
                String notificationetst = notificationedt.getText().toString() ;
                String descriptiontst = descriptionedt.getText().toString() ;

                if (company_nametst.isEmpty()) {
                    company_nameedt.setError("Please fill out all fields completely. ");
                    noErrors = false;
                } else {
                    company_nameedt.setError(null);
                }
                if (notificationetst.isEmpty()) {
                    notificationedt.setError("Please fill out all fields completely. ");
                    noErrors = false;
                } else {
                    notificationedt.setError(null);
                }
                if (descriptiontst.isEmpty()) {
                    descriptionedt.setError("Please fill out all fields completely. ");
                    noErrors = false;
                } else {
                    descriptionedt.setError(null);
                }
                if (descriptiontst.isEmpty()) {
                    descriptionedt.setError("Please fill out all fields completely. ");
                    noErrors = false;
                } else {
                    descriptionedt.setError(null);
                }
                if (pointt==null) {
                    noErrors = false;
                    Toast.makeText(getApplicationContext(),"you forget to add location",Toast.LENGTH_LONG).show();
                }

                if (noErrors) {
                    if(bitmap!=null ) {
                        addcompany us=    new addcompany(getApplicationContext(), bytesimage, company_nametst,notificationetst,descriptiontst,pointt.longitude,pointt.latitude,dbhelper);
                        us.execute();
                    }else {
                        new AlertDialog.Builder(addCompany.this)
                                .setTitle("please upload a logo")
                                .setMessage("please upload a logo")


                                .setNegativeButton(android.R.string.ok, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    }


                }
            }
        });

    }
    private class addcompany extends AsyncTask<Void,Void,String> {
        String company_name , notification_text , description_text ,longitude, latitude;
        byte[] bytesimage;
        Context ct;
        dbhelper dbh;

        public addcompany(Context applicationContext, byte[] bytesimage, String company_nametst, String notificationetst, String descriptiontst, double longitude, double latitude, dbhelper dbhelper) {
            this.company_name =company_nametst;
            this.bytesimage=bytesimage;
            this.notification_text=notificationetst;
            this.description_text=descriptiontst;
            this.longitude= String.valueOf(longitude);
            this.latitude=String.valueOf(latitude);
            this.ct = applicationContext;

            this.dbh=dbhelper;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Toast.makeText(ct, "wait !!! ", Toast.LENGTH_LONG).show();
        }



        @Override
        protected String doInBackground(Void... voids) {
int retailer_id = SharedPref.readSharedSettingint(addCompany.this,"user_id",0);
                ContentValues values = new ContentValues();
                values.put("retailerId",retailer_id);
                values.put("brand_title", company_name);
                values.put("brand_image", bytesimage);
            values.put("brand_desc", description_text);
                values.put("latitude", latitude);
                values.put("longitude", longitude);
               values.put("notification_text", notification_text);
                dbh.Insert(values,"brand");
                Intent retailer = new Intent(addCompany.this,RetailerActivity.class);
                addCompany.this.startActivity(retailer);
                return "company added";


        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(),"response : "+s,Toast.LENGTH_LONG);

        }




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

        map();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==CODE_APPEL) subProviders();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (lm!=null) lm.removeUpdates(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_IMAGE_CAPTURE) {

                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                   // image.setImageURI(selectedImageUri);

                    Uri selectedImage = data.getData();

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(addCompany.this.getContentResolver(), selectedImage);
                        image.setImageBitmap(bitmap);
                        image.setVisibility(View.VISIBLE);
                        path = MediaStore.Images.Media.insertImage(addCompany.this.getContentResolver(), bitmap, "Title", null);
                        bytesimage = BitmapConvert.getBytes(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    @SuppressWarnings("MissingPermission")
    protected void map(){
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                addCompany.this.googleMap=googleMap;
                googleMap.moveCamera(CameraUpdateFactory.zoomBy(20));
                googleMap.setMyLocationEnabled(true);
             //   googleMap.addMarker(new MarkerOptions().position(new LatLng(10, 10)).title("Ma position"));
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng point) {
                        pointt=point;
                        points.add(point);
                        googleMap.clear();
                        googleMap.addMarker(new MarkerOptions().position(point));
                        textlocation.setText(point.latitude + " " + point.longitude);
                    }
                });

            }     });

btnlocation.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      mapFragment.getView().setVisibility(View.VISIBLE);
    }
});
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_CAPTURE);
            }
        });



    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        LatLng myposition = new LatLng(latitude,longitude);
        googleMap.moveCamera(CameraUpdateFactory.zoomBy(20));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myposition));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}