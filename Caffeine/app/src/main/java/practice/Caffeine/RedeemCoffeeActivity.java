package practice.Caffeine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class RedeemCoffeeActivity extends AppCompatActivity {

    private TextView tvShopName;
    private TextView tvShopAddress;
    private ImageView ivPointsDetail;
    private Button bBaristaReset;
    private int userID;
    private String name;
    private int pointCount;
    private Double lat;
    private Double lng;
    private String shopName;
    private String shopAddress;
    private int shopID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_coffee);

        Intent intent = getIntent();
        tvShopName = (TextView) findViewById(R.id.tvShopName);
        tvShopAddress = (TextView) findViewById(R.id.tvShopAddress);
        ivPointsDetail = (ImageView) findViewById(R.id.ivNineCupsWide);
        bBaristaReset = (Button) findViewById(R.id.bBaristaButton);
        userID = intent.getIntExtra("userID", 0);
        name = intent.getStringExtra("name");
        pointCount = intent.getIntExtra("pointCount", 0);
        lat = intent.getDoubleExtra("lat", 0);
        lng = intent.getDoubleExtra("lng", 0);
        shopName = intent.getStringExtra("shopName");
        shopAddress = intent.getStringExtra("shopAddress");
        shopID = intent.getIntExtra("shopID", 0);

        tvShopName.setText(shopName);
        tvShopAddress.setText(shopAddress);

        ivPointsDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RedeemCoffeeActivity.this, VisitListActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("userID", userID);
                intent.putExtra("pointCount", pointCount);
                intent.putExtra("shopID", shopID);
                intent.putExtra("activitySource", "redeemActivity");
                RedeemCoffeeActivity.this.startActivity(intent);
            }
        });

        bBaristaReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Boolean[] proceed = {false};

                final AlertDialog.Builder builder = new AlertDialog.Builder(RedeemCoffeeActivity.this);
                builder.setMessage(R.string.barista_button_warning)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Response.Listener<String> listener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            Log.d("fixme", response);
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if (success) {
                                                Intent intent = new Intent(RedeemCoffeeActivity.this, CoffeeShopsActivity.class);
                                                intent.putExtra("name", name);
                                                intent.putExtra("userID", userID);
                                                RedeemCoffeeActivity.this.startActivity(intent);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                };
                                AddVisitRequest addVisitRequest = new AddVisitRequest(listener, String.valueOf(shopID), String.valueOf(userID));
                                RequestQueue queue = Volley.newRequestQueue(RedeemCoffeeActivity.this);
                                queue.add(addVisitRequest);

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                return;
                            }
                        })
                        .create()
                        .show();
            }
        });
    }
}

