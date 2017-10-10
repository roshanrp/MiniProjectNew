package com.example.roshan.miniproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class UserContacts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_contacts);

        Bundle userCredentials = getIntent().getExtras();
        if (userCredentials == null)
            return;

        String phoneNo = userCredentials.getString("Phone Number");
        String password = userCredentials.getString("Password");

        Toast.makeText(this, phoneNo + password, Toast.LENGTH_SHORT).show();
    }
}
