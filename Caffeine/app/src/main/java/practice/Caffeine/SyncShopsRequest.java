package practice.Caffeine;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel on 20/02/2017.
 */

public class SyncShopsRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://dunedinglobal.com/caffeine/syncShops.php";

    private Map<String, String> params;

    public SyncShopsRequest(Response.Listener<String> listener, String userID, String shopID, String shopName, String address, String website, String lat, String lng, String placeID, String wifiMAC, String wifiSSID) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("userID", userID);
        params.put("shopID", shopID);
        params.put("shopName", shopName);
        params.put("address", address);
        params.put("website", website);
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("placeID", placeID);
        params.put("wifiMAC", wifiMAC);
        params.put("wifiSSID", wifiSSID);
    }

    public Map<String, String> getParams() {
        return params;
    }
}
