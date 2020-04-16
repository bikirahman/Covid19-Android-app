package com.anupom.covidinfo.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anupom.covidinfo.R;

public class BreakingNewsViewHolder extends RecyclerView.ViewHolder {

    private TextView newsTitle, newsDesc, dateView;
    private ImageView newsImage;

    public TextView getNewsTitle() {
        return newsTitle;
    }

    public TextView getNewsDesc() {
        return newsDesc;
    }

    public ImageView getNewsImage() {
        return newsImage;
    }

    public TextView getDateView() {
        return dateView;
    }

    public BreakingNewsViewHolder(@NonNull View itemView) {
        super(itemView);

        newsTitle = itemView.findViewById(R.id.titleview);
        newsDesc = itemView.findViewById(R.id.desctext);
        dateView = itemView.findViewById(R.id.dateView);

//        newsImage   = itemView.findViewById(R.id.newsImage);
    }
}
