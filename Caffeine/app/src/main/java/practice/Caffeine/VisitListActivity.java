package practice.Caffeine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_list);
        Context mContext = VisitListActivity.this;

        Intent intent = getIntent();
        shopID = intent.getIntExtra("shopID", 0);
        name = intent.getStringExtra("name");
        userID = intent.getIntExtra("userID", 0);
        pointCount = intent.getIntExtra("pointCount", 0);
        displayVisitList = new ArrayList<>();

        // check if pointCount is 0 to optimise
        if (pointCount == 0) {
            Visit emptyVisit = new Visit();
            emptyVisit.setID(-1);
            emptyVisit.setShopID(shopID);
            emptyVisit.setVisitID(-1);
            emptyVisit.setDate(getResources().getString(R.string.no_visits));
            displayVisitList.add(0, emptyVisit);
        } else {
            // Create a list of visits given the shopID for an individual shop that started this activity
            DatabaseHelper myDB = new DatabaseHelper(mContext);
            myDB.getReadableDatabase();
            displayVisitList = myDB.getAllVisits();
            myDB.close();

            if (!displayVisitList.isEmpty()) {
                for (Iterator<Visit> itr = displayVisitList.iterator(); itr.hasNext(); ) {
                    Visit itrVisit = itr.next();
                    if (itrVisit.getShopID() != shopID) {
                        itr.remove();
                    }
                }
                int listSize = displayVisitList.size() - 1;
                displayVisitList = displayVisitList.subList(listSize - pointCount, listSize);  // cut the full list down to just those visits to be displayed
            }
        }
        Log.d("List: ", displayVisitList.toString());
        String[] date = new String[displayVisitList.size()];
        // Build the listView
        for (int i = 0; i < displayVisitList.size(); i++) {
            Visit lvVisit = displayVisitList.get(i);
            date[i] = lvVisit.getDate();
        }
        lv = (ListView) findViewById(R.id.visitListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.visit_list_item, R.id.list_item, date);
        lv.setAdapter(adapter);
    }
}
