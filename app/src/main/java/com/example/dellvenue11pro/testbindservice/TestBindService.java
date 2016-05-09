package com.example.dellvenue11pro.testbindservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class TestBindService extends Service {
    double latitude;
    double longtitude;
    double altitude;


    // 컴포넌트로 반환되는 IBinder
    private final IBinder mBinder = new LocalBinder();

    // IBinder 상속하여 이너클래스 생성, 이 클래스로 컴포넌트와 서비스를 bind한다
    public class LocalBinder extends Binder{
        TestBindService getService(){
            return TestBindService.this;
        }
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
        Toast.makeText(TestBindService.this, "Create Service", Toast.LENGTH_SHORT).show();
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(TestBindService.this, "Bind Service", Toast.LENGTH_SHORT).show();
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
