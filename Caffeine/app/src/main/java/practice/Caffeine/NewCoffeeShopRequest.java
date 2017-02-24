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

    public NewCoffeeShopRequest(Response.Listener<String> listener, String userID, String shopName, String shopCity, String shopCountry, String wifiName) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("userID", userID);
        params.put("shopName", shopName);
        params.put("shopCity", shopCity);
        params.put("shopCountry", shopCountry);
        params.put("wifiName", wifiName);



    }
    public Map<String, String> getParams() {
        return params;
    }
}
