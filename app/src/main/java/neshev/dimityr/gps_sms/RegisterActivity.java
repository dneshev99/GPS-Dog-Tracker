package neshev.dimityr.gps_sms;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;


public class RegisterActivity extends Activity {

    private TextView errorText;
    private EditText usernameText,passwordText,trackeridText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setEditText();
        setButton();
    }


    private void setEditText() {
        usernameText = (EditText) findViewById(R.id.username_text);
        passwordText = (EditText) findViewById(R.id.password_text);
        trackeridText = (EditText) findViewById(R.id.tracker_id_text);
        errorText = (TextView) findViewById(R.id.error_text);
    }

    private void setButton() {
        Button registerButton = (Button) findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();
                String trackerID = trackeridText.getText().toString();
                checkInput(username,password,trackerID);
            }
        });
    }

    private void checkInput(String username, String password, String trackerID) {
        if(username.equals("")){
            errorText.setText("Input username");
        }else if (password.equals("")){
            errorText.setText("Input username");
        }else if(trackerID.equals("")){
            errorText.setText("");
        }else{
            postRequest(username,password,trackerID);
        }
    }

    private void postRequest(String username, String password, String trackerID) {
        String url = "http://tues-gps-app.herokuapp.com/create?username=" + username +
                "&password=" + password + "&fcm_token=" + FirebaseInstanceId.getInstance().getToken()
                + "&tracker_id=" + trackerID;

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
           case "username_taken" :
               errorText.setText("Username is taken.");
               break;
           case "tracker_id_taken" :
               errorText.setText("Tracker is already in use");
               break;
           case "success" :
               Intent startLogin = new Intent(getApplicationContext(),LoginActivity.class);
               startActivity(startLogin);
       }

    }


}
