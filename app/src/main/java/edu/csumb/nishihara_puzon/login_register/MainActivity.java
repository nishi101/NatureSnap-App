package edu.csumb.nishihara_puzon.login_register;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static File savedImage;
    private Uri fileUri;
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
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }

    Button bLogout, bCamera;
    TextView username, views, uploads;


    private static final int SELECTED_PICTURE=1;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                UserLocalStore.user=null;
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
        if (authenticate() == true) {
            displayUserDetails();
        }
    }

    private boolean authenticate() {
        if (UserLocalStore.user == null) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            return false;
        }
        return true;
    }

    private void displayUserDetails() {
        User user = UserLocalStore.user;
        username.setText(user.name);
        //etAge.setText(user.age + "");
    }

    private void loadCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        savedImage = getOutputMediaFileUri(); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(savedImage)); // set the image file name
        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Image saved to:\n" + savedImage, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, Upload.class);
                intent.putExtra("file", savedImage.toURI().toString());
                startActivity(intent);
            } else if (resultCode == RESULT_CANCELED) {
                savedImage = null;
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Image Capture Failed", Toast.LENGTH_LONG).show();
                savedImage = null;
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

}