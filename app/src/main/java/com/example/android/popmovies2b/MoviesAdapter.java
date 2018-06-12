package com.example.android.popmovies2b;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieHolder> {
    private ArrayList<Movies> movies;
    private GridView gv;
    List<Movies> movs;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Movies movies);
    }

    public MoviesAdapter(List<Movies> movs, OnItemClickListener listener) {
        this.movs = movs;
        this.listener = listener;
    }

    public void add(List<Movies> movies) {
        movs.clear();
        this.movs.addAll(movies);
        notifyDataSetChanged();
    }

    public void clear() {
        if (this.movs != null) {
            int size = this.movs.size();
            this.movs.clear();
            this.notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies, parent, false);
        return new MovieHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        holder.bind(movs.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return movs.size();
    }

    static class MovieHolder extends RecyclerView.ViewHolder {
        ImageView posters;

        public MovieHolder(View itemView) {
            super(itemView);
            posters = (ImageView) itemView.findViewById(R.id.posterImage);
        }

        public void bind(final Movies movies, final OnItemClickListener listener) {
            Picasso.with(itemView.getContext()).
                    load("https://image.tmdb.org/t/p/w500" + movies.getMoviePoster()).into(posters);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(movies);
                }
            });
        }
    }
}
