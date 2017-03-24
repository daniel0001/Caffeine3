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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;


public class LoginActivity extends AppCompatActivity {
    DatabaseHelper myDB;
    private Integer userID;
    private boolean gpsEnabled;
    private boolean wifiConnected;

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

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bLogin = (Button) findViewById(R.id.bLogin);
        final TextView tvRegisterLink = (TextView) findViewById(R.id.tvRegisterHere);

        tvRegisterLink.setOnClickListener(new View.OnClickListener() {

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
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
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
                                userID = jsonResponse.getInt("user_id");  // user_id is the name in the database but userID in app
                                String phone = jsonResponse.getString("phone");
                                String location = jsonResponse.getString("location");
                                String email = jsonResponse.getString("email");

                                // If myDB exists Enter Data into SQLite DB 'myDB'
                                String dbName = "myDB.db";
                                Boolean myDBExists = doesDatabaseExist(LoginActivity.this, getDatabasePath(DatabaseHelper.databasePath).toString());
                                if (!myDBExists) {
                                    myDB.getWritableDatabase();
                                    User user = new User();
                                    user.setUsername(username);
                                    user.setUserID(userID);
                                    user.setName(name);
                                    user.setEmail(email);
                                    user.setPassword(password);
                                    user.setPhone(phone);
                                    user.setLocation(location);

                                    if (myDB.addUser(user)) {
                                        Toast.makeText(LoginActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                                    }

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


