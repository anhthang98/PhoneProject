package com.example.phonedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class UpdateProfileActivity extends AppCompatActivity {
    EditText etUsername, etPassword, etEmail, etAddress;
    Button btnUpdate;
    static SessionManager sessionManager;
    private static final String URL_UPDATE = "http://192.168.1.113/phone_demo/updateProfile.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        etUsername = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        btnUpdate = findViewById(R.id.btnUpdate);
        sessionManager = new SessionManager(getBaseContext());
        Intent intent = getIntent();
        etUsername.setText(intent.getStringExtra("userName"));
        etEmail.setText(intent.getStringExtra("email"));
        etAddress.setText(intent.getStringExtra("address"));

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> param = sessionManager.getUserDetail();
                final String userID = param.get(sessionManager.ID);
                String userName, email, address;
                userName = etUsername.getText().toString().trim();
                email = etEmail.getText().toString().trim();
                address = etAddress.getText().toString().trim();
                updateProfile(userID, userName, email, address);
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void updateProfile(final String userID, final String userName, final String email, final String address) {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i(TAG, "onResponse: " + response);
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(getBaseContext(), "Successfull!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getBaseContext(), "Invalid!" + response, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("userid", userID);
                params.put("username", userName);
                params.put("email", email);
                params.put("address", address);

                return params;
            }

        };
        requestQueue.add(stringRequest);
    }
}
