package com.example.gestionfactura;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tapadoo.alerter.Alerter;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textView;
    private ProgressBar progressBar;
    private BroadcastReceiver mReciver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("4B","OnCreate");
        textView =findViewById(R.id.txt_dato);
        progressBar = findViewById(R.id.id_progressbar);

        mReciver = new Bateria();
        registerReceiver(mReciver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
       // getWindow().setBackgroundDrawableResource(android.R.color.white);


    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mReciver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        Log.e("4B","OnStart");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("4B","onResume");
    }

    @Override
    public void onClick(View v) {
        //int i = v.getId();

    }



    private void showAlertWithButtons() {
        Alerter.create(MainActivity.this)
                .setTitle("Alert Title")
                .setText("Alert text...").enableSwipeToDismiss().setBackgroundColorRes(R.color.colorAccent).show();
    }





    @Override
    protected void onPause() {
        super.onPause();
        Log.e("4B","onPause");
        if(isDestroyed()){
            Toast.makeText(this, "Preparata putito", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mReciver);
        Log.e("4B","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("4B","onDestroy");
    }

    private class Bateria extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //batterymanager y extra nevel conocer el nivel de la bateria
            //el 0 es un vlor por defecto
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            //asigna el nivel de la bateria concadenar con el string bateria
            textView.setText(level + " " + getString(R.string.bateria));
            // darle el valor al progressbar
            progressBar.setProgress(level);
        }
    }


}
