package com.example.gestionfactura;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CaptureActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQUEST_PERMISSION_STORAGE_SAVE = 101;
    private final int REQUEST_PERMISSION_STORAGE_DELETE = 100;


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
            onDeleteMenuTap();

            return true;
        }else if(item.getItemId() == R.id.save){
            onSaveMenuTap();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        if(v == buttonCamera){
            dispatchTakePictureIntent();
        }
    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(bitmap);
            txtMessage.setVisibility(View.INVISIBLE);
        }
    }


   public void onSaveMenuTap(){
       if(!hasPermissionToWrite()){
           ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSION_STORAGE_SAVE);
       }else{
           if(imageView.getDrawable() !=null){
               createFolder();
               String stogareDir = Environment.getExternalStorageDirectory() + "/UOCImageApp";
               createImageFile(stogareDir, this.File_NAME, bitmap);
           }else{
               Toast.makeText(this, "tome unafoto primero", Toast.LENGTH_SHORT).show();
           }
       }
   }


    private void createFolder() {
       String myFolder = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+FOLDER_NAME;
       File folder = new File(myFolder);
       if(!folder.exists()){
           if(!folder.mkdir()){
               Toast.makeText(this, "no se pude crear", Toast.LENGTH_SHORT).show();
           }else{
               Toast.makeText(this, "se puedp crear", Toast.LENGTH_SHORT).show();
           }
       }
    }

    private void createImageFile(String stogaDir,String fileName, Bitmap bitmap){
        try{
            File myFile = new File(stogaDir,fileName);
            FileOutputStream stream = new FileOutputStream(myFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
            Toast.makeText(this, "iMAGEN GUARDADA", Toast.LENGTH_SHORT).show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    private void onDeleteMenuTap(){
        if(!hasPermissionToWrite()){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSION_STORAGE_DELETE);
        }else{
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        try{
                            deleteImageFile();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Eliminar imagen").setMessage("quieres eliminar esta imagen?").setPositiveButton("YES",dialogClickListener)
                    .setNegativeButton("No",dialogClickListener).show();
        }
    }


    private void deleteImageFile() throws  IOException{
        File storage = new File(Environment.getExternalStorageDirectory() + "/UOCImageApp");
        File image = new File(storage + "/" + this.File_NAME);

        if(image.exists()){
            image.delete();
            Toast.makeText(this, "file dileted", Toast.LENGTH_SHORT).show();
            imageView.setImageResource(0);
            txtMessage.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(this, "image no encotrada", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        switch (requestCode){
            case REQUEST_PERMISSION_STORAGE_DELETE:{
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                }else {
                    Toast.makeText(this, "permiso revocado", Toast.LENGTH_SHORT).show();
                }
            }
            case REQUEST_PERMISSION_STORAGE_SAVE:{
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    createFolder();
                    String stoageDir = Environment.getExternalStorageDirectory()+"/UOCImageApp";
                    createImageFile(stoageDir,File_NAME,bitmap);
                }else{
                    Toast.makeText(this, "permiso de guarda denegado", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
