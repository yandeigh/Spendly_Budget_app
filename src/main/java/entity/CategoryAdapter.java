package com.example.spendly;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import entity.Category;
import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    public interface OnCategoryAction {
        void onAction(Category category);
    }

    private List<Category> categories = new ArrayList<>();
    private final OnCategoryAction onEdit;
    private final OnCategoryAction onDelete;

    public CategoryAdapter(OnCategoryAction onEdit, OnCategoryAction onDelete) {
        this.onEdit = onEdit;
        this.onDelete = onDelete;
    }

    public void setCategories(List<Category> list) {
        this.categories = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category cat = categories.get(position);
        holder.tvIcon.setText(cat.icon != null ? cat.icon : "📁");
        holder.tvName.setText(cat.name);
        if (cat.color != null) {
            try {
                holder.tvIcon.setBackgroundColor(Color.parseColor(cat.color));
            } catch (IllegalArgumentException ignored) {}
        }
        holder.btnEdit.setOnClickListener(v -> onEdit.onAction(cat));
        holder.btnDelete.setOnClickListener(v -> onDelete.onAction(cat));
    }

    @Override
    public int getItemCount() { return categories.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIcon, tvName;
        ImageButton btnEdit, btnDelete;

        ViewHolder(View v) {
            super(v);
            tvIcon = v.findViewById(R.id.tvCategoryIcon);
            tvName = v.findViewById(R.id.tvCategoryName);
            btnEdit = v.findViewById(R.id.btnEditCategory);
            btnDelete = v.findViewById(R.id.btnDeleteCategory);
        }
    }
}