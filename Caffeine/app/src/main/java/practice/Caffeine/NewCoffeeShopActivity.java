

package practice.Caffeine;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;


public class NewCoffeeShopActivity extends AppCompatActivity {

    private String name;
    private String userID;
    private Button btnPlacePicker;
    private Button btnAddShop;
    private TextView tvShopName;
    private TextView tvWifiSSID;
    private TextView tvShopAddress;
    private String wifiSSID;
    private String shopWeb;
    private String lat;
    private String lng;
    private String phoneNum;
    private String placeID;
    private ArrayList<Shop> shopList;
    private int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_coffee_shop);
        final Intent intent = getIntent();
        name = intent.getStringExtra("name");
        userID = intent.getStringExtra("userID");
        btnPlacePicker = (Button) findViewById(R.id.bPickPlace);
        btnAddShop = (Button) findViewById(R.id.btnAddShop);
        tvWifiSSID = (TextView) findViewById(R.id.tvWifiSSID);
        tvShopName = (TextView) findViewById(R.id.tvShopName);
        tvShopAddress = (TextView) findViewById(R.id.tvShopAddress);
        wifiSSID = null;
        // Store shops to file
        shopList = new ArrayList<>();
        populateList(shopList);


        // get the current logged in wifi SSID and display in tvWifiSSID
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo;
        wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            wifiSSID = wifiInfo.getSSID();
        }
        tvWifiSSID.setText(wifiSSID);

        // when addCoffeeShop clicked, add the name of the Coffee Shop, wifi SSID, Time and Address to the table corresponding to the user_id
        // then start the Coffee Shop activity

        final String wifiName = wifiSSID.replaceAll("^\"|\"$", "");

        // Check if location services and network connected
        LocationManager lm = (LocationManager) NewCoffeeShopActivity.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(NewCoffeeShopActivity.this);
            dialog.setMessage(NewCoffeeShopActivity.this.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(NewCoffeeShopActivity.this.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    NewCoffeeShopActivity.this.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton(NewCoffeeShopActivity.this.getString(R.string.Cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }

        // To Do Hide buttons if no location or network connected


        // btnPlacePicker.onPickButtonClick();

        btnPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Construct an intent for the place picker
                try {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    startActivityForResult(builder.build(NewCoffeeShopActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    // ...
                } catch (GooglePlayServicesNotAvailableException e) {
                    // ...
                }
                //    onActivityResult(PLACE_PICKER_REQUEST, RESULT_OK, getIntent() );

            }


        });
        btnAddShop.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        final String shopName = tvShopName.getText().toString();
                        final String address = tvShopAddress.getText().toString();
                        // Check if the editext fields are empty and return if so
                        if (shopName.length() == 0 || address.length() == 0) {
                            Toast.makeText(NewCoffeeShopActivity.this, "No shop name or shop adress - please 'Find Shop' again", Toast.LENGTH_LONG).show();
                            // if not completed return to start
                            return;
                        }
                        if (shopList.contains(shopName)) {
                            Toast.makeText(NewCoffeeShopActivity.this, "Shop already registered in your list - please 'Find Shop' again or go back to list", Toast.LENGTH_LONG).show();
                            return;
                        }
                        //Create a response listener
                        Response.Listener<String> responseListener = new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                                try {
                                    Log.d("fixme", response);
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean shopExists = jsonResponse.getBoolean("shopExists");
                                    if (shopExists) {
                                        Toast.makeText(NewCoffeeShopActivity.this, "Shop added to your Love Coffee cards.", Toast.LENGTH_LONG).show();
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(NewCoffeeShopActivity.this);
                                        builder.setMessage("Yay! It looks like you are the first person to register this shop for Love Coffee. That's great - but we will have to check if they accept 'Love Coffee' loyalty points. Please check with a member of staff. If they are curious they can visit www.lovecoffee.ie for merchant details.")
                                                .setNegativeButton("Ok", null)
                                                .create()
                                                .show();
                                    }
                                    // Write shop details to file and start NewCoffeeShopActivity
                                    TextView shopName = (TextView) findViewById(R.id.tvShopName);
                                    String name = shopName.getText().toString();

                                    Shop shop = new Shop(name, 1, 0);
                                    shopList.add(shop);
                                    writeToFile(shopList);

                                        Intent nextIntent = new Intent(NewCoffeeShopActivity.this, CoffeeShopsActivity.class);
                                        nextIntent.putExtra("userID", userID);
                                        nextIntent.putExtra("name", name);
                                        NewCoffeeShopActivity.this.startActivity(nextIntent);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        };
                        NewCoffeeShopRequest newCoffeeShopRequest = new NewCoffeeShopRequest(responseListener, shopName, address, wifiName, lat, lng, shopWeb, phoneNum, userID, placeID);
                        RequestQueue queue = Volley.newRequestQueue(NewCoffeeShopActivity.this);
                        queue.add(newCoffeeShopRequest);

                    }

                });

    }

    private ArrayList<Shop> populateList(ArrayList<Shop> list) {
        Scanner scan = new Scanner(getResources().openRawResource(R.raw.myshoplist));
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            int i = line.indexOf(",");
            int j = line.indexOf(",", i);
            String name = line.substring(0, i);
            String visits = line.substring(i + 1, j);   // convert to int
            String thumbnail = line.substring(j + 1, line.length()); // convert to int
            Shop shop = new Shop(name, visits, thumbnail);
            list.add(shop);
        }
        scan.close();
        return list;
    }

    // write shopList to file
    private void writeToFile(ArrayList<Shop> list) {
        PrintStream out = null;
        try {
            out = new PrintStream(openFileOutput("raw/myshoplist.txt", MODE_PRIVATE));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < list.size(); i++) {
            Shop shop = list.get(i);
            out.println(shop);
        }
        out.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {

            Place place = PlacePicker.getPlace(NewCoffeeShopActivity.this, data);
            tvShopName.setText(place.getName().toString());
            tvShopAddress.setText(place.getAddress().toString());
            shopWeb = place.getWebsiteUri().toString();
            lat = String.valueOf(place.getLatLng().latitude);
            lng = String.valueOf(place.getLatLng().longitude);
            phoneNum = place.getPhoneNumber().toString();
            placeID = place.getId().toString();
        }
    }
}