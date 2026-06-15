package com.example.spendly;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.ViewHolder> {

    private List<Achievement> achievements = new ArrayList<>();

    public void setAchievements(List<Achievement> list) {
        this.achievements = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_achievement, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Achievement a = achievements.get(position);
        holder.tvEmoji.setText(a.emoji);
        holder.tvTitle.setText(a.title);
        holder.tvDescription.setText(a.description);

        float alpha = a.unlocked ? 1f : 0.35f;
        holder.tvEmoji.setAlpha(alpha);
        holder.tvTitle.setAlpha(alpha);
        holder.tvDescription.setAlpha(alpha);
        holder.cardView.setCardBackgroundColor(a.unlocked ? 0xFF1A1A1A : 0xFF0D0D0D);
    }

    @Override
    public int getItemCount() {
        return achievements.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        androidx.cardview.widget.CardView cardView;
        TextView tvEmoji, tvTitle, tvDescription;

        ViewHolder(View v) {
            super(v);
            cardView = (androidx.cardview.widget.CardView) v;
            tvEmoji = v.findViewById(R.id.tvAchievementEmoji);
            tvTitle = v.findViewById(R.id.tvAchievementTitle);
            tvDescription = v.findViewById(R.id.tvAchievementDescription);
        }
    }
}
