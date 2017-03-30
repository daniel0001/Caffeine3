package practice.Caffeine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
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


public class LoginActivity extends AppCompatActivity {
    DatabaseHelper myDB;
    private int userID;
    private boolean gpsEnabled;
    private boolean wifiConnected;
    private String username;
    private String password;
    private Button bLogin;
    private Button bRegister;
    private EditText etUsername;
    private EditText etPassword;
    private Toast toast;


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
        setContentView(R.layout.activity_login);
        gpsEnabled = false;
        wifiConnected = false;

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        bRegister = (Button) findViewById(R.id.bRegister);

        // New intent to collect username passed back from Register Activity
        Intent registerIntent = getIntent();
        if (registerIntent.hasExtra("username")) {
            username = registerIntent.getStringExtra("username");
            String upperString = username.substring(0, 1).toUpperCase() + username.substring(1);
            username = upperString;
            etUsername.setText(username);
            toast = Toast.makeText(LoginActivity.this, "Congrats! You have registered for Love Coffee. Now let's dive in and start collecting those lovely loyalty points from yur favourite coffee shops!.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        bRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Check that location is switched on and internet connected
                CheckConnectedHelper checkConnectedHelper = new CheckConnectedHelper(LoginActivity.this);
                if (!checkConnectedHelper.checkConnected(gpsEnabled, wifiConnected)) return;

                // Start next intent Register Actvity
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });


        // listen to the button being clicked
        bLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Create the DB
                myDB = new DatabaseHelper(LoginActivity.this);
                // Check that location is switched on and internet connected
                CheckConnectedHelper checkConnectedHelper = new CheckConnectedHelper(LoginActivity.this);
                if (!checkConnectedHelper.checkConnected(gpsEnabled, wifiConnected)) return;

                // Check if the editext fields are empty and return if so
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                if ( username.length() == 0 || password.length() == 0){
                    toast = Toast.makeText(LoginActivity.this, "Please complete all of the Login info.", Toast.LENGTH_LONG);
                    toast.show();
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
                                userID = jsonResponse.getInt("user_id");  // user_id is the name in the database but userID in app
                                String phone = jsonResponse.getString("phone");
                                String email = jsonResponse.getString("email");
                                String lat = jsonResponse.getString("lat");
                                String lng = jsonResponse.getString("lat");

                                // If myDB already Exists then wipe clean - Data should be fresh from remote DB
                                // to preserve accuracy and avoid syncing issues - An extension would be
                                // to enable offline use by syncing more often between SQLite db (local) and SQL db (remote)
                                // But this version is MVP to get to launch
                                Boolean myDBExists = doesDatabaseExist(LoginActivity.this, getDatabasePath(DatabaseHelper.databasePath).toString());
                                if (myDBExists) {
                                    myDB.deleteAllShops();
                                    myDB.deleteAllUsers();
                                    myDB.deleteAllVisits();
                                }

                                // Enter user Data into SQLite DB 'myDB'
                                User user = new User();
                                user.setUsername(username);
                                user.setUserID(userID);
                                user.setName(name);
                                user.setEmail(email);
                                user.setPassword(password);
                                user.setPhone(phone);
                                user.setLat(lat);
                                user.setLng(lng);
                                myDB.getWritableDatabase();
                                myDB.addUser(user);
                                Log.d("userID: ", myDB.getUser(1).getUserID() + "");

                                // Sync the shops table with remote DB


                                // Test to see if data inserted into table
                                if (myDB.getUser(1).getUsername().equals(user.getUsername())) {
                                        Toast.makeText(LoginActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                                    }

                                // Set up new intent CoffeeShopsActivity
                                Intent intent = new Intent(LoginActivity.this, CoffeeShopsActivity.class);
                                intent.putExtra("userID", userID);
                                intent.putExtra("name", name);

                                //Open CoffeeShopsActivity
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


