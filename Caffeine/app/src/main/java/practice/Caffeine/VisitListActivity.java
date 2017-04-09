package practice.Caffeine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Daniel on 03/04/2017.
 */

public class VisitListActivity extends AppCompatActivity {


    private int shopID;
    private int userID;
    private String name;
    private int pointCount;
    private List<Visit> displayVisitList;
    private ListView lv;
    private String shopName;
    private ArrayList<String> date;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_list);
        Context mContext = VisitListActivity.this;

        Intent intent = getIntent();
        shopID = intent.getIntExtra("shopID", 0);
        name = intent.getStringExtra("name");
        shopName = intent.getStringExtra("shopName");
        userID = intent.getIntExtra("userID", 0);
        pointCount = intent.getIntExtra("pointCount", 0);
        displayVisitList = new ArrayList<>();
        TextView tvShopName = (TextView) findViewById(R.id.tvShopName);
        tvShopName.setText(shopName);
        date = new ArrayList<String>();


        // check if pointCount is 0 to optimise
        if (pointCount == 0) {
            date.add(0, getResources().getString(R.string.no_visits));
        } else {
            // Create a list of visits given the shopID for an individual shop that started this activity
            DatabaseHelper myDB = new DatabaseHelper(mContext);
            myDB.getReadableDatabase();
            displayVisitList = myDB.getAllVisits();
            myDB.close();
            for (Iterator<Visit> itr = displayVisitList.iterator(); itr.hasNext(); ) {
                Visit itrVisit = itr.next();
                if (itrVisit.getShopID() != shopID) {
                    itr.remove();
                    }
                }
            for (int i = 0; i < displayVisitList.size(); i++) {
                //TODO: Timezones below need to be fixed - 09/04/2017
                Visit lvVisit = displayVisitList.get(i);
                String lvDate = lvVisit.getDate();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                TimeZone utcZone = TimeZone.getTimeZone("CST");
                simpleDateFormat.setTimeZone(utcZone);
                Date myDate = null;
                try {
                    myDate = simpleDateFormat.parse(lvDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                String mDate = simpleDateFormat.format(myDate);

                date.add(i, mDate);
            }
        }
        Log.d("List: ", displayVisitList.toString());

        // Build the listView

        lv = (ListView) findViewById(R.id.visitListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.visit_list_item, R.id.list_item, date);
        lv.setAdapter(adapter);
    }
}
