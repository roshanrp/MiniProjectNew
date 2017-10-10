package com.example.roshan.miniproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SMSBroadcastReceiver extends AppCompatActivity implements BroadcastReceiver {

    String phoneNo, password, phoneNo1, phoneNo2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsbroadcast_receiver);

        Bundle params = getIntent().getExtras();
        if (params == null)
            return;

        phoneNo = params.getString("My Phone Number");
        password = params.getString("Password");
        phoneNo1 = params.getString("Phone Number 1");
        phoneNo2 = params.getString("Phone Number 2");

        Toast.makeText(this, phoneNo+password+phoneNo1+phoneNo2, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
