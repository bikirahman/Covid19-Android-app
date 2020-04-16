package com.anupom.covidinfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anupom.covidinfo.R;
import com.anupom.covidinfo.viewholder.NewsHeadlineViewHolder;

import java.util.ArrayList;

public class NewsHeadlineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<String> newsHeadlinesArray = new ArrayList<>();
    private Context context;

    public NewsHeadlineAdapter(Context context, ArrayList<String> newsHeadlinesArray) {
        this.context            = context;
        this.newsHeadlinesArray = newsHeadlinesArray;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View headLineLayout = inflater.inflate(R.layout.news_headline_layout, parent, false);
        viewHolder          = new NewsHeadlineViewHolder(headLineLayout);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NewsHeadlineViewHolder breakingNewViewHolder = (NewsHeadlineViewHolder) holder;

        // now set the value in the news headline section
        breakingNewViewHolder.getNewsHeadline().setText(newsHeadlinesArray.get(position));
    }

    @Override
    public int getItemCount() {
        return newsHeadlinesArray.size();
    }
}
