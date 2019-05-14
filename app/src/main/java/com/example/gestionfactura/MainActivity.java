package com.example.gestionfactura;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("4B","OnCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("4B","OnStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("4B","onResume");
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
        Log.e("4B","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("4B","onDestroy");
    }


}
