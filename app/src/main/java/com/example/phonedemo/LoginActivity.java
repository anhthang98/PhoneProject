package com.example.phonedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText etUsername, etPassword;
    Button btnLogin;
    TextView tvRegister;
    String URL_LOGIN = "http://192.168.1.113/phone_demo/Login.php";

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsername = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = etUsername.getText().toString();
                String pass = etPassword.getText().toString();

//                if (user.equals("tuananh") && pass.equals("123456")){
//                    Toast.makeText(getBaseContext(), "Login Successfull", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getBaseContext(),MainActivity.class);
//                    startActivity(intent);
//
//                }
                LoginAccount();
            }
        });
    }

    private void LoginAccount() {

//        final ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setMessage("Loading...");
//        dialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if (success.equals("1")) {
                                //dialog.dismiss();
                                for (int i = 0 ; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("userid").trim();
                                    String name = object.getString("username").trim();
                                    String email = object.getString("email").trim();
                                    String address = object.getString("address").trim();

                                    sessionManager = new SessionManager(getBaseContext());
                                    sessionManager.createSession(name, email, id, address);

                                    Toast.makeText(LoginActivity.this, "Login Success"
                                            +"\nID: " + id
                                            +"\nname" + name

                                            , Toast.LENGTH_SHORT).show();

                                }
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                                finish();
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Wrong password or email", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // dialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Error system: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("usernamelogin", etUsername.getText().toString().trim());
                params.put("userpasslogin", etPassword.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
