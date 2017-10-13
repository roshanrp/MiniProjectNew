package com.example.roshan.miniproject;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaCodec;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class SMSBroadcastReceiver extends BroadcastReceiver {

    String phoneNo, password, contact1, contact2;
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELECOM_SERVICE);
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        Camera camera;
        Parameters parameters;

        if (bundle == null)
            return;

        try {
            Object[] pdus = (Object[]) bundle.get("pdus");
            for (int i = 0; i < pdus.length; i++) {
                SmsMessage smsMessage;
                if (Build.VERSION.SDK_INT < 23) {
                    smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                } else {
                    smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i], bundle.getString("format"));
                }
                String msgSender = smsMessage.getDisplayOriginatingAddress();
                String msgBody = smsMessage.getDisplayMessageBody();
                MyDBHandler myDBHandler = new MyDBHandler(context);

                password = myDBHandler.getPassword();
                contact1 = myDBHandler.getContact1();
                contact2 = myDBHandler.getContact2();

                if (msgSender.contains(contact1) | msgSender.contains(contact2)) {
                        String[] splitArray = msgBody.split("\\#", 2);
                        if (splitArray.length == 2) {
                            if (splitArray[0].equals(password)) {
                                msgBody = splitArray[1];

                               // Toast.makeText(context, "Passed", Toast.LENGTH_SHORT).show();
                                if (Pattern.matches(".?ring.?", msgBody)) {
                                    audioManager.setStreamVolume(AudioManager.RINGER_MODE_NORMAL, audioManager.getStreamMaxVolume(AudioManager.RINGER_MODE_NORMAL), 0);
                                    final Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
                                    r.play();
                                } else if (Pattern.matches(".?turn\\son\\swifi.?", msgBody)) {
                                    wifiManager.setWifiEnabled(true);
                                } else if (Pattern.matches(".?turn\\soff\\swifi.?", msgBody)) {
                                    wifiManager.setWifiEnabled(false);
                                } else if (Pattern.matches(".?turn\\son\\sdata.?", msgBody)) {
                                    try {
                                        final Class conmanClass = Class.forName(connectivityManager.getClass().getName());
                                        final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
                                        iConnectivityManagerField.setAccessible(true);
                                        final Object iConnectivityManager = iConnectivityManagerField.get(connectivityManager);
                                        final Class iConnectivityManagerClass = Class.forName(
                                                iConnectivityManager.getClass().getName());
                                        final Method setMobileDataEnabledMethod = iConnectivityManagerClass
                                                .getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
                                        setMobileDataEnabledMethod.setAccessible(true);

                                        setMobileDataEnabledMethod.invoke(iConnectivityManager, true);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else if (Pattern.matches(".?turn\\soff\\sdata.?", msgBody)) {




                                } else if (Pattern.matches(".?silent\\smode.?", msgBody)) {
                                    audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                                } else if (Pattern.matches(".?vibrate\\smode.?", msgBody)) {
                                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                                } else if (Pattern.matches(".?general\\smode.?", msgBody)) {
                                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                                } else if (Pattern.matches(".?turn\\son\\sflashlight.?", msgBody)) {
                                    boolean hasFlash = context.getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
                                    if(hasFlash) {
                                        Toast.makeText(context, "Flash", Toast.LENGTH_SHORT).show();
                                        camera = Camera.open();
                                        parameters = camera.getParameters();
                                        parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
                                        camera.setParameters(parameters);
                                        camera.startPreview();
                                    }
                                } else if (Pattern.matches(".?turn\\soff\\sflashlight.?", msgBody)) {
                                    Toast.makeText(context, "Flash", Toast.LENGTH_SHORT).show();
                                    camera = Camera.open();
                                    parameters = camera.getParameters();
                                    parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
                                    camera.setParameters(parameters);
                                    camera.stopPreview();
                                    camera.release();
                                    camera = null;
                                }
                            }
                        }
                }
            }
        } catch (NullPointerException e) {
            Toast.makeText(context, (CharSequence) "Null Pointer Exception", Toast.LENGTH_SHORT).show();
        }
    }
}