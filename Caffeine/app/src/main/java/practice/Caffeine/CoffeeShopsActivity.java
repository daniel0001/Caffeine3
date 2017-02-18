package practice.Caffeine;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;


public class CoffeeShopsActivity extends AppCompatActivity {
     @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_coffee_shops);

        // Instantiate new local SQL DB = UserDetails
         UserDetailsContract.UserDetailsDBHelper mDbHelper = new UserDetailsContract.UserDetailsDBHelper(this);
         // Gets the data repository in write mode
         SQLiteDatabase db = mDbHelper.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
         String[] projection = {
                 UserDetailsContract.UserDetails._ID,
                 UserDetailsContract.UserDetails.COLUMN_NAME_USERNAME,
         };

// Filter results WHERE "title" = 'My Title'
         String selection = UserDetailsContract.UserDetails.COLUMN_NAME_USERNAME + " = ?";
         String[] selectionArgs = {"username"};

// How you want the results sorted in the resulting Cursor
         String sortOrder =
                 UserDetailsContract.UserDetails.COLUMN_NAME_USERNAME + " DESC";

         Cursor cursor = db.query(
                 UserDetailsContract.UserDetails.TABLE_NAME,                     // The table to query
                 projection,                               // The columns to return
                 selection,                                // The columns for the WHERE clause
                 selectionArgs,                            // The values for the WHERE clause
                 null,                                     // don't group the rows
                 null,                                     // don't filter by row groups
                 sortOrder                                 // The sort order
         );
         List <String>itemIds = new ArrayList<>();
         while (cursor.moveToNext()) {
             String itemId = cursor.getString(
                     cursor.getColumnIndexOrThrow(UserDetailsContract.UserDetails._ID));
             itemIds.add(itemId);
         }
         cursor.close();

         // Display the SQL data in the edit text field test
         final EditText etUsername = (EditText) findViewById(R.id.etDBTest);

         etUsername.setText(itemIds.get(0));


         // Variables passed to this activity from Login
         Intent intent = getIntent();
         final String name = intent.getStringExtra("name");
         final String userID = intent.getStringExtra("userID");
         final String city = intent.getStringExtra("city");
         final String country = intent.getStringExtra("country");
         final String username = intent.getStringExtra("username");
         final String email = intent.getStringExtra("email");
         final int phone = intent.getIntExtra("phone", -1);
         final Button bAddShop = (Button) findViewById(R.id.bAddShop);


         // Button to move to new coffee shop activity with variables
         bAddShop.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
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
