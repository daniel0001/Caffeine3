package practice.Caffeine;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Daniel on 28/03/2017.
 */

public class SyncShopsHelper {

    private Context mContext;
    private Integer userID;


    public SyncShopsHelper(Context mContext) {

        this.mContext = mContext;
    }


    public void sync(int userID) {

        this.userID = Integer.valueOf(userID);

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            public void onResponse(String response) {
                try {
                    Log.d("response: ", response);

                    JSONArray jsonResponse = new JSONArray(response);
                    Boolean success = jsonResponse.getBoolean(0);

                    int totalShops = jsonResponse.length();
                    DatabaseHelper myDB = new DatabaseHelper(mContext);
                    myDB.getWritableDatabase();

                    if (success) {
                        for (int i = 1; i < totalShops; i++) {
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
                        Log.d("All shops inserted: ", myDB.getAllShops().toString());

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        SyncShopsRequest syncRequest = new SyncShopsRequest(responseListener, userID);
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(syncRequest);

    }

}
