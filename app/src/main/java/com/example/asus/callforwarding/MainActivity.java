package com.example.asus.callforwarding;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


public class MainActivity extends AppCompatActivity {

    Button buttonCallForwardOn;
    Button buttonCallForwardOff;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((AdView) findViewById(R.id.adView)).loadAd(new AdRequest.Builder().build());
        MobileAds.initialize(this,"ca-app-pub-3940256099942544~3347511713");


        buttonCallForwardOn = (Button) findViewById(R.id.On);
        buttonCallForwardOn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                callforward("*21*01765644975#"); // 0123456789 is the number you want to forward the calls.
            }
        });

        buttonCallForwardOff = (Button) findViewById(R.id.Off);
        buttonCallForwardOff.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                callforward("#21#");
            }
        });



    }

    private void callforward(String callForwardString)
    {
        CallState phoneListener = new CallState();
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        Intent intentCallForward = new Intent(Intent.ACTION_CALL);
        Uri mmiCode = Uri.fromParts("tel", callForwardString, "#");
        intentCallForward.setData(mmiCode);


        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){


            return;

        }
        startActivity(intentCallForward);
    }

    public class CallState extends PhoneStateListener
    {
        private boolean isPhoneCalling = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber)
        {
            if (TelephonyManager.CALL_STATE_RINGING == state)
            {
                // phone ringing

                for(int i=0;i<100;i++){

                    System.out.println("phone ringing");
                }
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state)
            {
                // active

                for(int i=0;i<100;i++){

                    System.out.println("phone offhook");
                }
                isPhoneCalling = true;
            }


            if (TelephonyManager.CALL_STATE_IDLE == state)
            {

                for(int i=0;i<100;i++){

                    System.out.println("phone IDLE");
                }
                // run when class initial and phone call ended, need detect flag
                // from CALL_STATE_OFFHOOK
                if (isPhoneCalling)
                {
                    // restart app
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    isPhoneCalling = false;
                }
            }
        }
    }









}
