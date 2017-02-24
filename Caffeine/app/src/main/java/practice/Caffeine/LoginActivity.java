package practice.Caffeine;

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


public class LoginActivity extends AppCompatActivity {
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Create the DB
        myDB = new DatabaseHelper(this);

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


                                // Enter Data into SQLite DB 'myDB'
                               boolean isInserted =  myDB.insertData(username, userID, name, password, phone, city, country, email);
                                if(isInserted){
                                    Toast.makeText(LoginActivity.this,"Data Inserted", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(LoginActivity.this,"Data not Inserted", Toast.LENGTH_LONG).show();
                                }

                                // Set up new intent CoffeeShopsActivity
                                Intent intent = new Intent(LoginActivity.this, CoffeeShopsActivity.class);
                                intent.putExtra("userID", userID);
                                intent.putExtra("name", name);

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


