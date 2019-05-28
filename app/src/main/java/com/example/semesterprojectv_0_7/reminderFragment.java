package com.example.semesterprojectv_0_7;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import static android.content.Context.LOCATION_SERVICE;



public class reminderFragment extends Fragment implements LocationListener {
    private TextView baslikTx;
    private TextView tarihTx;
    private TextView adresTx;
    private EditText oncelikBx;
    private Button dosyaBt;
    private Button updateBt;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    private Location location;
    private LocationManager locationManager;
    Double latitude,longtitude;
    private Data data;
    private Data fileData;
    public reminderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);
        getLocation();
        baslikTx = view.findViewById(R.id.baslikTx);
        tarihTx = view.findViewById(R.id.tarihsaatTx);
        adresTx=view.findViewById(R.id.adresTx);
        oncelikBx = view.findViewById(R.id.oncelikTx);
        Intent i = getActivity().getIntent();
        data = (Data) i.getSerializableExtra("deneme");
        baslikTx.setText( data.getBaslik() );
     //  Log.d("RFrag",data.getBaslik()+" "+data.getText()+" ");
        final File file = new File(getContext().getFilesDir(),data.getBaslik());
        if(file.exists()){
            fileData = new Data();
            fileData= loadFile();
            Log.d("RFrag","burda "+ fileData.getBaslik()+" "+fileData.getText()+" ");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss ");
          if(data.getDate()!=null&&data.getLocation()!=null){

              String tarihSaat = sdf.format(data.getDate());
              tarihTx.setText(tarihSaat);
              adresTx.setText(String.format("%f %f",data.getLocation().getLatitude(),data.getLocation().getLongitude()));

          }
          else{
              String currentDateandTime = sdf.format(new Date());
              tarihTx.setText(currentDateandTime);
          }

        }
        oncelikBx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncelikBx.getText().clear();
            }
        });
        if(data != null){
            baslikTx.setText(data.getBaslik());

        }
        dosyaBt = view.findViewById(R.id.dosyaekleBt);
        updateBt = view.findViewById(R.id.Guncelle);
        adresTx.setText(String.format("%f %f",latitude,longtitude));
        updateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileData.setBaslik( baslikTx.getText().toString() );
                fileData.setLocation(location);
                fileData.setDate( new Date());
                fileData.setPriority(oncelikBx.getText().toString());
                writeFile();
            }
        });


        return view;
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
    public void writeFile()
    {
        Context context = getContext();

        try
        {
            FileOutputStream fileOutput = context.openFileOutput(baslikTx.getText().toString(), Context.MODE_PRIVATE);
            ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
            objectOutput.writeObject(fileData);
            objectOutput.close();
            fileOutput.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public Data loadFile()
    {
        Data newData = new Data();
        try
        {
            FileInputStream fileInput = getContext().openFileInput("profileSaves.bin");
            ObjectInputStream objectInput = new ObjectInputStream(fileInput );
           newData = (Data) objectInput.readObject();
//            Log.i("data1 ",newData.getBaslik()+" "+ newData.getText());
            objectInput.close();
            fileInput.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    return newData;
    }
}
