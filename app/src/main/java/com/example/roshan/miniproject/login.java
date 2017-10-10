package com.example.roshan.miniproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void initLogin(View v) {
        EditText num = (EditText) findViewById(R.id.phoneNo);
        EditText pass = (EditText) findViewById(R.id.password);
        String phoneNo = num.getText().toString();
        int numLen = num.getText().length();
        String password = pass.getText().toString();
        int passLen = pass.getText().length();


        if (numLen != 10 && passLen < 5) {
            Toast.makeText(this, (CharSequence) "Enter valid Phone Number\nPassword must be more than 5 characters", Toast.LENGTH_SHORT).show();
        } else if (numLen != 10) {
            Toast.makeText(this, (CharSequence) "Enter valid Phone Number", Toast.LENGTH_SHORT).show();
            num.setText("");
        } else if (passLen < 5) {
            Toast.makeText(this, (CharSequence) "Password must be more than 5 characters", Toast.LENGTH_SHORT).show();
            pass.setText("");
        } else {
            /////Code for success
            Toast.makeText(this, (CharSequence) "Success !!!", Toast.LENGTH_LONG).show();
            Intent loginIntent = new Intent(this, UserContacts.class);
            loginIntent.putExtra("Phone Number", phoneNo);
            loginIntent.putExtra("Password", password);
            startActivity(loginIntent);
        }
    }
}
