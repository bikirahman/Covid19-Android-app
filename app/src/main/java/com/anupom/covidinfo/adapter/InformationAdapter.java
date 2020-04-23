package com.anupom.covidinfo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.anupom.covidinfo.R;
import com.anupom.covidinfo.viewholder.InformationImageView;
import com.anupom.covidinfo.viewholder.InformationTextViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class InformationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //------------------------------------
    // define the constant variables
    private final int TEXT_CONTENT = 0;
    private final int IMAGE_CONTENT = 1;
    //------------------------------------


    //------------------------------------------------------------------------
    // define the store variables
    private Context context;
    private ArrayList<Integer> recyclerViewContent = new ArrayList<>();
    private ArrayList<String> informationContent = new ArrayList<>();
    //------------------------------------------------------------------------


    public InformationAdapter(Context context, ArrayList<Integer> recyclerViewContent, ArrayList<String> informationContent) {
        this.context = context;
        this.recyclerViewContent = recyclerViewContent;
        this.informationContent = informationContent;

        Log.e("TAG", "InformationAdapter: " + informationContent);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TEXT_CONTENT:
                View textInformation = inflater.inflate(R.layout.information_text_layout, parent, false);
                viewHolder = new InformationTextViewHolder(textInformation);
                break;
            case IMAGE_CONTENT:
                View imageInformation = inflater.inflate(R.layout.information_image_layout, parent, false);
                viewHolder = new InformationImageView(imageInformation);
                break;
            default:
                View defaultInformation = inflater.inflate(R.layout.information_text_layout, parent, false);
                viewHolder = new InformationTextViewHolder(defaultInformation);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TEXT_CONTENT:
                InformationTextViewHolder textViewHolder = (InformationTextViewHolder) holder;
                textViewHolder.getInformationText().setText(informationContent.get(position));
                break;
            case IMAGE_CONTENT:
                InformationImageView imageViewHolder = (InformationImageView) holder;

                // get the image link
                String imageLink = informationContent.get(position);
                Log.e("link", "" + imageLink);
                //Picasso.get().load(R.drawable.logo).fit().into(imageViewHolder.getInformationImage());
                Picasso.get().load("http://api.indigierp.com/helpline_files/" + imageLink).fit().into(imageViewHolder.getInformationImage());
                //imageViewHolder.getInformationImage().setImageResource(/*R.drawable.welcome_bg*/));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return recyclerViewContent.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (recyclerViewContent.get(position) == TEXT_CONTENT) {
            return TEXT_CONTENT;
        } else if (recyclerViewContent.get(position) == IMAGE_CONTENT) {
            return IMAGE_CONTENT;
        }

        return -1;
    }

}
