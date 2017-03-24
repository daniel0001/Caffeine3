package practice.Caffeine;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel on 20/02/2017.
 */

public class ImportRemoteDB extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://dunedinglobal.com/caffeine/importDB.php";

    private Map<String, String> params;

    public ImportRemoteDB(Response.Listener<String> listener, Integer userID) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("userID", userID.toString());
    }

    public Map<String, String> getParams() {
        return params;
    }
}
