package practice.Caffeine;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
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


public class NewCoffeeShopActivity extends AppCompatActivity {

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_coffee_shop);
        final Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String userID = intent.getStringExtra("userID");
        Button btnAddShop = (Button) findViewById(R.id.bCreateCoffeeShop);
        final EditText etWifiSSID = (EditText) findViewById(R.id.etWifiSSID);
        final EditText etShopName = (EditText) findViewById(R.id.etCoffeeShopName);
        final EditText etShopCity = (EditText) findViewById(R.id.etCoffeeShopCity);
        final EditText etShopCountry = (EditText) findViewById(R.id.etCoffeeShopCountry);
        String wifiSSID = null;


        // get the current logged in wifi SSID and display in etWifiSSID
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo;
        wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            wifiSSID = wifiInfo.getSSID();
        }
        Toast.makeText(NewCoffeeShopActivity.this, wifiSSID, Toast.LENGTH_LONG).show();
        etWifiSSID.setText(wifiSSID);

        // when addCoffeeShop clicked, add the name of the Coffee Shop, wifi SSID, Time and Address to the table corresponding to the user_id
        // then start the Coffee Shop activity

        final String wifiName = wifiSSID.replaceAll("^\"|\"$", "");

        btnAddShop.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        final String shopName = etShopName.getText().toString();
                        final String shopCity = etShopCity.getText().toString();
                        final String shopCountry = etShopCountry.getText().toString();
                        // Check if the editext fields are empty and return if so
                        if (shopName.length() == 0 || shopCity.length() == 0 || shopCountry.length() == 0) {
                            Toast.makeText(NewCoffeeShopActivity.this, "Please complete all of the Shop info so that we can add it to your list", Toast.LENGTH_LONG).show();
                            return;         // if not completed return to start
                        }

                        // Create ShopDetails for php storing of new column in user_id table remotely




                        //Create a response listener
                        Response.Listener<String> responseListener = new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.d("fixme", response);
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");

                                    if (success) {
                                        Intent intent = new Intent(NewCoffeeShopActivity.this, CoffeeShopsActivity.class);
                                        NewCoffeeShopActivity.this.startActivity(intent);
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(NewCoffeeShopActivity.this);
                                        builder.setMessage("It looks like this shop is already registered.")
                                                .setNegativeButton("Retry", null)
                                                .create()
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };


                        Intent nextIntent = new Intent(NewCoffeeShopActivity.this, CoffeeShopsActivity.class);
                        nextIntent.putExtra("userID", userID);
                        nextIntent.putExtra("name", name);
                        NewCoffeeShopActivity.this.startActivity(intent);


                        NewCoffeeShopRequest newCoffeeShopRequest = new NewCoffeeShopRequest(responseListener, userID, shopName, shopCity, shopCountry, wifiName);
                        RequestQueue queue = Volley.newRequestQueue(NewCoffeeShopActivity.this);
                        queue.add(newCoffeeShopRequest);
                    }

                });


    }
}