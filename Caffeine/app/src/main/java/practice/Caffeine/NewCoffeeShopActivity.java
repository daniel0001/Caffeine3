

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


public class NewCoffeeShopActivity extends AppCompatActivity {

    private String name;
    private Integer userID;
    private Button btnPlacePicker;
    private Button btnAddShop;
    private TextView tvShopName;
    private TextView tvWifiSSID;
    private TextView tvShopAddress;
    private String wifiSSID;
    private String wifiMAC;
    private String shopWeb;
    private String lat;
    private String lng;
    private String phone;
    private String placeID;
    private int shopID;


    private int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_coffee_shop);
        final Intent intent = getIntent();
        name = intent.getStringExtra("name");
        userID = intent.getIntExtra("userID", 0);
        btnPlacePicker = (Button) findViewById(R.id.bPickPlace);
        btnAddShop = (Button) findViewById(R.id.btnAddShop);
        tvWifiSSID = (TextView) findViewById(R.id.tvWifiSSID);
        tvShopName = (TextView) findViewById(R.id.tvShopName);
        tvShopAddress = (TextView) findViewById(R.id.tvShopAddress);
        wifiSSID = null;
        wifiMAC = null;

        // Store shops to shopDB SQLite
        final DatabaseHelper myDB = new DatabaseHelper(this);

        // get the current logged in wifi SSID and display in tvWifiSSID
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo;
        wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            wifiSSID = wifiInfo.getSSID();
            wifiMAC = wifiInfo.getMacAddress();
        }
        tvWifiSSID.setText(wifiSSID);

        // when addCoffeeShop clicked, add the name of the Coffee Shop, wifi SSID, Time and Address to the table corresponding to the user_id
        // then start the Coffee Shop activity

        // final String wifiName = wifiSSID.replaceAll("^\"|\"$", "");

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
                                    myDB.getWritableDatabase();
                                    Shop newShop = new Shop();

                                    //  SHOPS_COLUMNS = {_ID, COLUMN_NAME_SHOPID, COLUMN_NAME_NAME, COLUMN_NAME_ADDRESS, COLUMN_NAME_WEBSITE, COLUMN_NAME_LAT, COLUMN_NAME_LNG, COLUMN_NAME_PLACEID, COLUMN_NAME_WIFIMAC, COLUMN_NAME_WIFISSID};
                                    shopID = jsonResponse.getInt("shop_ID");
                                    newShop.setShopID(shopID);
                                    newShop.setName(shopName);
                                    newShop.setAddress(address);
                                    newShop.setWebsite(shopWeb);
                                    newShop.setLat(lat);
                                    newShop.setLng(lng);
                                    newShop.setPlaceID(placeID);
                                    newShop.setWifiMAC(wifiMAC);
                                    newShop.setWifiSSID(wifiSSID);

                                    if (myDB.getAllShops().contains(newShop)) {
                                        Toast.makeText(NewCoffeeShopActivity.this, "Shop already registered in your list - please 'Find Shop' again or go back to list", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    // Add shop to Shops Table
                                    myDB.addShop(newShop);
                                    Intent nextIntent = new Intent(NewCoffeeShopActivity.this, CoffeeShopsActivity.class);
                                    nextIntent.putExtra("userID", userID);
                                    nextIntent.putExtra("name", name);
                                    NewCoffeeShopActivity.this.startActivity(nextIntent);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        };

                        NewCoffeeShopRequest newCoffeeShopRequest = new NewCoffeeShopRequest(responseListener, shopName, address, wifiSSID, wifiMAC, lat, lng, shopWeb, phone, userID, placeID);
                        RequestQueue queue = Volley.newRequestQueue(NewCoffeeShopActivity.this);
                        queue.add(newCoffeeShopRequest);

                    }

                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED && data != null && requestCode == PLACE_PICKER_REQUEST) {
            Place place = PlacePicker.getPlace(NewCoffeeShopActivity.this, data);
            Log.d("place: ", place.toString());
            tvShopName.setText(place.getName().toString());
            tvShopAddress.setText(place.getAddress().toString());
            if (place.getWebsiteUri() != null) {
                shopWeb = place.getWebsiteUri().toString();
            } else {
                shopWeb = "URL not available";
            }
            lat = String.valueOf(place.getLatLng().latitude);
            lng = String.valueOf(place.getLatLng().longitude);
            phone = place.getPhoneNumber().toString();
            placeID = place.getId();
        }
    }
}