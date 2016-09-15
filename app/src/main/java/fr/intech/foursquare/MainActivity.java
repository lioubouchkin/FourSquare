package fr.intech.foursquare;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;

    private static final String LOG_TAG = "MainActivity";
    private final int REQUEST_PERMISSION_PHONE_STATE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // create an instance of Google API Client
        this.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        Log.d(LOG_TAG, "GoogleApiClient is created");
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        if (this.mGoogleApiClient != null) {
            this.mGoogleApiClient.connect();
            Log.d(LOG_TAG,
                    "onStart() :\n"+"Connection established - " + this.mGoogleApiClient.isConnected());
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (this.mGoogleApiClient != null){
            this.mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    /**
     *
     * @param bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // no permission for ACCESS_FINE_LOCATION
        if (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(LOG_TAG,
                    ".onConnected() :\n" + "Permission ACCESS_FINE_LOCATION is not granted, \n" +
                            "call ActivityCompat.requestPermissions()");
            this.showPhoneStatePermission();
        } else {
            Log.d(LOG_TAG, "Permission ACCESS_FINE_LOCATION is granted");
        }
        //
        this.mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                this.mGoogleApiClient);
        if (this.mLastLocation != null) {
            Log.d("Latitude", String.valueOf(this.mLastLocation.getLatitude()));
            Log.d("Longitude", String.valueOf(this.mLastLocation.getLongitude()));
        } else {
            Log.d(LOG_TAG, "mLastLocation is null");
        }

//        new Thread(new Runnable() {
//            public void run() {
//                FourSquareSearch search = new FourSquareSearch("40.7", "74");
//                search.buildQuery();
//                search.readURL();
//
//                BufferedReader in = search.getIn();
//                String line;
//
//                try {
//                    while ((line = in.readLine()) != null) {
//                        Log.d("JSonParser : ", line);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(LOG_TAG, "Connection to Google Play Services suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Connection to Google Play Services failed." +
                "ERROR : " + connectionResult.getErrorCode());
        /**
         * TODO: treat the connection failure on the ConnectionResult
         * https://developers.google.com/android/guides/api-client#Starting : Handle connection failures
         * */
    }

    /**
     * Callback for the result from requesting permissions.
     * This method is invoked for every call on requestPermissions
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(LOG_TAG, " entered onRequestPermissionsResult()");
        switch (requestCode) {
            case REQUEST_PERMISSION_PHONE_STATE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                    this.mGoogleApiClient.connect();

                    Log.d(LOG_TAG,
                            "onRequestPermissionsResult() :\n"+"Connection established - " + this.mGoogleApiClient.isConnected());
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    /**
     * method alerts the user of a permission to attribute
     */
    private void showPhoneStatePermission() {
        Log.d(LOG_TAG, " entered showPhoneStatePermission()");
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                this.showExplanation("Permission Needed", "Rationale", Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSION_PHONE_STATE);
            } else {
                this.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSION_PHONE_STATE);
            }
        } else {
            Toast.makeText(MainActivity.this, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *
     * @param title
     * @param message
     * @param permission
     * @param permissionRequestCode
     */
    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        Log.d(LOG_TAG, " entered showExplanation()");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        Log.d(LOG_TAG, " entered requestPermission()");
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }
}
