package practice.Caffeine;

import android.content.Context;

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
    private DatabaseHelper myDB;
    private String userID;
    private String shopID;
    private String shopName;
    private String address;
    private String website;
    private String lat;
    private String lng;
    private String placeID;
    private String wifiMAC;
    private String wifiSSID;

    public void SyncShopsHelper(Context mContext, int userID) {
        this.mContext = mContext;
        this.userID = String.valueOf(userID);


    }

    private Boolean syncShopsTable() {

        myDB = new DatabaseHelper(mContext);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray();
                    boolean success = jsonArray.getJSONObject(0).getBoolean("success");
                    int totalShops = jsonArray.length();
                    myDB.getWritableDatabase();

                    if (success) {
                        for (int i = 1; i < totalShops; i++) {
                            Shop shop = new Shop();
                            JSONObject jsonShop = jsonArray.getJSONObject(i);
                            shop.setShopID(jsonShop.getInt("shopID"));
                            shop.setName(jsonShop.getString("name"));
                            shop.setAddress(jsonShop.getString("address"));
                            shop.setWebsite(jsonShop.getString("website"));
                            shop.setLat(jsonShop.getString("lat"));
                            shop.setLng(jsonShop.getString("lng"));
                            shop.setPlaceID(jsonShop.getString("placeID"));
                            shop.setWifiMAC(jsonShop.getString("wifiMac"));
                            shop.setWifiSSID(jsonShop.getString("wifiSSID"));
                            myDB.addShop(shop);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        SyncShopsRequest syncRequest = new SyncShopsRequest(responseListener, userID, shopID, shopName, address, website, lat, lng, placeID, wifiMAC, wifiSSID);
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(syncRequest);

        return (myDB.getAllShops().size() > 0);#

        //TODO: write syncShops.php to work with the above and return the shops as a JSONARRAY that correspond to userID

    }


}
