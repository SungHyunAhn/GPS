package com.example.dellvenue11pro.testbindservice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class TestBindActivity extends Activity {

    TestBindService mService; // bind 타입 서비스
    public boolean mBound = false; // 서비스 연결 상태

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_bind);

        //첫번째 버튼, 서비스에서 GPS 정보 받아옴
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBound) {
                    double latitude = mService.getLatitude();
                    double longtitude = mService.getLongtitude();
                    double alititude = mService.getAltitude();
                    Toast.makeText(TestBindActivity.this, "lat = " + latitude + "\nlng = " + longtitude + "\nalt = " + alititude, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 두번째 버튼, unbindService() 호출로 서비스 연결 해제
        Button btn2 = (Button)findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBound) {
                    mBound = false;
                    unbindService(mConn);
                }
            }
        });

        // 세번째 버튼, bindService() 호출로 서비스 연결
        Button btn3 = (Button)findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mBound){
                    bind();
                }
            }
        });
    }

    // bind 할때 사용
    public void bind(){
        bindService(new Intent(this, TestBindService.class), mConn, Context.BIND_AUTO_CREATE);
    }

    // 최초로 액티비티가 실행될 때 자동으로 bind
    public void onStart(){
        super.onStart();
        bind();
    }

    // 액티비티가 정지될 때 bind 해제
    public void onStop(){
        super.onStop();
        if(mBound){
            unbindService(mConn);
        }
    }

    // ServiceConnection 객체 생성으로 bind타입 서비스와 액티비티간 연결
    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TestBindService.LocalBinder binder = (TestBindService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };
}
