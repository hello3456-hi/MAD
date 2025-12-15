package com.example.mad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.DonationViewHolder> {

    private List<Donation> donationList;

    public DonationAdapter(List<Donation> donationList) {
        this.donationList = donationList;
    }

    @NonNull
    @Override
    public DonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_donation, parent, false);
        return new DonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonationViewHolder holder, int position) {
        Donation donation = donationList.get(position);
        holder.tvCampaignName.setText(donation.getCampaignName());
        holder.tvDonationDate.setText(donation.getDate() + " - " + donation.getMeals() + " meals");
        holder.tvDonationAmount.setText("$" + donation.getAmount());
    }

    @Override
    public int getItemCount() {
        return donationList.size();
    }

    public void updateList(List<Donation> newList) {
        this.donationList = newList;
        notifyDataSetChanged();
    }

    static class DonationViewHolder extends RecyclerView.ViewHolder {
        TextView tvCampaignName, tvDonationDate, tvDonationAmount;

        public DonationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCampaignName = itemView.findViewById(R.id.tvCampaignName);
            tvDonationDate = itemView.findViewById(R.id.tvDonationDate);
            tvDonationAmount = itemView.findViewById(R.id.tvDonationAmount);
        }
    }
}
