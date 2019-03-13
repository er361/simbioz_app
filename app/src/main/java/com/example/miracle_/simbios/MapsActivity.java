package com.example.miracle_.simbios;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String http_res = "http_res";
    private static String TAG_TEST = "test_tag";
    private static String TAG = "tagg";
    private static final String DIRECTIONS_API_KEY = "AIzaSyDtPEtWSaq3th_Mz-rYp1J9H0CIJqz9JyM";
    private ArrayList<Marker> markers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(51.1359602, 71.3916134);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));

        final RequestQueue queue = Volley.newRequestQueue(this);

        //api config
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
                markers.add(marker);

                if (markers.size() > 1) {
                    LatLng oriPos = markers.get(0).getPosition();
                    LatLng desPos = markers.get(1).getPosition();

                    String origin = oriPos.latitude + "," + oriPos.longitude;
                    String destination = desPos.latitude + "," + desPos.longitude;

                    String url = "http://10.0.2.2/route/" + origin + "/" + destination;

                    Log.d(TAG, "onMapClick: " + url);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG_TEST, response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG_TEST, error.toString());
                        }
                    });
                    queue.add(stringRequest);

                    for (Marker delM : markers) {
                        delM.remove();
                    }
                }
            }
        });
    }

}
