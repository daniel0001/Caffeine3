package practice.Caffeine;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel on 20/02/2017.
 */

public class NewCoffeeShopRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://dunedinglobal.com/caffeine/new_coffee_shop.php";

    private Map<String, String> params;

    public NewCoffeeShopRequest(Response.Listener<String> listener, String shopName, String address, String wifiSSID, String wifiMAC, String lat, String lng, String shopWeb, String phone, Integer userID, String placeID) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("shopName", shopName);
        params.put("address", address);
        params.put("wifiSSID", wifiSSID);
        params.put("wifiMAC", wifiMAC);
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("shopWeb", shopWeb);
        params.put("phoneNum", phone);
        params.put("userID", userID.toString());
        params.put("placeID", placeID);

    }
    public Map<String, String> getParams() {
        return params;
    }
}
