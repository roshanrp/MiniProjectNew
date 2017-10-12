package com.example.roshan.miniproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by roshan on 11/10/17.
 */

public class MyDBHandler extends SQLiteOpenHelper {

    private SQLiteDatabase sqLiteDatabase;
    private SQLiteOpenHelper sqLiteOpenHelper;

    private static final String COL_ID = "_ID";
    private static final String COL_MYNUMBER = "MyNumber";
    private static final String COL_PASSWORD = "Password";
    private static final String COL_CONTACT1 = "Contact1";
    private static final String COL_CONTACT2 = "Contact2";
    private static final String TABLE = "account";
    private static final String DB_NAME = "accountDB.db";
    private static final int DB_VERSION = 1;


    public MyDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE account (_ID INTEGER, MyNumber INTEGER, Password TEXT, Contact1 INTEGER, Contact2 INTEGER);");
        db.execSQL("INSERT INTO account (_ID, MyNumber, Password, Contact1) VALUES (1, 1, '1', 1);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS account");
        onCreate(db);
    }

    public void enterUserDetails (String pNumber, String pPassword) {
        String password = pPassword;
        String number = pNumber;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE account SET MyNumber = '" + number + "', Password = '" + password + "' WHERE _ID = '1';");
        db.close();
    }

    public void enterContacts (String pNum1, String pNum2) {
        String number1 = pNum1;
        String number2 = pNum2;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE account SET Contact1 = '" + number1 + "', Contact2 = '" + number2 + "' WHERE _ID = '1';");
        db.close();
    }

    public String getPassword () {
        String password = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM account", null);
        while (cursor.moveToNext()) {
            password = password + cursor.getString(cursor.getColumnIndex("Password"));
        }

        db.close();
        return password;
    }

    public String getContact1 () {
        String contact = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM account", null);
        while (cursor.moveToNext()) {
            contact = contact + cursor.getString(cursor.getColumnIndex("Contact1"));
        }
        db.close();
        return contact;
    }

    public String getContact2 () {
        String contact = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM account", null);
        while (cursor.moveToNext()) {
            contact = contact + cursor.getString(cursor.getColumnIndex("Contact2"));
        }
        db.close();
        return contact;
    }
}