package practice.Caffeine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import java.lang.reflect.Array;

public class NewCoffeeShop extends AppCompatActivity {

    String wifiSSID;
    Array csAddress[] = new Array[3];
    String coffeeShopName;
    String time;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_coffee_shop);
        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String userID = intent.getStringExtra("userID");
        final String city = intent.getStringExtra("city");
        final String country = intent.getStringExtra("country");
        final String username = intent.getStringExtra("username");
        final String email = intent.getStringExtra("email");
        final int phone = intent.getIntExtra("phone", -1);


        final String shopCity;

        // when addCoffeeShop clicked, add the name of the Coffee Shop, wifi SSID, Time and Address to the table corresponding to the user_id
        // then start the Coffee Shop activity

        Button addCoffeeShop = (Button) findViewById(R.id.bCreateCoffeeShop);
        intent = new Intent(NewCoffeeShop.this, CoffeeShopsActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("userID", userID);
        intent.putExtra("name", name);
        intent.putExtra("phone", phone);
        intent.putExtra("city", city);
        intent.putExtra("country", country);
        intent.putExtra("email", email);
        startActivity(new Intent(NewCoffeeShop.this, CoffeeShopsActivity.class));

        }

    public String getWifi(){

        wifiSSID = "wifiSSID";
        return wifiSSID;
    }

    public Array[] getCSAddress(){

        return csAddress;

    }
}
