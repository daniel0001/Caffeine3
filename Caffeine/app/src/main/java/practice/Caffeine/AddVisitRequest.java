package practice.Caffeine;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel on 04/04/2017.
 */

public class AddVisitRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://dunedinglobal.com/caffeine/add_visit.php";

    private Map<String, String> params;

    public AddVisitRequest(Response.Listener<String> listener, String shopID, String userID) {
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("shopID", shopID);
        params.put("userID", userID);
    }

    public Map<String, String> getParams() {
        return params;
    }
}
