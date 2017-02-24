package practice.Caffeine;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Created by Daniel on 06/02/2017.
 */
public class RegisterActivity extends AppCompatActivity {

    EditText etCountry;

    private static final int MY_PERMISSION_REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etName = (EditText) findViewById(R.id.etName);
        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etPhone = (EditText) findViewById(R.id.etPhone);
        final EditText etTownCity = (EditText) findViewById(R.id.etTownCity);
        etCountry = (EditText) findViewById(R.id.etCountry);
        final EditText etEmailAddress = (EditText) findViewById(R.id.etEmailAddress);
        final Button bRegister = (Button) findViewById(R.id.bRegister);

    int x = PackageManager.PERMISSION_GRANTED;
        int y = ContextCompat.checkSelfPermission(RegisterActivity.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION);

    if (ContextCompat.checkSelfPermission(RegisterActivity.this,
        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
        if(ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION)){
            ActivityCompat.requestPermissions(RegisterActivity.this,
            new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            try {
                etCountry.setText(getCountry(location.getLatitude(), location.getLongitude()));
            } catch (Exception e){
                e.printStackTrace();
                Toast.makeText(RegisterActivity.this, "Not found!", Toast.LENGTH_SHORT).show();
                }
            }
            }



    bRegister.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick (View v){

        final String name = etName.getText().toString();
        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();
        final String country = etCountry.getText().toString();
        final String city = etTownCity.getText().toString();
        final String email = etEmailAddress.getText().toString();

        final String phoneNum = etPhone.getText().toString();

        // Check that all of the edit text fields have been completed
        if (name.length() == 0 || username.length() == 0 || password.length() == 0 || country.length() == 0 || city.length() == 0 || email.length() == 0 || phoneNum.length() == 0) {
            Toast.makeText(RegisterActivity.this, "Please complete all of the user info.", Toast.LENGTH_LONG).show();
            return;         // if not completed return to start
        }

        final int phone = Integer.parseInt(etPhone.getText().toString());


        // stamps is a Map to record the String coffee shop and Integer cups. Integer cups will also hold the
        final Map<String, Integer> stamps = new HashMap<String, Integer>();
        stamps.put(" ", 0);


        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    Log.d("fixme", response);
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        RegisterActivity.this.startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("It looks like that username is already taken, please choose another and try again.")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };


        RegisterRequest registerRequest = new RegisterRequest(responseListener, username, name, password, phone, city, country, email);
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(registerRequest);


    }
    }

    );

}

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_REQUEST_LOCATION:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(RegisterActivity.this,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {try {
                            etCountry.setText(getCountry(location.getLatitude(), location.getLongitude()));
                        } catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "Not found!", Toast.LENGTH_SHORT).show();
                        }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Location Null", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(this, "No Permission granted!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    // get Country  Name
    public String getCountry (double lat, double lon){
        String country = "";
        Geocoder geocoder = new Geocoder(RegisterActivity.this, Locale.getDefault());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocation(lat, lon, 1);
            if(addressList.size() > 0){
                country = addressList.get(0).getCountryName();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return country;
    }
}
