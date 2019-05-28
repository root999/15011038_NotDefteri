package com.example.semesterprojectv_0_7;


import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class gpsFragment extends Fragment implements LocationListener {
    private static final float GEOFENCE_RADIUS = 500 ;
    private Button konumBt;
    private Button remindBt;
    private TextView enlemTx;
    private TextView boylamTx;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    private Location location;
    private LocationManager locationManager;
    Double latitude,longtitude;
    private GeofencingClient geofencingClient;
    public gpsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_gps, container, false);
       konumBt= view.findViewById(R.id.konumalBt);
        remindBt = view.findViewById(R.id.hatirlaticiBT);
        enlemTx = view.findViewById(R.id.latitudeTx);
        boylamTx = view.findViewById(R.id.longtitudeTx);
        remindBt=view.findViewById(R.id.makeReminderBt);
        konumBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
                enlemTx.setText(String.format("%f",latitude));
                boylamTx.setText(String.format("%f",longtitude));
                remindBt.setClickable(true);
            }
        });
        remindBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLocationAlert();
            }
        });
        geofencingClient = LocationServices.getGeofencingClient(getActivity());
    return view;
    }
    private Geofence getGeofence(double lat, double lang, String key) {
        return new Geofence.Builder()
                .setRequestId(key)
                .setCircularRegion(lat, lang, GEOFENCE_RADIUS)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_DWELL)
                .setLoiteringDelay(10000)
                .build();
    }
    private GeofencingRequest getGeofencingRequest(Geofence geofence) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofence(geofence);
        return builder.build();
    }
    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(getActivity(), LocationIntentService.class);
        return PendingIntent.getService(getActivity(), 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
    private void addLocationAlertPreview(double latitude,double longtitude){
        try{
            String key = ""+latitude+"-"+longtitude;
            Geofence geofence = getGeofence(latitude,longtitude,key);
            geofencingClient.addGeofences(getGeofencingRequest(geofence),
                    getGeofencePendingIntent())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(),
                                        "Location alter has been added",
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getContext(),
                                        "Location alter could not be added",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        catch (SecurityException e){
            e.printStackTrace();
        }

    }
    public Location getLocationPreview(){
        try{
            locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(!isNetworkEnabled&&!isGPSEnabled){

            }
            else{
                canGetLocation=true;
                if(isNetworkEnabled){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,60000,10,this);
                    if(locationManager !=null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location !=null){
                            latitude = location.getLatitude();
                            longtitude = location.getLongitude();
                        }
                    }
                }
                if(isGPSEnabled){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,60000,10,this);
                    if(locationManager != null){
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(location != null){
                            latitude = location.getLatitude();
                            longtitude = location.getLongitude();
                        }
                    }
                }

            }
        }
        catch (SecurityException e){
            e.printStackTrace();
        }
        return  location;
    }
    public void addLocationAlert(){
        if(getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_DENIED){
            if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(getActivity(),"Konum bazlı hatırlatma için GPS izni verilmelidir",Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1001);
        }
        else{
            addLocationAlertPreview(latitude,longtitude);
        }
    }

    public void getLocation(){
        if(getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_DENIED){
            if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(getActivity(),"Konum bazlı hatırlatma için GPS izni verilmelidir",Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1001);
        }
        else{
            location= getLocationPreview();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

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
