package com.example.android.popmovies2b;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class ReviewsInfo extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviews_info);
        TextView author = (TextView) findViewById(R.id.commentAuthor);
        TextView comment = (TextView) findViewById(R.id.comment);
        author.setText(getIntent().getStringExtra("author"));
        comment.setText(getIntent().getStringExtra("comment"));
    }
}
