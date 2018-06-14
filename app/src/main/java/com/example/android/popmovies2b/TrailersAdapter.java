package com.example.android.popmovies2b;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;



public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.RevHolder> {
    private ArrayList<Trailers> trailers;
    private Context context;

    public TrailersAdapter(Context context, ArrayList<Trailers> trailers) {
        this.context = context;
        this.trailers = trailers;
    }

    public void addAll(ArrayList<Trailers> tlist) {
        this.trailers = tlist;
        notifyDataSetChanged();
    }

    @Override
    public RevHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailers, parent, false);
        return new RevHolder(v);
    }

    @Override
    public void onBindViewHolder(RevHolder holder, int position) {
        holder.title.setText(trailers.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public class RevHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public RevHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.trailer);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        String videoId = trailers.get(getAdapterPosition()).getKey();
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoId));
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("VIDEO_ID", videoId);
                        context.startActivity(i);
                    }
                }
            });
        }
    }
}
