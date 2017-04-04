package practice.Caffeine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Daniel on 03/04/2017.
 */

public class VisitListActivity extends AppCompatActivity {

    private TextView tvShopName;
    private TextView tvShopAddress;
    private ImageView ivPointsDetail;
    private Button bBaristaReset;
    private int userID;
    private String name;
    private int pointCount;
    private String actSource;  // The activity that created this Activity

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_list);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        userID = intent.getIntExtra("userID", 0);
        pointCount = intent.getIntExtra("pointCount", 0);
        actSource = intent.getStringExtra("activitySource");

    }
}
