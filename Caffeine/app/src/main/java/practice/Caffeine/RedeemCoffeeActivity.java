package practice.Caffeine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_coffee);

        Intent intent = getIntent();
        tvShopName = (TextView) findViewById(R.id.tvShopName);
        tvShopAddress = (TextView) findViewById(R.id.tvShopAddress);
        userID = intent.getIntExtra("userID", 0);
        name = intent.getStringExtra("name");
        pointCount = intent.getIntExtra("pointCount", 0);
        lat = intent.getDoubleExtra("lat", 0);
        lng = intent.getDoubleExtra("lng", 0);
        shopName = intent.getStringExtra("shopName");
        shopAddress = intent.getStringExtra("shopAddress");

        tvShopName.setText(shopName);
        tvShopAddress.setText(shopAddress);

        ivPointsDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RedeemCoffeeActivity.this, VisitListActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("userID", userID);
                intent.putExtra("pointCount", pointCount);
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
                                proceed[0] = true;
                                return;
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                proceed[0] = false;
                                return;
                            }
                        })
                        .create()
                        .show();

                if (proceed[0]) {
                    Intent intent = new Intent(RedeemCoffeeActivity.this, VisitDetailsActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("userID", userID);
                    intent.putExtra("pointCount", 0); // Set pointCount to 0 now that the coffee has been redeemed
                    intent.putExtra("lat", lat);
                    intent.putExtra("lng", lng);
                    RedeemCoffeeActivity.this.startActivity(intent);
                }

            }
        });

    }
}
