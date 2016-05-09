package com.example.dellvenue11pro.testbindservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Random;

public class TestBindService extends Service {

    // 컴포넌트로 반환되는 IBinder
    private final IBinder mBinder = new LocalBinder();

    // IBinder 상속하여 이너클래스 생성, 이 클래스로 컴포넌트와 서비스를 bind한다
    public class LocalBinder extends Binder{
        TestBindService getService(){
            return TestBindService.this;
        }
    }

    // 난수 생성용 변수
    private final Random num = new Random();

    // 난수 생성 메소드
    public int getRandomNum(){
        return num.nextInt(100) + 1;
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
