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

    public SyncShopsRequest(Response.Listener<String> listener, Integer userID, Integer maxVisits) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("userID", userID.toString());
        params.put("maxVisits", maxVisits.toString());
    }

    public Map<String, String> getParams() {
        return params;
    }
}
