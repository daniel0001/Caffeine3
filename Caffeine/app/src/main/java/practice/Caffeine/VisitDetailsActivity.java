package practice.Caffeine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

/**
 * Created by Daniel on 23/03/2017.
 */

public class VisitDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int MIN_NUMBER_POINTS = 8;
    private String userName;
    private int userID;
    private String shopName;
    private String shopPhone;
    private String shopAddress;
    private int visitCount;
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
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_details);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        // intent data passed in by the shopadapter thumbnail.onClickListener
        intent = getIntent();
        userName = intent.getStringExtra("name");
        userID = intent.getIntExtra("userID", 0);
        lat = intent.getDoubleExtra("lat", 0);
        lng = intent.getDoubleExtra("lng", 0);
        visitCount = intent.getIntExtra("visitCount", 0);
        shopLatLng = new LatLng(lat, lng);

        tvShopName = (TextView) findViewById(R.id.tvShopName);
        tvShopAddress = (TextView) findViewById(R.id.tvShopAddress);
        tvShopPhone = (TextView) findViewById(R.id.tvShopPhone);
        ivVisitCount = (ImageView) findViewById(R.id.ivPointsWide);

        shopName = intent.getStringExtra("shopName");
        shopAddress = intent.getStringExtra("shopAddress");
        shopPhone = intent.getStringExtra("shopPhone");
        tvShopName.setText(shopName);
        tvShopPhone.setText(shopPhone);
        tvShopAddress.setText(shopAddress);

        // Button Add Point - check if in shop using GPS, check if already at 9 points,
        // check if point already collected in same calendar day (only 1 point per day allowed)

        bAddPoint = (Button) findViewById(R.id.bAddPoint);


        bClaimCoffee = (Button) findViewById(R.id.bRedeemPoints);
        if (visitCount <= MIN_NUMBER_POINTS) {
            bClaimCoffee.setTextColor(R.color.light_grey);
            bClaimCoffee.setBackgroundColor(R.color.dark_grey);
        }
        if (visitCount == MIN_NUMBER_POINTS + 1) {
            bClaimCoffee.setTextColor(R.color.white);
            bClaimCoffee.setBackgroundColor(R.color.red);
        }


        bClaimCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (visitCount == MIN_NUMBER_POINTS + 1) {
                    Intent intent = new Intent(VisitDetailsActivity.this, RedeemCoffeeActivity.class);
                    intent.putExtra("name", userName);
                    intent.putExtra("userID", userID);
                    intent.putExtra("shopName", shopName);
                    intent.putExtra("shopAddress", shopAddress);
                    intent.putExtra("shopPhone", shopPhone);
                    intent.putExtra("lat", lat);
                    intent.putExtra("lng", lng);
                    intent.putExtra("visitCount", visitCount);
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

        // Create array of shopImages and change image depending on number of visits from visitCount
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
        Glide.with(this).load(shopImages[visitCount]).into(ivVisitCount);

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
                intent.putExtra("visitCount", visitCount);
                VisitDetailsActivity.this.startActivity(intent);
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap map) {

        // TODO: change marker to be custom (ideas might be to use the cup_yellow_heart but resource needs work
        MapsInitializer.initialize(this);
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


}
