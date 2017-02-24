package practice.Caffeine;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class CoffeeShopsActivity extends AppCompatActivity {
     @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_coffee_shops);



         // Variables passed to this activity from Login
         Intent intent = getIntent();
         final String name = intent.getStringExtra("name");
         final String userID = intent.getStringExtra("userID");
         final Button bAddShop = (Button) findViewById(R.id.bAddShop);


         // Button to move to new coffee shop activity with variables
         bAddShop.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(CoffeeShopsActivity.this, NewCoffeeShopActivity.class);
                 intent.putExtra("userID", userID);
                 intent.putExtra("name", name);
                 CoffeeShopsActivity.this.startActivity(intent);
             }
         });

     }





}
