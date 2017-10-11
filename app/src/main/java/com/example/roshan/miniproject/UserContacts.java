package com.example.roshan.miniproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class UserContacts extends AppCompatActivity {

    MyDBHandler myDBHandler;
    String phoneNo;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_contacts);

        Bundle userCredentials = getIntent().getExtras();
        if (userCredentials == null)
            return;

        phoneNo = userCredentials.getString("Phone Number");
        password = userCredentials.getString("Password");
        this.myDBHandler = new MyDBHandler(this);
    }

    public void SubmitContacts (View v) {
        EditText num1 = (EditText) findViewById(R.id.contact1);
        EditText num2 = (EditText) findViewById(R.id.contact2);
        String phoneNo1 = num1.getText().toString();
        String phoneNo2 = num2.getText().toString();
        int num1len = num1.getText().length();
        int num2len = num2.getText().length();

        if(num1len == 10 && num2len == 10) {
            //SUCCESS
            Intent broadcastIntent = new Intent(this, SMSBroadcastReceiver.class);
            broadcastIntent.putExtra("My Phone Number", phoneNo);
            broadcastIntent.putExtra("Password", password);
            broadcastIntent.putExtra("Phone Number 1", phoneNo1);
            broadcastIntent.putExtra("Phone Number 2", phoneNo2);
            //startActivity(broadcastIntent);
            this.myDBHandler.enterContacts(phoneNo1, phoneNo2);

        } else if (num1len != 10) {
            Toast.makeText(this, (CharSequence) "Please Enter 1st Number Valid", Toast.LENGTH_SHORT).show();
            num1.setText("");
        } else if (num2len != 10) {
            Toast.makeText(this, (CharSequence) "Please Enter 2nd Number Valid", Toast.LENGTH_SHORT).show();
            num2.setText("");
        }



    }
}
