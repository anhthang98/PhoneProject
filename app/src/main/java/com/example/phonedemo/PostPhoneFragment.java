package com.example.phonedemo;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class PostPhoneFragment extends Fragment {

    Button btnChooseImage, btnPost;
    ImageView imgImageChoose;
    EditText edtTitle, edtPrice, edtDescription, edtCate;
    int SELECT_PICTURE = 1;
    Bitmap bitmap;
    SessionManager sessionManager;

    String URL_POST = "http://192.168.59.107/phone_demo/uploadImage.php";

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.postphone_fragment, container, false);

        // Anh xa
        btnChooseImage = view.findViewById(R.id.choseImage);
        imgImageChoose = view.findViewById(R.id.imageChoose);
        edtTitle = view.findViewById(R.id.edtTitle);
        edtPrice = view.findViewById(R.id.edtPrice);
        edtDescription = view.findViewById(R.id.edtDescription);
        btnPost = view.findViewById(R.id.btnPost);
        edtCate = view.findViewById(R.id.edtCate);

        sessionManager = new SessionManager(getContext());
        HashMap<String, String> param = sessionManager.getUserDetail();
        final String userID = param.get(sessionManager.ID);

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadPicture(userID, getStringImage(bitmap));
            }
        });

        return view;
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                imgImageChoose.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void UploadPicture(final String id, final String photo) {
        final String name = edtTitle.getText().toString().trim();
        final String price = edtPrice.getText().toString().trim();
        final String description = edtDescription.getText().toString().trim();
        final String category = edtCate.getText().toString().trim();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Post", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");


                            if (success.equals("1")) {
                                Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Post", "Try again! " + e);
                            Toast.makeText(getContext(), "Try Again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Post", "Error system! " + error);
                        Toast.makeText(getContext(), "Error system!", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", id);
                param.put("name", name);
                param.put("price", price);
                param.put("description", description);
                param.put("category", category);
                param.put("image", photo);
                return param;
            }
        };

        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
