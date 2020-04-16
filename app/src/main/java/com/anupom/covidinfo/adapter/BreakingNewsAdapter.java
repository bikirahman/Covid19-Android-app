package com.anupom.covidinfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anupom.covidinfo.R;
import com.anupom.covidinfo.viewholder.BreakingNewsViewHolder;

import java.util.ArrayList;

public class BreakingNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    //--------------------------------------------------------------
    // arraylist to store the news title, desc and news image link
    private ArrayList<String> newsTitle, newsDesc, newsDate;
    //--------------------------------------------------------------

    public BreakingNewsAdapter(Context context, ArrayList<String> newsTitle, ArrayList<String> newsDesc, ArrayList<String> newsDate) {
        this.context = context;
        this.newsTitle = newsTitle;
        this.newsDesc = newsDesc;
        this.newsDate = newsDate;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View breakingNewsLayout = inflater.inflate(R.layout.breaking_new_layout, parent, false);
        viewHolder = new BreakingNewsViewHolder(breakingNewsLayout);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BreakingNewsViewHolder breakingNewsViewHolder = (BreakingNewsViewHolder) holder;

        // now set the breaking news title, description and image link
        breakingNewsViewHolder.getNewsTitle().setText(newsTitle.get(position));
        breakingNewsViewHolder.getNewsDesc().setText(newsDesc.get(position));
        breakingNewsViewHolder.getDateView().setText(newsDate.get(position));
//        Picasso.get().load(newsImageLink.get(position)).fit().into(breakingNewsViewHolder.getNewsImage());
    }

    @Override
    public int getItemCount() {
        return newsTitle.size();
    }
}
