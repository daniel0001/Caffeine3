package mycompany.freecoffeev1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etName = (EditText) findViewById(R.id.etName);
        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etPhone = (EditText) findViewById(R.id.etPhone);
        final EditText etTownCity = (EditText) findViewById(R.id.etTownCity);
        final EditText etCountry = (EditText) findViewById(R.id.etCountry);
        final EditText etEmailAddress = (EditText) findViewById(R.id.etEmailAddress);
        final Button bRegister = (Button) findViewById(R.id.bRegister);

    }
}
