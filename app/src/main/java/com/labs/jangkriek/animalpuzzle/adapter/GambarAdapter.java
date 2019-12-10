package com.labs.jangkriek.animalpuzzle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.labs.jangkriek.animalpuzzle.R;
import com.labs.jangkriek.animalpuzzle.activity.ChoosePhotosGridActivity;
import com.labs.jangkriek.animalpuzzle.model.Gambar;

import java.util.ArrayList;

public class GambarAdapter extends RecyclerView.Adapter<GambarAdapter.GambarVIewHolder> {

    private ArrayList<Gambar> listGambar;
    private Context context;

    public GambarAdapter(ArrayList<Gambar> datalist ){
        this.listGambar = datalist;
    }

    @NonNull
    @Override
    public GambarAdapter.GambarVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_image, parent, false);
        return new GambarVIewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final GambarAdapter.GambarVIewHolder holder, final int position) {
        holder.imageView.setImageResource(listGambar.get(position).getImageView());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return listGambar.size();
    }

    public class GambarVIewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private CardView singleCard;
        ChoosePhotosGridActivity.ItemClickListener click;

        public GambarVIewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
            singleCard = itemView.findViewById(R.id.cardView);
            singleCard.setOnClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public void setClickListener(ChoosePhotosGridActivity.ItemClickListener itemClickListener){
            this.click = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            click.onClick(v, getLayoutPosition());
        }
    }
}
