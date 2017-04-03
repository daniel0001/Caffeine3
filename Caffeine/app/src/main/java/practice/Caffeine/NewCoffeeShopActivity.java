

package practice.Caffeine;


import android.content.Context;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
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

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import static com.google.android.gms.location.places.Place.TYPE_CAFE;
import static com.google.android.gms.location.places.Place.TYPE_CONVENIENCE_STORE;
import static com.google.android.gms.location.places.Place.TYPE_FOOD;
import static com.google.android.gms.location.places.Place.TYPE_GAS_STATION;
import static com.google.android.gms.location.places.Place.TYPE_GROCERY_OR_SUPERMARKET;
import static com.google.android.gms.location.places.Place.TYPE_GYM;
import static com.google.android.gms.location.places.Place.TYPE_LIQUOR_STORE;
import static com.google.android.gms.location.places.Place.TYPE_RESTAURANT;


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
    private List<Integer> placeTypes;
    private boolean gpsEnabled;
    private boolean wifiConnected;


    private int PLACE_PICKER_REQUEST = 1;

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

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

        // Check if location services and network connected
        gpsEnabled = false;
        wifiConnected = false;

        // Store shops to shopDB SQLite
        final DatabaseHelper myDB = new DatabaseHelper(this);

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getActiveNetworkInfo();

        if (!mWifi.isConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(NewCoffeeShopActivity.this);
            builder.setMessage("***Sign in to wifi*** \n \nLove Coffee allows you collect all your coffee loyalty card points in one place. \n\nTo use Love Coffee you must be signed in to the Coffee Shop's wifi as this is how Love Coffee knows to give you a loyalty point.  \n\nPlease check with a member of staff in your current shop what their Wifi login details and Wifi password are, then login to their wifi to proceed. \n\n Please note, you can only collect 1 point each day. Please visit www.lovecoffee.ie for more details.")
                    .setNegativeButton("Ok", null)
                    .create()
                    .show();
        }

        // get the current logged in wifi SSID and display in tvWifiSSID
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo;
        wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            wifiSSID = wifiInfo.getSSID();
            wifiMAC = getMacAddr();
        }
        tvWifiSSID.setText(wifiSSID);

        // when addCoffeeShop clicked, add the name of the Coffee Shop, wifi SSID, Time and Address to the table corresponding to the user_id
        // then start the Coffee Shop activity

        // final String wifiName = wifiSSID.replaceAll("^\"|\"$", "");

        // btnPlacePicker.onPickButtonClick();

        btnPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckConnectedHelper checkConnectedHelper = new CheckConnectedHelper(NewCoffeeShopActivity.this);
                if (!checkConnectedHelper.checkConnected(gpsEnabled, wifiConnected)) return;
                // Construct an intent for the place picker
                try {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    startActivityForResult(builder.build(NewCoffeeShopActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    // ...
                } catch (GooglePlayServicesNotAvailableException e) {
                    // ...
                }
            }


        });
        btnAddShop.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        CheckConnectedHelper checkConnectedHelper = new CheckConnectedHelper(NewCoffeeShopActivity.this);
                        if (!checkConnectedHelper.checkConnected(gpsEnabled, wifiConnected)) return;

                        final String shopName = tvShopName.getText().toString().replace("'", "").replace(",", "");
                        final String address = tvShopAddress.getText().toString().replace("'", "");

                        // Check if the editext fields are empty and return if so
                        if (shopName.length() == 0 || address.length() == 0) {
                            Toast.makeText(NewCoffeeShopActivity.this, "No shop name or shop address - please 'Find Shop' again", Toast.LENGTH_LONG).show();
                            // if not completed return to start
                            return;
                        }
                        // check if the selected Place is a restaurant or coffee shop using getPlaceTypes List shown here(https://developers.google.com/android/reference/com/google/android/gms/location/places/Place)
                        if (!(placeTypes.contains(TYPE_CAFE) || placeTypes.contains(TYPE_CONVENIENCE_STORE) || placeTypes.contains(TYPE_FOOD) || placeTypes.contains(TYPE_GAS_STATION) || placeTypes.contains(TYPE_GROCERY_OR_SUPERMARKET) || placeTypes.contains(TYPE_GYM) || placeTypes.contains(TYPE_LIQUOR_STORE) || placeTypes.contains(TYPE_RESTAURANT))) {
                            Toast.makeText(NewCoffeeShopActivity.this, "Oh Snap! This shop doesn't appear to be on our list as a valid Coffee Shop, Store or Restaurant. Please 'Find Shop' again", Toast.LENGTH_LONG).show();
                            // if not completed return to start
                            return;
                        }

                        //Create a response listener
                        Response.Listener<String> responseListener = new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                                // Must be online to add new shop (this preserves the remote SQL db as the master)
                                boolean shopExists;
                                try {
                                    Log.d("fixme", response);
                                    JSONObject jsonResponse;
                                    try {
                                        jsonResponse = new JSONObject(response);
                                        shopExists = jsonResponse.getBoolean("shopExists");
                                        shopID = jsonResponse.getInt("shop_ID");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(NewCoffeeShopActivity.this, "Error, please try again", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    myDB.getWritableDatabase();
                                    Shop newShop = new Shop();
                                    // Check if the shop already exists in the SQLite database
                                    // return if true
                                    if (checkDB(shopID) > 0) {
                                        Toast.makeText(NewCoffeeShopActivity.this, "Shop already registered in your list - please 'Find Shop' again or go back", Toast.LENGTH_LONG).show();
                                        return;
                                    } else {
                                        // Add shop to Shops Table
                                        try {
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
                                            myDB.addShop(newShop);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(NewCoffeeShopActivity.this, "Error, please try again", Toast.LENGTH_LONG).show();
                                            return;
                                        }

                                        Intent nextIntent = new Intent(NewCoffeeShopActivity.this, CoffeeShopsActivity.class);
                                        if (shopExists) {
                                            Toast.makeText(NewCoffeeShopActivity.this, "Shop added to your Love Coffee cards.", Toast.LENGTH_LONG).show();
                                        } else {
                                            nextIntent.putExtra("firstEverShopAdd", true);
                                        }
                                        nextIntent.putExtra("userID", userID);
                                        nextIntent.putExtra("name", name);
                                        NewCoffeeShopActivity.this.startActivity(nextIntent);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            // TODO: Below should be reviewed for security issues around SQL injection

                            private long checkDB(int shopID) {
                                SQLiteDatabase db = myDB.getReadableDatabase();
                                String sql = "SELECT * FROM shops WHERE shopID = " + shopID;
                                String[] args = {String.valueOf(shopID)};
                                long cnt = DatabaseUtils.queryNumEntries(db, "shops", "shopID = ?", args);
                                db.close();
                                return cnt;
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
            placeTypes = place.getPlaceTypes();
            placeID = place.getId();
        }
    }
}