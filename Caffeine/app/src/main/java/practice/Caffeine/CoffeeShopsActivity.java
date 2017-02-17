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
         Intent intent = getIntent();
         final String name = intent.getStringExtra("name");
         final String userID = intent.getStringExtra("userID");
         final String city = intent.getStringExtra("city");
         final String country = intent.getStringExtra("country");
         final String username = intent.getStringExtra("username");
         final String email = intent.getStringExtra("email");
         final int phone = intent.getIntExtra("phone", -1);
         final Button bAddShop = (Button) findViewById(R.id.bAddShop);


         bAddShop.setOnClickListener(new View.OnClickListener(){

             @Override
             public void onClick(View v){
                 Intent intent = new Intent(CoffeeShopsActivity.this, NewCoffeeShop.class);
                 intent.putExtra("username", username);
                 intent.putExtra("userID", userID);
                 intent.putExtra("name", name);
                 intent.putExtra("phone", phone);
                 intent.putExtra("city", city);
                 intent.putExtra("country", country);
                 intent.putExtra("email", email);
                 startActivity(new Intent(CoffeeShopsActivity.this, NewCoffeeShop.class));
             }
         });

    }






}
