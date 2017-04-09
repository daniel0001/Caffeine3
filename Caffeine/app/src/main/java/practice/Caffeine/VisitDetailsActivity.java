package practice.Caffeine;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Daniel on 23/03/2017.
 */

public class VisitDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int MAX_NUMBER_POINTS = 9;   // The Max + 1 Visit is when the user redeems the free coffee
    private String userName;
    private int userID;
    private String shopName;
    private String shopPhone;
    private String shopAddress;
    private int pointCount;
    private TextView tvShopName;
    private TextView tvShopPhone;
    private TextView tvShopAddress;
    private ImageView ivVisitCount;
    private Button bAddPoint;
    private Button bClaimCoffee;
    private Button bTakePhotoShare;
    private Double lat;
    private Double lng;
    private LatLng shopLatLng;
    private GoogleMap map;
    private int shopID;
    private Visit visit;
    private Integer LOCATION_REFRESH_TIME = 5000;
    private Integer LOCATION_REFRESH_DISTANCE = 5;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private DatabaseHelper myDB;
    private boolean gpsEnabled;
    private boolean wifiConnected;
    private Shop shop;
    private List<Visit> visitList;
    private String wifiSSID;
    private String wifiMAC;
    private Double GPS_ERROR = 0.000008; // Error check for GPS location
    private int ACCURACY = 15;
    private Double userLat;
    private Double userLng;
    private String currentDate;

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
        setContentView(R.layout.activity_visit_details);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        // intent data passed in by the shopadapter thumbnail.onClickListener
        Intent intent = getIntent();
        userName = intent.getStringExtra("name");
        userID = intent.getIntExtra("userID", 0);
        lat = intent.getDoubleExtra("lat", 0);
        lng = intent.getDoubleExtra("lng", 0);
        pointCount = intent.getIntExtra("pointCount", 0);
        shopLatLng = new LatLng(lat, lng);
        gpsEnabled = false;
        wifiConnected = false;

        tvShopName = (TextView) findViewById(R.id.tvShopName);
        tvShopAddress = (TextView) findViewById(R.id.tvShopAddress);
        tvShopPhone = (TextView) findViewById(R.id.tvShopPhone);
        ivVisitCount = (ImageView) findViewById(R.id.ivPointsWide);

        shopName = intent.getStringExtra("shopName");
        shopAddress = intent.getStringExtra("shopAddress");
        shopPhone = intent.getStringExtra("shopPhone");
        shopID = intent.getIntExtra("shopID", 0);
        tvShopName.setText(shopName);
        tvShopPhone.setText(shopPhone);
        tvShopAddress.setText(shopAddress);

        myDB = new DatabaseHelper(VisitDetailsActivity.this);
        myDB.getReadableDatabase();
        visitList = myDB.getAllVisits();
        shop = myDB.getShop(shopID);
        myDB.close();


        // Button Add Point - check if in shop using GPS, check if already at 9 points,
        // check if point already collected in same calendar day (only 1 point per day allowed)
        // User NOT able to add point if not redeemed when MAZ_NUMBER_POINTS reached - achieved by redirecting
        // to RedeemCoffeeActivity onClick

        bAddPoint = (Button) findViewById(R.id.bAddPoint);
        if (pointCount == MAX_NUMBER_POINTS) {
            bAddPoint.setTextColor(R.color.light_grey);
            bAddPoint.setBackgroundColor(R.color.dark_grey);
        }


  /*      locationChecker();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location.getAccuracy() > ACCURACY) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, locationListener);
                    return;
                }


                //Then remove the updates once done
                locationManager.removeUpdates(locationListener);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        Log.d("location : ", locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) + "");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, locationListener);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        location.setAccuracy(Criteria.ACCURACY_FINE);
        userLat = location.getLatitude();
        userLng = location.getLongitude();
*/

        bAddPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pointCount == MAX_NUMBER_POINTS) {
                    Toast toast = Toast.makeText(VisitDetailsActivity.this, "It's time to claim that coffee.", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                Boolean locCheck = false; // This checks if the GPS matches (if it does then add point and move back to CoffeeShopActivity)
                Boolean sameDateCheck = true; // Checks if current date is same as lastvisit date given the shopID(true if same)
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 1);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                currentDate = sdf.format(calendar.getTime());
                Log.d("CurrentDate: ", currentDate);

                int x = visitList.size();
                for (int i = 1; i < visitList.size(); i++) {
                    visit = visitList.get(x - i);
                    String visitDate = visit.getDate();
                    visitDate = visitDate.substring(0, visitDate.indexOf(" "));

                    if (visit.getShopID() == shopID && !visitDate.equals(currentDate)) {
                        sameDateCheck = false;
                        break;
                    }
                }


                // Check that location is switched on and internet connected
                CheckConnectedHelper checkConnectedHelper = new CheckConnectedHelper(VisitDetailsActivity.this);
                if (!checkConnectedHelper.checkConnected(gpsEnabled, wifiConnected)) return;


                //Get latitude and longitude of user to find out where they are when registering
                // TODO: issue with GPS accuracy so compromise to 3 decimal places
                // There is a secondary issue with not all shops having wifi and logging in being restrictive
                // Alternative solution would be to use a QR code in the store for user to verify with
                // However this limits growth to those stores that have QR codes creating admin overheads


                // remove minus signs from lat and lng
 /*               Double latX = lat;
                Double lngX = lng;
                Double userLatX = userLat;
                Double userLngX = userLng;
                if (latX < 0) latX *= -1;
                if (lngX < 0) lngX *= -1;
                if (userLatX < 0) userLatX *= -1;
                if (userLngX < 0) userLngX *= -1;

                // check location to within a percentage of Error
                Double latMax = latX + (latX * GPS_ERROR);
                Double latMin = latX - (latX * GPS_ERROR);
                Double lngMax = lngX + (lngX * GPS_ERROR);
                Double lngMin = lngX - (lngX * GPS_ERROR);
                if ((userLatX > latMin && userLatX < latMax) && (userLngX > lngMin && userLngX < lngMax))
                    locCheck = true;

                if (sameDateCheck) {
                    String[] funnyNegStrings = new String[]{ // Strings to tell the user that they don't have enough points
                            getString(R.string.funny_neg_visit_1),
                            getString(R.string.funny_neg_visit_2),
                            getString(R.string.funny_neg_visit_3),
                            getString(R.string.funny_neg_visit_4),
                            getString(R.string.funny_neg_visit_5)
                    };
                    Random random = new Random();
                    int max = 4;
                    int y = random.nextInt(max);
                    AlertDialog.Builder builder = new AlertDialog.Builder(VisitDetailsActivity.this);
                    builder.setMessage(funnyNegStrings[y])
                            .setNegativeButton(R.string.ok, null)
                            .create()
                            .show();
                }

                if (!locCheck && sameDateCheck) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(VisitDetailsActivity.this);
                    DialogInterface.OnClickListener wifiListener;
                    builder.setMessage("**** Oh no, something went wrong **** \n\nError: You must be in the shop to collect a loyalty point.\n\nLocation data from your phone\'s GPS doesn\'t match with this shop \n\nIf this is an error try switching your Location off and on to refresh. \n\n" + shopName + " GPS Data:\n\nLat: " + lat + "\nLng: " + lng + "\n\nYour Location: \n\nLat: " + userLat + "\nLng: " + userLng)
                            .setNegativeButton(R.string.cancel, null)
                            .create()
                            .show();
                }
               */

                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.d("fixme", response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {

                                Intent intent = new Intent(VisitDetailsActivity.this, CoffeeShopsActivity.class);
                                intent.putExtra("name", userName);
                                intent.putExtra("userID", userID);
                                finish();
                                VisitDetailsActivity.this.startActivity(intent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                };
                AddVisitRequest addVisitRequest = new AddVisitRequest(listener, String.valueOf(shopID), String.valueOf(userID));
                RequestQueue queue = Volley.newRequestQueue(VisitDetailsActivity.this);
                queue.add(addVisitRequest);
            }

        });


        bClaimCoffee = (Button) findViewById(R.id.bRedeemPoints);
        if (pointCount > MAX_NUMBER_POINTS) {
            bClaimCoffee.setTextColor(R.color.light_grey);
            bClaimCoffee.setBackgroundColor(R.color.dark_grey);
        }
        if (pointCount == MAX_NUMBER_POINTS) {
            bClaimCoffee.setTextColor(R.color.white);
            bClaimCoffee.setTextColor(R.color.red);
        }

        bClaimCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pointCount == MAX_NUMBER_POINTS) {
                    Intent intent = new Intent(VisitDetailsActivity.this, RedeemCoffeeActivity.class);
                    intent.putExtra("name", userName);
                    intent.putExtra("userID", userID);
                    intent.putExtra("shopName", shopName);
                    intent.putExtra("shopAddress", shopAddress);
                    intent.putExtra("shopPhone", shopPhone);
                    intent.putExtra("lat", lat);
                    intent.putExtra("lng", lng);
                    intent.putExtra("pointCount", pointCount);
                    intent.putExtra("shopID", shopID);
                    finish();
                    VisitDetailsActivity.this.startActivity(intent);

                } else {
                    String[] funnyNegStrings = new String[]{ // Strings to tell the user that they don't have enough points
                            getString(R.string.funny_neg_1),
                            getString(R.string.funny_neg_2),
                            getString(R.string.funny_neg_3),
                            getString(R.string.funny_neg_4),
                            getString(R.string.funny_neg_5)
                    };
                    Random random = new Random();
                    int max = 4;
                    int x = random.nextInt(max);
                    AlertDialog.Builder builder = new AlertDialog.Builder(VisitDetailsActivity.this);
                    builder.setMessage(funnyNegStrings[x])
                            .setNegativeButton("Ok", null)
                            .create()
                            .show();
                }
            }
        });

        // Create array of shopImages and change image depending on number of visits from pointCount
        int[] shopImages = new int[]{
                R.drawable.cup_grey_grid_0_wide,
                R.drawable.cup_grey_grid_heart_1_wide,
                R.drawable.cup_grey_grid_heart_2_wide,
                R.drawable.cup_grey_grid_heart_3_wide,
                R.drawable.cup_grey_grid_heart_4_wide,
                R.drawable.cup_grey_grid_heart_5_wide,
                R.drawable.cup_grey_grid_heart_6_wide,
                R.drawable.cup_grey_grid_heart_7_wide,
                R.drawable.cup_grey_grid_heart_8_wide,
                R.drawable.cup_grey_grid_heart_9_wide,
        };
        Glide.with(VisitDetailsActivity.this).load(shopImages[pointCount]).into(ivVisitCount);

        ivVisitCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VisitDetailsActivity.this, VisitListActivity.class);
                intent.putExtra("name", userName);
                intent.putExtra("userID", userID);
                intent.putExtra("shopName", shopName);
                intent.putExtra("shopAddress", shopAddress);
                intent.putExtra("shopPhone", shopPhone);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("pointCount", pointCount);
                intent.putExtra("shopID", shopID);
                VisitDetailsActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {

        // TODO: change marker to be custom (ideas might be to use the cup_yellow_heart but resource needs work
        MapsInitializer.initialize(VisitDetailsActivity.this);
        map.addMarker(new MarkerOptions().position(shopLatLng).title(shopName));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(shopLatLng) // Center Set
                .zoom(18.0f)                // Zoom
                .bearing(0)                // Orientation of the camera to east
                .tilt(30)                   // Tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        map.getUiSettings().setMapToolbarEnabled(true);
    }

    public void locationChecker() {
        // check for permissions to use GPS location
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(VisitDetailsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.INTERNET
                }, 10);
            }
            return;     // If no permission granted return
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 10:
                locationChecker();
                break;
            default:
                break;
        }
    }

}
