package com.example.mad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class NGOAdapter extends RecyclerView.Adapter<NGOAdapter.NGOViewHolder> {

    private List<NGO> ngoList;
    private OnNGOClickListener listener;

    public interface OnNGOClickListener {
        void onDonateClick(NGO ngo);
    }

    public NGOAdapter(List<NGO> ngoList, OnNGOClickListener listener) {
        this.ngoList = ngoList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NGOViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ngo, parent, false);
        return new NGOViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NGOViewHolder holder, int position) {
        NGO ngo = ngoList.get(position);

        holder.tvNGOName.setText(ngo.getName());
        holder.tvLocation.setText(ngo.getLocation());
        holder.tvDescription.setText(ngo.getDescription());
        holder.tvRating.setText(String.valueOf(ngo.getRating()));
        holder.tvCampaigns.setText(ngo.getCampaigns() + " campaigns");
        holder.tvMeals.setText(ngo.getMeals() + " meals");

        // Show/hide verified badge
        holder.ivVerified.setVisibility(ngo.isVerified() ? View.VISIBLE : View.GONE);

        // Donate button click
        holder.btnDonate.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDonateClick(ngo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ngoList.size();
    }

    public void updateList(List<NGO> newList) {
        this.ngoList = newList;
        notifyDataSetChanged();
    }

    static class NGOViewHolder extends RecyclerView.ViewHolder {
        TextView tvNGOName, tvLocation, tvDescription, tvRating, tvCampaigns, tvMeals;
        ImageView ivVerified;
        MaterialButton btnDonate;

        public NGOViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNGOName = itemView.findViewById(R.id.tvNGOName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvCampaigns = itemView.findViewById(R.id.tvCampaigns);
            tvMeals = itemView.findViewById(R.id.tvMeals);
            ivVerified = itemView.findViewById(R.id.ivVerified);
            btnDonate = itemView.findViewById(R.id.btnDonate);
        }
    }
}
