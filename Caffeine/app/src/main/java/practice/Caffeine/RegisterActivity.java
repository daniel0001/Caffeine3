package practice.Caffeine;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    private DatabaseHelper myDB;

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

        final EditText etName = (EditText) findViewById(R.id.etName);
        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etPhone = (EditText) findViewById(R.id.etPhone);
        final EditText etTownCity = (EditText) findViewById(R.id.etTownCity);
        final EditText etCountry = (EditText) findViewById(R.id.etCountry);
        final EditText etEmailAddress = (EditText) findViewById(R.id.etEmailAddress);
        final Button bRegister = (Button) findViewById(R.id.bRegister);

        myDB = new DatabaseHelper(RegisterActivity.this);


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





        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    Log.d("fixme", response);
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean usernameExists = jsonResponse.getBoolean("usernameExists");
                    boolean locationExists = jsonResponse.getBoolean("locationExists");
                    Integer userID = jsonResponse.getInt("userID");
                    Integer locationID = jsonResponse.getInt("locationID");

                    if(usernameExists) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("It looks like that username is already taken, please choose another and try again.")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                    /* Extension here would be to use geocoder to find current city and country to populate locations instead of reading off predefined location table*/
                    if (!locationExists) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("It looks like this location doesn't exist.")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }

                    if (!usernameExists && locationExists) {
                        //check if myDB already exists
                        String dbName = "myDB.db";
                        Boolean myDBExists = doesDatabaseExist(RegisterActivity.this, getDatabasePath(DatabaseHelper.databasePath).toString());
                        if (!myDBExists) {
                            myDB.getWritableDatabase();
                            myDB.insertDataUserTable(username, userID, name, password, phone, locationID, email);
                        }

                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        RegisterActivity.this.startActivity(intent);
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


}
