package com.example.android.popmovies2b;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewHolder> {

    private ArrayList<Reviews> revList;

    public void setRev(ArrayList<Reviews> reviewsList) {
        this.revList = reviewsList;
        notifyDataSetChanged();
    }

    public ReviewsAdapter(ArrayList<Reviews> revList) {
        this.revList = revList;
    }
    @Override
    public ReviewsAdapter.ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews, parent, false);
        return new ReviewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ReviewHolder holder, int position) {
        Reviews reviews = revList.get(position);
        holder.author.setText(reviews.getAuthor());
        holder.content.setText(reviews.getcontent());
    }

    @Override
    public int getItemCount() {
        return revList.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        TextView author, content;

        public ReviewHolder(final View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.userreview);
            content = (TextView) itemView.findViewById(R.id.commentsreview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(itemView.getContext(), ReviewsInfo.class);
                    i.putExtra("author", author.getText());
                    i.putExtra("content", content.getText());
                    itemView.getContext().startActivity(i);
                }
            });
        }
    }
}
