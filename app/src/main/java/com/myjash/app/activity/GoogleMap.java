package com.myjash.app.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.myjash.app.R;
import com.myjash.app.model.ProductModel;

import java.util.ArrayList;


public class GoogleMap extends AppCompatActivity implements OnMapReadyCallback {

    RelativeLayout lytMenu;
    private com.google.android.gms.maps.GoogleMap mMap;
    Button btnClose;
    Double latitude = 0.0;
    Double longitude = 0.0;
    String name;
    ArrayList<ProductModel> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        btnClose = (Button) findViewById(R.id.btnClose);
        /*remove menu icon from header*/
        lytMenu = (RelativeLayout) findViewById(R.id.lytMenu);
        lytMenu.setVisibility(View.GONE);

        arrayList = (ArrayList<ProductModel>) getIntent().getSerializableExtra("array");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {

        for (int i = 0; i < arrayList.size(); i++) {
            mMap = googleMap;
            String lat = arrayList.get(i).getLatitude() != null ? arrayList.get(i).getLatitude() : "0.0";
            latitude = Double.parseDouble(lat);
            String lng = arrayList.get(i).getLongitude() != null ? arrayList.get(i).getLongitude() : "0.0";
            longitude = Double.parseDouble(lng);
            int icon;
            if (arrayList.get(i).getVendorName() == null) {
                name = arrayList.get(i).getName();
                icon = R.drawable.pin_branch;
            } else {
                name = arrayList.get(i).getVendorName();
                icon = R.drawable.pin_shop;
            }
            Log.d("Names", name + "geo " + latitude + " " + longitude);

            if (latitude != 0.0 && longitude != 0.0) {
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(name));
                LatLng latLng = new LatLng(latitude, longitude);
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(name)
                        .snippet(arrayList.get(i).getAddress() + "\nPhone:" + arrayList.get(i).getPhone())
                        .icon(BitmapDescriptorFactory
                                .fromResource(icon)));
                marker.showInfoWindow();

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
//                mMap.setMapType(6);
                mMap.setOnMarkerClickListener(new com.google.android.gms.maps.GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        return false;
                    }
                });

            }
        }
    }

    private class CustomInfoWindowAdapter implements com.google.android.gms.maps.GoogleMap.InfoWindowAdapter {

        String name;

        public CustomInfoWindowAdapter(String name) {
            this.name = name;

        }

        @Override
        public View getInfoContents(Marker mMap) {

            /*if (GoogleMap.this.marker != null
                    && GoogleMap.this.marker.isInfoWindowShown()) {
                GoogleMap.this.marker.hideInfoWindow();
                GoogleMap.this.marker.showInfoWindow();
            }*/
            return null;
        }

        @Override
        public View getInfoWindow(final Marker marker) {
            View view = getLayoutInflater().inflate(R.layout.layout_custom_info_window,
                    null);
            TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            TextView txtSnippet = (TextView) view.findViewById(R.id.txtSnippet);
            txtTitle.setText(marker.getTitle());
            txtSnippet.setText(marker.getSnippet());
            return view;
        }
    }
}
