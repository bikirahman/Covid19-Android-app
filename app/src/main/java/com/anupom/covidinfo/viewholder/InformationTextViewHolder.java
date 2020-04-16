package com.anupom.covidinfo.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anupom.covidinfo.R;

public class InformationTextViewHolder extends RecyclerView.ViewHolder {

    private TextView informationText;

    public TextView getInformationText() {
        return informationText;
    }

    public InformationTextViewHolder(@NonNull View itemView) {
        super(itemView);

        informationText = itemView.findViewById(R.id.information_textView);
    }
}
