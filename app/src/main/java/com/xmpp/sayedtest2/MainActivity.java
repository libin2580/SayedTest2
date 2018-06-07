package com.xmpp.sayedtest2;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.xmpp.sayedtest2.Model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng syedMetal;


    public ArrayList<Model> lat_long_list;

    BitmapDescriptor bitmapDescriptor;

    private ClusterManager<ClusterModel> mClusterManager;
    String JSON_URL="https://www.myscrap.com/android/msDiscover";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bitmapDescriptor= BitmapDescriptorFactory.fromResource(R.drawable.strtloc);
        lat_long_list=new ArrayList<>();
        get_tracking_data();

      /*  if (InternetConnection.checkConnection(getApplicationContext())) {

            get_tracking_data();

        }
        else {

            Toasty.error(MainActivity.this, "No Internet", Toast.LENGTH_SHORT, true).show();
        }*/
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpClusterer();

    }


    private void setUpClusterer() {
        // Declare a variable for the cluster manager.
        syedMetal = new LatLng(25.286182, 55.397430);

       /* for(int i=0;i<lat_long_list.size();i++){
             syedMetal = new LatLng(Double.parseDouble(lat_long_list.get(i).getLatitude()), Double.parseDouble(lat_long_list.get(i).getLatitude()));

        }*/

        // Position the map.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(syedMetal, 7));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager(this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener( mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addItems();
    }
    private void get_tracking_data() {
        final KProgressHUD hud2= KProgressHUD.create(MainActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f)
                .show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("_________response___________" + response);
                        hud2.dismiss();
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String  status = jsonObj.getString("status");
                            System.out.println("_________status___________" + status);
                            String  locationData = jsonObj.getString("locationData");

                            JSONArray array1=new JSONArray(locationData);
                            for (int k = 0; k < array1.length(); k++) {
                                JSONObject l = array1.getJSONObject(k);


                                String name = l.getString("name");
                                String Latitude = l.getString("latitude");
                                String Longitude = l.getString("longitude");
                                Model mm=new Model();
                                mm.setLatitude(Latitude);
                                mm.setLongitude(Longitude);
                                lat_long_list.add(mm);
                                System.out.println("_________Longitude____________" + Longitude);

                            }


                        }
                        catch (JSONException ew) {
                            ew.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hud2.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId","35");
                params.put("apiKey","123456test");
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);


    }

    public void addItems() {

        // Set some lat/lng coordinates to start with.
        double lat = 25.286182;
        double lng = 55.397430;
       // System.out.println("_________DDDDDDe____________" + Double.parseDouble(lat_long_list.get(1).getLatitude()));
       /* double lat = Double.parseDouble(lat_long_list.get(0).getLatitude());
        double lng = Double.parseDouble(lat_long_list.get(0).getLongitude());*/

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < lat_long_list.size(); i++) {
            double offset = i / 60d;
            lat = Double.parseDouble(lat_long_list.get(i).getLatitude() + offset);
            lng = Double.parseDouble(lat_long_list.get(i).getLongitude() + offset);
            ClusterModel offsetItem = new ClusterModel(lat,lng);
            mClusterManager.addItem(offsetItem);
        }
    }


}
