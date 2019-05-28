package com.example.gestionfactura;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class CaptureActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQUEST_PERMISSION_STORAGE_SAVE = 101;
    private final int REQUEST_PERMISSION_STORAGE_DELETE = 101;


    //valor constante para la camara
    static final int REQUEST_IMAGE_CAPTURE =1;

    //para la creacion de imagen
    Bitmap bitmap;


    //constante para la gestion de archivos y carpetas
    private  final String FOLDER_NAME = "UOCImageApp";
    private  final String File_NAME = "image.jpg";

    private Button buttonCamera;
    private ImageView imageView;
    private TextView txtMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        buttonCamera = findViewById(R.id.button);
        imageView = findViewById(R.id.picture);
        txtMessage = findViewById(R.id.message);

        buttonCamera.setOnClickListener(this);

        cargarImagen();
    }

    //PROCEDIMIENTO QUE CARGA LA IMAGEN DESDE LA SDCARD SI ES QUE EXISTE
    private void cargarImagen() {
        String myImage = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+FOLDER_NAME+"/"+File_NAME;
        //OBTENEMOS LA DIRECCION DE LA IMAGEN
        File imagen = new File(myImage);
        if(imagen.exists()){
            //LEER LA IMAGEN DE KA SDCARD UTILIZANDO PICASSO LIBRARY
            Picasso.get().load(imagen).into(imageView);
            txtMessage.setVisibility(View.INVISIBLE);
        }else {
            txtMessage.setVisibility(View.VISIBLE);
        }
    }




    private boolean hasPermissionToWrite(){
        //Para los permisos si deja escribir en la memoria externa
        return ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delete){
            //onDeleteMenuTap();
            return true;
        }else if(item.getItemId() == R.id.save){
            //onSaveMenuTap();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

    }
}
