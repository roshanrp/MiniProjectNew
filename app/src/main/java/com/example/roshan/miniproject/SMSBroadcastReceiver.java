package com.example.roshan.miniproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaCodec;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.regex.Pattern;

public class SMSBroadcastReceiver extends BroadcastReceiver {

    String phoneNo, password, contact1, contact2;
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

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

                                Toast.makeText(context, "Passed", Toast.LENGTH_SHORT).show();
                                if (Pattern.matches(".?ring.?", msgBody)) {
                                    audioManager.setStreamVolume(AudioManager.RINGER_MODE_NORMAL, audioManager.getStreamMaxVolume(AudioManager.RINGER_MODE_NORMAL), 0);
                                    //audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                                    final Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
                                    r.play();
                                } else if (Pattern.matches(".?turn\\son\\swifi.?", msgBody)) {
                                    wifiManager.setWifiEnabled(true);
                                } else if (Pattern.matches(".?turn\\soff\\swifi.?", msgBody)) {
                                    wifiManager.setWifiEnabled(false);
                                } else if (Pattern.matches(".?turn\\son\\sdata.?", msgBody)) {

                                } else if (Pattern.matches(".?turn\\son\\sdata.?", msgBody)) {

                                } else if (Pattern.matches(".?silent\\smode.?", msgBody)) {
                                    audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                                } else if (Pattern.matches(".?vibrate\\smode.?", msgBody)) {
                                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                                } else if (Pattern.matches(".?general\\smode.?", msgBody)) {
                                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
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


/*
try {
                                    camera = Camera.open();
                                    Camera.Parameters p = camera.getParameters();
                                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                                    camera.setParameters(p);
                                    camera.startPreview();
                                }catch(Exception e){
                                    e.printStackTrace();
                                }



 final ConnectivityManager conman = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
                                final Class conmanClass = Class.forName(conman.getClass().getName());
                                final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
                                iConnectivityManagerField.setAccessible(true);
                                final Object iConnectivityManager = iConnectivityManagerField.get(conman);
                                final Class iConnectivityManagerClass =  Class.forName(iConnectivityManager.getClass().getName());
                                final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
                                setMobileDataEnabledMethod.setAccessible(true);

                                setMobileDataEnabledMethod.invoke(iConnectivityManager, true);

 */