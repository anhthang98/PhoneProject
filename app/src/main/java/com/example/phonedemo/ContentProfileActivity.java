package com.example.phonedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ContentProfileActivity extends AppCompatActivity {
    TextView tvUsername, tvEmail, tvAddress;
    Button btnUpdate, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_profile);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvAddress = findViewById(R.id.tvAddress);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), UpdateProfileActivity.class);
                intent.putExtra("userName", tvUsername.getText().toString().trim());
                intent.putExtra("email", tvEmail.getText().toString().trim());
                intent.putExtra("address", tvAddress.getText().toString().trim());
                startActivity(intent);
            }
        });
    }
}
