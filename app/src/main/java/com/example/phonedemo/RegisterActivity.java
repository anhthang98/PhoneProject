package com.example.phonedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class RegisterActivity extends AppCompatActivity {

    EditText etUsername, etPassword, etEmail, etAddress;
    Button btnRegister;
    private static final String URL_REGISTER = "http://192.168.59.107/phone_demo/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etUsername = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = "";
                String userName = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String email = etEmail.getText().toString();
                String address = etAddress.getText().toString();

                if (validateLogin() || email.isEmpty() || address.isEmpty()){
                    addFavorite(userID, userName, password, email, address);
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    private boolean validateLogin() {
        if (TextUtils.isEmpty(etUsername.getText().toString())) {
            etUsername.setError("Not blank");
            etUsername.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            etPassword.setError("Not blank");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    public void addFavorite(final String userID, final String userName, final String password, final String email, final String address) {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
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
                params.put("password", password);
                params.put("email", email);
                params.put("address", address);

                return params;
            }

        };
        requestQueue.add(stringRequest);
    }
}
