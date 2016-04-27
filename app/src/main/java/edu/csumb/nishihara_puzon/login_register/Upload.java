package edu.csumb.nishihara_puzon.login_register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import net.naturesnap.apiclient.Interface;
import net.naturesnap.apiclient.http.requests.ImageUpload;
import net.naturesnap.apiclient.http.results.Code;

import java.io.File;

public class Upload extends Activity {
    private LocationManager locationManager;
    public String id;
    public ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Code c = (Code) Interface.request(new ImageUpload(), this.getIntent().getExtras().getString("file"), String.valueOf(locationGPS.getLatitude()), String.valueOf(locationGPS.getLongitude()));
            String[] uP = c.getResponse().split(":");
            if (uP[0].equals("success")) {
                // uploaded
                id = uP[1];
                image = (ImageView) findViewById(R.id.imageView7);
                image.setImageURI(Uri.fromFile(new File(this.getIntent().getExtras().getString("file"))));
            } else {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_upload, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
