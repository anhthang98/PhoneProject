package com.example.phonedemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileFragment extends Fragment {
    TextView tvBackHome;

    TextView tvUsername, tvEmail, tvAddress;
    Button btnUpdate, btnLogout;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        tvBackHome = view.findViewById(R.id.tvBackHome);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvAddress = view.findViewById(R.id.tvAddress);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnLogout = view.findViewById(R.id.btnLogout);

        tvBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UpdateProfileActivity.class);
                intent.putExtra("userName", tvUsername.getText().toString().trim());
                intent.putExtra("email", tvEmail.getText().toString().trim());
                intent.putExtra("address", tvAddress.getText().toString().trim());
                startActivity(intent);
            }
        });

        return view;

    }

}
