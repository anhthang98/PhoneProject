package com.example.phonedemo;

import android.view.View;

public interface ItemClickListener {
    void onClick(View view, int position, boolean isLongClick);
}
