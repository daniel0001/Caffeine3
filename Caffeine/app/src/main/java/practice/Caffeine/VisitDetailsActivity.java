package practice.Caffeine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Daniel on 23/03/2017.
 */

public class VisitDetailsActivity extends AppCompatActivity {


    private TextView tvShopName = (TextView) findViewById(R.id.tvShopName);
    private TextView tvShopPhone = (TextView) findViewById(R.id.tvShopPhone);
    private TextView tvShopAddress = (TextView) findViewById(R.id.tvShopAddress);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_details);

        // intent data passed in by the shopadapter thumbnail.onClickListener
        Intent intent = getIntent();
        tvShopName.setText(intent.getStringExtra("shopName"));
        tvShopPhone.setText(intent.getStringExtra("shopPhone"));
        tvShopAddress.setText(intent.getStringExtra("shopAddress"));


    }



}
