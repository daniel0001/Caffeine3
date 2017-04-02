package practice.Caffeine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Daniel on 23/03/2017.
 */

public class VisitDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String shopName;
    private String shopPhone;
    private String shopAddress;
    private TextView tvShopName;
    private TextView tvShopPhone;
    private TextView tvShopAddress;
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
        lat = intent.getDoubleExtra("lat", 0);
        lng = intent.getDoubleExtra("lng", 0);
        shopLatLng = new LatLng(lat, lng);

        tvShopName = (TextView) findViewById(R.id.tvShopName);
        tvShopAddress = (TextView) findViewById(R.id.tvShopAddress);
        tvShopPhone = (TextView) findViewById(R.id.tvShopPhone);

        shopName = intent.getStringExtra("shopName");
        shopAddress = intent.getStringExtra("shopAddress");
        shopPhone = intent.getStringExtra("shopPhone");
        tvShopName.setText(shopName);
        tvShopPhone.setText(shopPhone);
        tvShopAddress.setText(shopAddress);


    }

    @Override
    public void onMapReady(GoogleMap map) {

        map.addMarker(new MarkerOptions().position(shopLatLng).title(shopName));
        map.getUiSettings().setMapToolbarEnabled(true);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(shopLatLng) // Center Set
                .zoom(18.0f)                // Zoom
                .bearing(0)                // Orientation of the camera to east
                .tilt(30)                   // Tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        // map.moveCamera(CameraUpdateFactory.newLatLngZoom(shopLatLng, 18));

    }


}
