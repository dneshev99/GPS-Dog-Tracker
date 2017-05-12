package neshev.dimityr.gps_sms;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;



public class LoginActivity extends Activity {

    private TextView errorText;
    private EditText usernameText,passwordText;

    String [] permissions = {Manifest.permission.RECEIVE_SMS,Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        isGooglePlayServicesAvailable(this);

        if(checkIfAlreadyhavePermission()){
            Toast.makeText(this,"Already hame permissions",Toast.LENGTH_SHORT).show();
        }else {
            requestForSpecificPermission();
        }

        setTextWidgets();
        setButtons();


    }


    public boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if(status != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }

    private boolean checkIfAlreadyhavePermission() {
        int locationResult = ContextCompat.checkSelfPermission(this, Manifest.permission_group.LOCATION);
        int smsResult = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        return locationResult == PackageManager.PERMISSION_GRANTED && smsResult == PackageManager.PERMISSION_GRANTED;
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, permissions, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"Permissions Granted",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this,"Permissions Not Granted",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void setTextWidgets() {
        errorText = (TextView) findViewById(R.id.error_text);
        usernameText = (EditText) findViewById(R.id.username_text);
        passwordText = (EditText) findViewById(R.id.password_text);
    }

    private void setButtons() {
        Button loginButton = (Button) findViewById(R.id.login_button);
        Button registerButton = (Button) findViewById(R.id.register_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();

                if(username.equals("")){
                    errorText.setText("Input username.");
                }else if(password.equals("")){
                    errorText.setText("Input password.");
                }else{
                    getRequest(username,password);
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startRegister = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(startRegister);
            }
        });
    }

    private void getRequest(String username, String password) {
        String url = "http://tues-gps-app.herokuapp.com/check_login?username=" + username + "&password=" + password;

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    handleResponse(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    errorText.setText("Try Again");
                }
            });
            queue.add(request);
    }

    private void handleResponse(String response) {
       switch (response){
           case "incorrect_username":
               errorText.setText("Invalid username");
               break;
           case "incorrect_password":
               errorText.setText("Invalid password");
               break;
           case "success":
               Intent startMenu = new Intent(getApplicationContext(),MenuActivity.class);
               startActivity(startMenu);
               break;
           default:
               errorText.setText("Try again.");
       }

    }


}
