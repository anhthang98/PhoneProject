package com.example.phonedemo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class Post_Fragment extends Fragment {
    private static final String TAG = "Post_Fragment";
    private static final String URL = "http://192.168.1.113/phone_demo/getPhone.php";
    private static final String URL_ADD_LIKE = "http://192.168.1.113/phone_demo/addLike.php";
    private static final String URL_DELETE_LIKEPOST = "http://192.168.1.113/phone_demo/deleteLike.php";
    private static final String URL_GET_LIKEPOST = "http://192.168.1.113/phone_demo/getLikePost.php";

    private RecyclerView recyclerView;
    private List<Movie> movieList;
    private List<Category> categoryList;
    private List<LikePost> likePostList;
    private static Post_Fragment.StoreAdapter mAdapter;
    ProgressBar progressBar;
    static SessionManager sessionManager;

    EditText edtSearch;
    ImageView imgSearch;

    public static Post_Fragment newInstance(String categoryID) {

        Bundle args = new Bundle();
        args.putString("categoryID", categoryID);
        Post_Fragment fragment = new Post_Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.post_info, container, false);
        imgSearch = view.findViewById(R.id.imgSearch);
        edtSearch = view.findViewById(R.id.edtSearch);
        String categoryID = getArguments().getString("categoryID");
        recyclerView = view.findViewById(R.id.recycler_view);
        movieList = new ArrayList<>();
        likePostList = new ArrayList<>();
        mAdapter = new Post_Fragment.StoreAdapter(getActivity(), movieList, likePostList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false);

        // recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        fetchStoreItems(categoryID);
        getLikePostList();
        // progressBar.setVisibility(View.VISIBLE);

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = edtSearch.getText().toString();
                ArrayList<Movie> newListPhone = new ArrayList<>();
                newListPhone.addAll(movieList);
                ArrayList<Movie> newList = new ArrayList<>();
                for (Movie post : newListPhone) {
                    if (post.getName().toLowerCase().contains(search)) {
                        newList.add(post);
                    }
                }
                newListPhone.clear();
                newListPhone.addAll(newList);
                mAdapter.notifyDataSetChanged();
                mAdapter = new Post_Fragment.StoreAdapter(getActivity(), newListPhone, likePostList);
                recyclerView.setAdapter(mAdapter);
            }
        });

        return view;

    }

    private void fetchStoreItems(final String categoryID) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i(TAG, response.toString());
                        if (response == null) {
                            Toast.makeText(getContext(), "Couldn't fetch the store items! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        movieList.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                movieList.add(new Movie(object.getString("phone_id"), object.getString("phone_name"),
                                        object.getString("phone_price"), object.getString("phone_description"),
                                        object.getString("phone_image")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("categoryid", categoryID);

                return params;
            }

        };
        requestQueue.add(jsonArrayRequest);
    }

    private void getLikePostList() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, URL_GET_LIKEPOST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i(TAG, response.toString());
                        if (response == null) {
                            Toast.makeText(getContext(), "Couldn't fetch the store items! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        likePostList.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                likePostList.add(new LikePost(object.getString("userID"),
                                        object.getString("phone_id"), object.getString("checkLike")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    static class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyViewHolder> {
        private Context context;
        private List<Movie> movieList;
        private OnItemClickListener listener;
        private List<LikePost> likePostList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name, description, price, phoneName, tvLike;
            public ImageView thumbnail, imgLike;
            ProgressBar progressBar;
            LinearLayout lnLike;

            public MyViewHolder(final View view) {
                super(view);
                phoneName = view.findViewById(R.id.name);
                name = view.findViewById(R.id.phoneName);
                price = view.findViewById(R.id.price);
                description = view.findViewById(R.id.description);
                thumbnail = view.findViewById(R.id.thumbnail);

                imgLike = view.findViewById(R.id.imgLike);
                tvLike = view.findViewById(R.id.tvLike);
                lnLike = view.findViewById(R.id.lnLike);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onItemClick(view, getAdapterPosition());
                        }
                    }
                });
            }
        }


        public StoreAdapter(Context context, List<Movie> movieList, List<LikePost> likePostList) {
            this.context = context;
            this.movieList = movieList;
            this.likePostList = likePostList;
            sessionManager = new SessionManager(context);

        }

        public interface OnItemClickListener {
            void onItemClick(View itemView, int position);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final Movie movie = movieList.get(position);
            holder.name.setText(movie.getName());
            holder.price.setText(movie.getPrice());
            holder.description.setText(movie.getDescription());
            Glide.with(context)
                    .load(movie.getImage())
                    .into(holder.thumbnail);


            final String[] checkLike = {""};

            if (likePostList.size() >= 0) {
                checkLike[0] = "T";
            } else if (likePostList.size() < 0) {
                checkLike[0] = "F";
            }
            HashMap<String, String> param = sessionManager.getUserDetail();
            final String userID = param.get(sessionManager.ID);

            for (int i = position; i < likePostList.size(); i++) {
                if (likePostList.get(i).getCheckLike().equals("T")) {
                    holder.imgLike.setImageResource(R.drawable.ic_likered);
                    int resource = holder.tvLike.getResources().getColor(R.color.text_like);
                    holder.tvLike.setTextColor(resource);
                    break;
                }
            }


            holder.lnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, String> param = sessionManager.getUserDetail();
                    final String userID = param.get(sessionManager.ID);
                    if (checkLike[0].equals("T")) {
                        holder.imgLike.setImageResource(R.drawable.ic_favorite);
                        int resource = holder.tvLike.getResources().getColor(R.color.text_unlike);
                        holder.tvLike.setTextColor(resource);
                        deleteLikePost(userID, movie.getId());
                        checkLike[0] = "F";
                    } else if (checkLike[0].equals("F")) {
                        holder.imgLike.setImageResource(R.drawable.ic_likered);
                        int resource = holder.tvLike.getResources().getColor(R.color.text_like);
                        holder.tvLike.setTextColor(resource);
                        addLike(userID, movie.getId(), "T");
                        checkLike[0] = "T";
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return movieList.size();
        }


        public void addLike(final String userID, final String postID, final String checkLike) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_LIKE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.i(TAG, "onResponse: " + response);
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");

                                if (success.equals("1")) {
                                    Toast.makeText(context, "Successfull!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Invalid!" + response, Toast.LENGTH_SHORT).show();
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

                    params.put("userID", userID);
                    params.put("postID", postID);
                    params.put("checkLike", checkLike);

                    return params;
                }

            };
            requestQueue.add(stringRequest);
        }

        public void deleteLikePost(final String userID, final String postID) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE_LIKEPOST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.i(TAG, "onResponse: " + response);
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");

                                if (success.equals("1")) {
                                    Toast.makeText(context, "Successfull!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Invalid!" + response, Toast.LENGTH_SHORT).show();
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

                    params.put("userID", userID);
                    params.put("postID", postID);

                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }
}
