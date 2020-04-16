package com.anupom.covidinfo.viewholder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anupom.covidinfo.R;

public class InformationImageView extends RecyclerView.ViewHolder {

    private ImageView informationImage;

    public ImageView getInformationImage() {
        return informationImage;
    }

    public InformationImageView(@NonNull View itemView) {
        super(itemView);

        informationImage = itemView.findViewById(R.id.information_imageView);
    }
}
