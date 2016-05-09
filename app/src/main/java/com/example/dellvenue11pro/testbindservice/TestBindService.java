package com.example.dellvenue11pro.testbindservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class TestBindService extends Service {
    double latitude;
    double longtitude;
    double altitude;

    private LocationManager locationManager;
    private String locationProvider;
    // 컴포넌트로 반환되는 IBinder
    private final IBinder mBinder = new LocalBinder();

    // IBinder 상속하여 이너클래스 생성, 이 클래스로 컴포넌트와 서비스를 bind한다
    public class LocalBinder extends Binder{
        TestBindService getService(){
            return TestBindService.this;
        }
    }

    public static Criteria getCriteria(){
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(true);
        criteria.setSpeedRequired(true);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        return criteria;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongtitude(){
        return longtitude;
    }

    public double getAltitude(){
        return altitude;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Toast.makeText(TestBindService.this, "Create Service", Toast.LENGTH_SHORT).show();
    }

    // 마지막 위치를 받아와 출력하는 메소드
    public void getLastLocation(){
        Location myLocation = getLastKnownLocation();

        latitude = myLocation.getLatitude();
        longtitude = myLocation.getLongitude();
        altitude = myLocation.getAltitude();

        Log.e("getData", "lat = " + latitude + " lng = " + longtitude + " alt = " + altitude);

    }

    // 마지막 위치를 확인하여 그 위치를 반환하는 메소드, 최초실행시 locationManager.getLastKnownLocation(provider) 사용하면 null 반환으로 인한 회피법
    private Location getLastKnownLocation() {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        Location l = null;
        for (String provider : providers) {
            try {
                l = locationManager.getLastKnownLocation(provider);
                Log.e("getLastKnownLocation", "bestProvider : " + locationProvider);
            }
            catch(SecurityException e){
                Log.e("getLastKnownLocation", "Permission Denied");
            }
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(TestBindService.this, "Bind Service", Toast.LENGTH_SHORT).show();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            Toast.makeText(TestBindService.this, "GPS is OFF, Please GPS ON", Toast.LENGTH_SHORT).show();

        locationProvider = locationManager.getBestProvider(getCriteria(), true);

        getLastLocation();

        try {
            locationManager.requestLocationUpdates(locationProvider, 5000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Toast.makeText(TestBindService.this, "Location Changed", Toast.LENGTH_SHORT).show();

                    latitude = location.getLatitude();
                    longtitude = location.getLongitude();
                    altitude = location.getAltitude();

                    Log.e("onLocationChanged", "bestProvider : " + locationProvider);
                    Log.e("getData", "lat = " + latitude + " lng = " + longtitude + " alt = " + altitude);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    switch (status) {
                        case LocationProvider.AVAILABLE:
                            //Toast.makeText(TestBindService.this, provider +" Available", Toast.LENGTH_SHORT).show();
                            Log.e("LocationProvider : ", provider + " Available");
                            break;
                        case LocationProvider.OUT_OF_SERVICE:
                            //Toast.makeText(TestBindService.this, provider +" Out of Service", Toast.LENGTH_SHORT).show();
                            Log.e("LocationProvider : ", provider + " Out of Service");
                            break;
                        case LocationProvider.TEMPORARILY_UNAVAILABLE:
                            //Toast.makeText(TestBindService.this, provider +" Service Stop", Toast.LENGTH_SHORT).show();
                            Log.e("LocationProvider : ", provider + " Service Stop");
                            break;
                    }
                }

                @Override
                public void onProviderEnabled(String provider) {
                    Toast.makeText(TestBindService.this, provider + " Provider Enabled", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onProviderDisabled(String provider) {
                    Toast.makeText(TestBindService.this, provider + " Provider Disenabled", Toast.LENGTH_SHORT).show();
                }
            });
            Log.e("requestLocationUpdate", "Permission Allowed");
        }
        catch (SecurityException e) {
            Log.e("requestLocationUpdate", "Permission Denied");
        }

        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(TestBindService.this, "Unbind Service", Toast.LENGTH_SHORT).show();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(TestBindService.this, "Destroy Service", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}
