package com.anupom.covidinfo.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anupom.covidinfo.R;

public class NewsHeadlineViewHolder extends RecyclerView.ViewHolder {

    private TextView newsHeadline;

    public TextView getNewsHeadline() {
        return newsHeadline;
    }

    public NewsHeadlineViewHolder(@NonNull View itemView) {
        super(itemView);

        newsHeadline = itemView.findViewById(R.id.news_headline);
    }
}
