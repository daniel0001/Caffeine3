package practice.Caffeine;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel on 06/02/2017.
 */


public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://dunedinglobal.com/caffeine/Register.php";

    private Map<String, String> params;

    public RegisterRequest(Response.Listener<String> listener, String username, String name, String password, int phone, String city, String country, String email) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();

        params.put("username", username);
        params.put("name", name);
        params.put("password", password);
        params.put("phone", phone + "");
        params.put("city", city);
        params.put("country", country);
        params.put("email", email);
    }
    public Map<String, String> getParams() {
        return params;
    }
}