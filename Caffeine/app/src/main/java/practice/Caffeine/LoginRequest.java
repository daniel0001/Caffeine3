package practice.Caffeine;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel on 06/02/2017.
 */

public class LoginRequest extends StringRequest {

    private static final String LOGIN_REQUEST_URL = "http://dunedinglobal.com/caffeine/login.php";
    private Map<String, String> params;

    public LoginRequest(Response.Listener<String> listener, String username, String password) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
    }

    public Map<String, String> getParams() {
        return params;
    }

}
