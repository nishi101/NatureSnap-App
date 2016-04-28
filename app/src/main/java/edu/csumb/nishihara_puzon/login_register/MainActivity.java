package edu.csumb.nishihara_puzon.login_register;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static User user = null;
    private Uri fileUri;
    public static Uri bullshit;
    public static final int MEDIA_TYPE_IMAGE = 1;

    /**
     * Create a file Uri for saving an image or video
     */
    private static File getOutputMediaFileUri() {
        return getOutputMediaFile();
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "NatureSnap");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("NatureSnap", "failed to create directory");
                //return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }

    public Button bLogout, bCamera;
    public TextView username, views, uploads;


    private static final int SELECTED_PICTURE=1;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            File userd = new File(getFilesDir(), "user.dat");
            if (userd.exists()) {
                FileInputStream fos = new FileInputStream(userd);
                ObjectInputStream obj_in = new ObjectInputStream(fos);
                user = (User) obj_in.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (authenticate() == true) {
            displayUserDetails();
        }

        username = (TextView) findViewById(R.id.editText);
        views = (TextView) findViewById(R.id.editText2);
        uploads = (TextView) findViewById(R.id.editText3);
        bLogout = (Button) findViewById(R.id.bLogout);
        bCamera = (Button) findViewById(R.id.bCamera);

        bCamera.setOnClickListener(this);
        bLogout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bLogout:
                File userd = new File(getFilesDir(), "user.dat");
                userd.delete();
                Intent loginIntent = new Intent(this, Login.class);
                startActivity(loginIntent);
                break;
            case R.id.bCamera:
                loadCamera();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private boolean authenticate() {
        if (user == null) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            return false;
        }
        return true;
    }

    private void displayUserDetails() {
        if (username != null) {
            username.setText(user.username);
        }

        //etAge.setText(user.age + "");
    }

    private void loadCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri f = Uri.fromFile(getOutputMediaFileUri());
        try {
            File file = new File(getFilesDir(), "imagePath.dat");
            if (file.exists())
                file.delete();
            if (!file.createNewFile())
                    throw new IOException("Unable to create file");
            ObjectOutputStream obj = new ObjectOutputStream( new FileOutputStream(file) );
            obj.writeObject(f);
            bullshit = f;
        }catch(Exception e){
            e.printStackTrace();
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, f); // set the image file name
        //Save image path temporarily for derps sake
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Uri imageUri = null;
                try {
                    File file = new File(getFilesDir(), "imagePath.dat");
                    ObjectInputStream obj_in = new ObjectInputStream(new FileInputStream(file));
                    imageUri = (Uri) obj_in.readObject();
                    file.delete();
                }catch(Exception e){
                    e.printStackTrace();
                }
                if (bullshit != null) {
                    Toast.makeText(this, "Image saved to:\n" + bullshit, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, Upload.class);
                    intent.putExtra("file", bullshit.toString());
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Image Capture Failed", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
            } else if (resultCode == RESULT_CANCELED) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Image Capture Failed", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

}