package practice.Caffeine;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Instantiate new local SQL DB = UserDetails
        UserDetailsContract.UserDetailsDBHelper mDbHelper = new UserDetailsContract.UserDetailsDBHelper(this);
        // Gets the data repository in write mode
       final SQLiteDatabase db = mDbHelper.getWritableDatabase();


        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bLogin = (Button) findViewById(R.id.bLogin);
        final TextView tvRegisterLink = (TextView) findViewById(R.id.tvRegisterHere);

        tvRegisterLink.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });


        // listen to the button being clicked
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();


                // Check if the editext fields are empty and return if so
                if ( username.length() == 0 || password.length() == 0){
                    Toast.makeText(LoginActivity.this, "Please complete all of the Login info.", Toast.LENGTH_LONG).show();
                    return;         // if not completed return to start
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("fixme", response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");


                            if (success) {
                                // Get data from JSONresponse from database

                                String name = jsonResponse.getString("name");
                                String userID = jsonResponse.getString("user_id");  // user_id is the name in the database but userID in app
                                int phone = jsonResponse.getInt("phone");
                                String country = jsonResponse.getString("country");
                                String city = jsonResponse.getString("city");
                                String email = jsonResponse.getString("email");
                                // pass data below to UserAreaActivity
                                Intent intent = new Intent(LoginActivity.this, CoffeeShopsActivity.class);
                                intent.putExtra("username", username);
                                intent.putExtra("userID", userID);
                                intent.putExtra("name", name);
                                intent.putExtra("password", password);
                                intent.putExtra("phone", phone);
                                intent.putExtra("city", city);
                                intent.putExtra("country", country);
                                intent.putExtra("email", email);


                                // Insert User data from JSON Response into DB
                                // Create a new map of values, where column names are the keys
                                ContentValues values = new ContentValues();
                                values.put(UserDetailsContract.UserDetails.COLUMN_NAME_USERNAME, username);
                                values.put(UserDetailsContract.UserDetails.COLUMN_NAME_USERID, userID);
                                values.put(UserDetailsContract.UserDetails.COLUMN_NAME_NAME , name);
                                values.put(UserDetailsContract.UserDetails.COLUMN_NAME_PASSWORD, password);
                                values.put(UserDetailsContract.UserDetails.COLUMN_NAME_PHONE, phone);
                                values.put(UserDetailsContract.UserDetails.COLUMN_NAME_CITY, city);
                                values.put(UserDetailsContract.UserDetails.COLUMN_NAME_COUNTRY, country);
                                values.put(UserDetailsContract.UserDetails.COLUMN_NAME_EMAIL, email);


                                // Insert the new row
                                db.insert(UserDetailsContract.UserDetails.TABLE_NAME, null, values);


                                //Open UserAreaActivity
                                LoginActivity.this.startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Login Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(responseListener, username, password);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);

            }
        });


    }

    }


