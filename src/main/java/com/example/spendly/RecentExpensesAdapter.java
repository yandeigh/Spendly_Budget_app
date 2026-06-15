package com.example.spendly;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import entity.Expense;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecentExpensesAdapter extends RecyclerView.Adapter<RecentExpensesAdapter.ViewHolder> {

    public interface OnExpenseActionListener {
        void onEdit(Expense expense);
        void onDelete(Expense expense);
    }

    private List<Expense> expenses = new ArrayList<>();
    private final NumberFormat currencyFormat;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private OnExpenseActionListener listener;

    public RecentExpensesAdapter(NumberFormat currencyFormat) {
        this.currencyFormat = currencyFormat;
    }

    public void setListener(OnExpenseActionListener listener) {
        this.listener = listener;
    }

    public void setExpenses(List<Expense> list) {
        this.expenses = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    public Expense getExpenseAt(int position) {
        return expenses.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recent_expense, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense e = expenses.get(position);
        holder.tvDescription.setText(e.description);
        holder.tvAmount.setText(currencyFormat.format(e.amount));
        holder.tvDate.setText(dateFormat.format(new Date(e.date)));

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(e);
        });
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(e);
        });
    }

    @Override
    public int getItemCount() { return expenses.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription, tvAmount, tvDate;
        ImageButton btnEdit, btnDelete;

        ViewHolder(View v) {
            super(v);
            tvDescription = v.findViewById(R.id.tvExpenseDescription);
            tvAmount      = v.findViewById(R.id.tvExpenseAmount);
            tvDate        = v.findViewById(R.id.tvExpenseDate);
            btnEdit       = v.findViewById(R.id.btnEditExpense);
            btnDelete     = v.findViewById(R.id.btnDeleteExpense);
        }
    }
}