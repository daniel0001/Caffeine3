package practice.Caffeine;



import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;


/**
 * Created by Daniel on 06/02/2017.
 */
public class RegisterActivity extends AppCompatActivity {


    private LocationManager locationManager;
    private LocationListener locationListener;
    private DatabaseHelper myDB;
    private boolean gpsEnabled;
    private boolean wifiConnected;

    private String lat;
    private String lng;
    private Integer LOCATION_REFRESH_TIME = 5000;
    private Integer LOCATION_REFRESH_DISTANCE = 0;
    private Button bRegister;
    private EditText etName;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etPhone;
    private EditText etEmailAddress;

    /**
     * Check if the database exist and can be read.
     *
     * @return true if it exists and can be read, false if it doesn't
     */
    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = (EditText) findViewById(R.id.etName);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmailAddress = (EditText) findViewById(R.id.etEmailAddress);
        bRegister = (Button) findViewById(R.id.bRegister);
        myDB = new DatabaseHelper(RegisterActivity.this);
        lat = null;
        lng = null;

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        registerButton();

        bRegister.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             // Check that location is switched on and internet connected

                                             CheckConnectedHelper checkConnectedHelper = new CheckConnectedHelper(RegisterActivity.this);
                                             if (!checkConnectedHelper.checkConnected(gpsEnabled, wifiConnected)) return;

                                             //Get latitude and longitude of user to find out where they are when registering
                                             locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, locationListener);
                                             Log.d("location : ", locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) + "");
                                             Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                             lat = String.valueOf(location.getLatitude());
                                             lng = String.valueOf(location.getLongitude());

                                             // Get the user's entries for personal details and identifiers
                                             final String name = etName.getText().toString();
                                             final String username = etUsername.getText().toString();
                                             final String password = etPassword.getText().toString();
                                             final String email = etEmailAddress.getText().toString();
                                             final String phone = etPhone.getText().toString();

                                             // Check that all of the edit text fields have been completed
                                             if (name.length() == 0 || username.length() == 0 || password.length() == 0 || email.length() == 0 || phone.length() == 0) {
                                                 Toast.makeText(RegisterActivity.this, "Please complete all of the user info.", Toast.LENGTH_LONG).show();
                                                 return;         // if not completed return to start
                                             }

                                             Response.Listener<String> responseListener = new Response.Listener<String>() {

                                                 @Override
                                                 public void onResponse(String response) {

                                                     try {
                                                         Log.d("fixme", response);
                                                         JSONObject jsonResponse = new JSONObject(response);
                                                         boolean usernameExists = jsonResponse.getBoolean("usernameExists");
                                                         Integer userID = jsonResponse.getInt("userID");

                                                         // Ask user for a new username if entry already exists in remote DB
                                                         if (usernameExists) {
                                                             etUsername.setText("");
                                                             etUsername.setHintTextColor(Color.RED);
                                                             etUsername.requestFocus();
                                                             AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                                             builder.setMessage("It looks like that username is already taken, please choose another and try again.")
                                                                     .setNegativeButton("Retry", null)
                                                                     .create()
                                                                     .show();
                                                         } else {
                                                             //check if myDB already exists
                                                             Boolean myDBExists = doesDatabaseExist(RegisterActivity.this, getDatabasePath(DatabaseHelper.databasePath).toString());
                                                             if (!myDBExists) {
                                                                 myDB.getWritableDatabase();
                                                                 User user = new User();
                                                                 user.setUsername(username);
                                                                 user.setUserID(userID);
                                                                 user.setName(name);
                                                                 user.setEmail(email);
                                                                 user.setPassword(password);
                                                                 user.setPhone(phone);
                                                                 user.setLat(lat);
                                                                 user.setLng(lng);
                                                                 myDB.addUser(user);
                                                             }
                                                             Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                             intent.putExtra("username", username);
                                                             finish();
                                                             RegisterActivity.this.startActivity(intent);
                                                         }
                                                     } catch (JSONException e) {
                                                         e.printStackTrace();
                                                     }

                                                 }
                                             };

                                             RegisterRequest registerRequest = new RegisterRequest(responseListener, username, name, password, phone, email, lat, lng);
                                             RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                                             queue.add(registerRequest);
                                         }
                                     }

        );
    }

    public void registerButton() {
        // check for permissions to use GPS location
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.INTERNET
                }, 10);
            }
            return;     // If no permission granted return
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 10:
                registerButton();
                break;
            default:
                break;
        }
    }


}
