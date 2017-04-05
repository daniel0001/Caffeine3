package practice.Caffeine;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CoffeeShopsActivity extends AppCompatActivity {

    private static final int MAX_VISITS_FOR_FREE_COFFEE = 9; // The Max + 1 Visit is when the user redeems the free coffee
    private RecyclerView recyclerView;
    private ShopAdapter adapter;
    private ArrayList<ShopCard> shopList;
    private String name;
    private Integer userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_shops);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Clean DB for rebuild
        DatabaseHelper myDB = new DatabaseHelper(this);
        myDB.getWritableDatabase();
        myDB.deleteAllShops();
        myDB.deleteAllVisits();


        initCollapsingToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        shopList = new ArrayList<>();
        adapter = new ShopAdapter(this, shopList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        // Variables passed to this activity from Login
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        userID = intent.getIntExtra("userID", 0);

        Response.Listener<String> syncResponseListener = new Response.Listener<String>() {

            public void onResponse(String response) {
                try {
                    Log.d("response: ", response);

                    JSONArray jsonResponse = new JSONArray(response);
                    Boolean success = jsonResponse.getBoolean(0);

                    int responseSize = jsonResponse.length();

                    DatabaseHelper myDB = new DatabaseHelper(CoffeeShopsActivity.this);
                    myDB.getWritableDatabase();

                    if (success) {
                        for (int i = 1; i < responseSize; i++) {
                            if (jsonResponse.getJSONObject(i).getString("type").equals("shop")) {
                                Shop shop = new Shop();
                                JSONObject jsonShop = jsonResponse.getJSONObject(i);
                                shop.setShopID(jsonShop.getInt("shopID"));
                                shop.setName(jsonShop.getString("name"));
                                shop.setWifiMAC(jsonShop.getString("wifiMAC"));
                                shop.setWifiSSID(jsonShop.getString("wifiSSID"));
                                shop.setAddress(jsonShop.getString("address"));
                                shop.setLat(jsonShop.getString("lat"));
                                shop.setLng(jsonShop.getString("lng"));
                                shop.setWebsite(jsonShop.getString("website"));
                                shop.setPhoneNum(jsonShop.getString("phoneNum"));
                                shop.setPlaceID(jsonShop.getString("placeID"));
                                myDB.addShop(shop);
                            }
                            if (jsonResponse.getJSONObject(i).getString("type").equals("visit")) {
                                Visit visit = new Visit();
                                JSONObject jsonVisit = jsonResponse.getJSONObject(i);
                                visit.setVisitID(jsonVisit.getInt("visitID"));
                                visit.setShopID(jsonVisit.getInt("shopID"));
                                visit.setDate(jsonVisit.getString("date"));
                                myDB.addVisit(visit);
                            }
                        }
                        Log.d("All Shops inserted: ", myDB.getAllShops().toString());
                        Log.d("All Visits inserted: ", myDB.getAllVisits().toString());

                    }
                    myDB.close();
                    prepareShops();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        SyncShopsRequest syncRequest = new SyncShopsRequest(syncResponseListener, userID);
        RequestQueue queue = Volley.newRequestQueue(CoffeeShopsActivity.this);
        queue.add(syncRequest);

        // Load shops


        try {
            Glide.with(this).load(R.drawable.love_coffee).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Variable passed to this activity from NewCoffeeShopActivity
        // check if this the first ever addition of this shop
        // and reward user with congrats message
        final Boolean firstTimeShopAdded = intent.getBooleanExtra("firstEverShopAdded", false);

        if (firstTimeShopAdded) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CoffeeShopsActivity.this);
            builder.setMessage("***NEW SHOP DISCOVERED!*** \n \nCongratulations! It looks like you are the first person to register this shop for Love Coffee. \n\nThat's great - but we will have to check if they accept 'Love Coffee' loyalty points so please check with a member of staff now. \n\nIf more info is required, please visit www.lovecoffee.ie for merchant details.")
                    .setNegativeButton("Ok", null)
                    .create()
                    .show();
        }
    }


    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Adding ShopCards
     */
    private void prepareShops() {


        int[] shopImages = new int[]{
                R.drawable.addshop,
                R.drawable.cup_grey_grid_0,
                R.drawable.cup_grey_grid_heart_1,
                R.drawable.cup_grey_grid_heart_2,
                R.drawable.cup_grey_grid_heart_3,
                R.drawable.cup_grey_grid_heart_4,
                R.drawable.cup_grey_grid_heart_5,
                R.drawable.cup_grey_grid_heart_6,
                R.drawable.cup_grey_grid_heart_7,
                R.drawable.cup_grey_grid_heart_8,
                R.drawable.cup_grey_grid_heart_9,
        };

        ShopCard a = new ShopCard(getString(R.string.add_new_coffee_shop), 0, shopImages[0], null, null, null, null, 0);
        shopList.add(a);

        // check if connected to internet
        IsWifiConnectedHelper con = new IsWifiConnectedHelper(this);
        Boolean connected = con.getConnected();
        if (!connected) {
            con.notConnectedMessage();
            // Set up new intent LoginActivity and send user back to Login again
            Intent intent = new Intent(CoffeeShopsActivity.this, LoginActivity.class);
            CoffeeShopsActivity.this.startActivity(intent);
        } else {
            DatabaseHelper myDB = new DatabaseHelper(this);
            myDB.getReadableDatabase();
            // Read from Local SQLite db to populate shop list
            if (0 < myDB.getAllShops().size()) {
                for (int i = 1; i <= myDB.getAllShops().size(); i++) {
                    ShopCard shopCard = new ShopCard();
                    Shop shop;
                    shop = myDB.getShop(i);
                    shopCard.setName(shop.getName());
                    shopCard.setShopPhone(shop.getPhoneNum());
                    shopCard.setShopAddress(shop.getAddress());
                    shopCard.setNumPoints(pointCount(shop.getShopID()));
                    shopCard.setShopImage(shopImages[pointCount(shop.getShopID()) + 1]);
                    shopCard.setLat(Double.valueOf(shop.getLat()));
                    shopCard.setLng(Double.valueOf(shop.getLng()));
                    shopCard.setShopID(shop.getShopID());
                    shopList.add(shopCard);
                }
                myDB.close();
                adapter.notifyDataSetChanged();
                }

        }
    }

    // pointCount() calculates the number of points collected by the user/
    // The Max + 1 Visit is when the user redeems the free coffee so that is why
    // pointCount = visitCount % MAX_VISITS_FOR_FREE_COFFEE + 1 or (visitCount % 10 points) if Max = 9
    // The user will be locked into collecting the free coffee before registering further points to avoid
    // overflow of points - alternative solution would be to implement a 'Redeemed Points' table and log
    // how many free 'credits' the user has collected. As this is extra work we are just going for MVP.
    private int pointCount(int shopID) {
        DatabaseHelper myDB = new DatabaseHelper(this);
        myDB.getReadableDatabase();
        int visitCount = 0;
        for (int i = 1; i < myDB.getAllVisits().size(); i++) {
            Visit visit = myDB.getVisit(i);
            if (visit.getShopID() == shopID) visitCount++;
        }
        int pointCount = visitCount % MAX_VISITS_FOR_FREE_COFFEE + 1;
        myDB.close();
        return pointCount;
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

}

