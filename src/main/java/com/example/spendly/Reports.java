package com.example.spendly;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import Dao.ExpenseDao;
import entity.AppDatabase;
import entity.BudgetGoal;
import entity.Category;
import entity.Expense;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;

public class Reports extends AppCompatActivity {

    private TextView tvTotalSpent, tvTransactionCount;
    private Button btnStartDate, btnEndDate;
    private PieChart pieChart;
    private BarChart barChart;
    private AppDatabase db;
    private Calendar startCal, endCal;
    private Map<Integer, String> categoryNames = new HashMap<>();
    private BudgetGoal budgetGoal = null;
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "ZA"));
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        db = AppDatabase.getDatabase(this);
        tvTotalSpent       = findViewById(R.id.tvTotalSpent);
        tvTransactionCount = findViewById(R.id.tvTransactionCount);
        btnStartDate       = findViewById(R.id.btnStartDate);
        btnEndDate         = findViewById(R.id.btnEndDate);
        pieChart           = findViewById(R.id.pieChart);
        barChart           = findViewById(R.id.barChart);

        setupPieChart();
        setupBarChart();

        startCal = Calendar.getInstance();
        startCal.set(Calendar.DAY_OF_MONTH, 1);
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);

        endCal = Calendar.getInstance();
        endCal.set(Calendar.HOUR_OF_DAY, 23);
        endCal.set(Calendar.MINUTE, 59);
        endCal.set(Calendar.SECOND, 59);
        endCal.set(Calendar.MILLISECOND, 999);

        updateDateLabels();

        db.categoryDao().getAllCategories().observe(this, categories -> {
            categoryNames.clear();
            if (categories != null) {
                for (Category c : categories) categoryNames.put(c.id, c.name);
            }
            loadReportData();
        });

        db.budgetGoalDao().getBudgetGoal().observe(this, goal -> {
            budgetGoal = goal;
            loadReportData();
        });

        btnStartDate.setOnClickListener(v ->
                new DatePickerDialog(this, (view, year, month, day) -> {
                    startCal.set(year, month, day);
                    startCal.set(Calendar.HOUR_OF_DAY, 0);
                    startCal.set(Calendar.MINUTE, 0);
                    startCal.set(Calendar.SECOND, 0);
                    startCal.set(Calendar.MILLISECOND, 0);
                    updateDateLabels();
                    loadReportData();
                }, startCal.get(Calendar.YEAR), startCal.get(Calendar.MONTH),
                        startCal.get(Calendar.DAY_OF_MONTH)).show());

        btnEndDate.setOnClickListener(v ->
                new DatePickerDialog(this, (view, year, month, day) -> {
                    endCal.set(year, month, day);
                    endCal.set(Calendar.HOUR_OF_DAY, 23);
                    endCal.set(Calendar.MINUTE, 59);
                    endCal.set(Calendar.SECOND, 59);
                    endCal.set(Calendar.MILLISECOND, 999);
                    updateDateLabels();
                    loadReportData();
                }, endCal.get(Calendar.YEAR), endCal.get(Calendar.MONTH),
                        endCal.get(Calendar.DAY_OF_MONTH)).show());
    }

    private void updateDateLabels() {
        btnStartDate.setText("From: " + dateFormat.format(startCal.getTime()));
        btnEndDate.setText("To: " + dateFormat.format(endCal.getTime()));
    }

    private void loadReportData() {
        long start = startCal.getTimeInMillis();
        long end   = endCal.getTimeInMillis();

        Executors.newSingleThreadExecutor().execute(() -> {
            List<Expense> expenses = db.expenseDao().getExpensesInRangeSync(start, end);
            List<ExpenseDao.CategoryTotal> totals = db.expenseDao().getCategoryTotalsInRangeSync(start, end);

            runOnUiThread(() -> {
                double total = 0;
                if (expenses != null) for (Expense e : expenses) total += e.amount;
                tvTotalSpent.setText(currencyFormat.format(total));
                int count = expenses != null ? expenses.size() : 0;
                tvTransactionCount.setText(count + " transaction" + (count == 1 ? "" : "s"));

                updatePieChart(totals);
                updateBarChart(totals);
            });
        });
    }

    private void updatePieChart(List<ExpenseDao.CategoryTotal> totals) {
        if (totals == null || totals.isEmpty()) {
            pieChart.clear();
            pieChart.setNoDataText("No data for this period");
            pieChart.invalidate();
            return;
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (ExpenseDao.CategoryTotal ct : totals) {
            String name = categoryNames.containsKey(ct.categoryId)
                    ? categoryNames.get(ct.categoryId) : "Unknown";
            entries.add(new PieEntry((float) ct.total, name));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(11f);
        data.setValueTextColor(0xFFFFFFFF);

        pieChart.setData(data);
        pieChart.animateY(1000);
        pieChart.invalidate();
    }

    private void updateBarChart(List<ExpenseDao.CategoryTotal> totals) {
        barChart.getAxisLeft().removeAllLimitLines();

        if (totals == null || totals.isEmpty()) {
            barChart.clear();
            barChart.setNoDataText("No expenses for this period");
            barChart.invalidate();
            return;
        }

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels   = new ArrayList<>();
        for (int i = 0; i < totals.size(); i++) {
            ExpenseDao.CategoryTotal ct = totals.get(i);
            entries.add(new BarEntry(i, (float) ct.total));
            String name = categoryNames.containsKey(ct.categoryId)
                    ? categoryNames.get(ct.categoryId) : "Unknown";
            labels.add(name);
        }

        BarDataSet dataSet = new BarDataSet(entries, "Spent");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(0xFFFFFFFF);
        dataSet.setValueTextSize(10f);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.6f);

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getXAxis().setLabelCount(labels.size());

        if (budgetGoal != null) {
            if (budgetGoal.maxMonthlyGoal > 0) {
                LimitLine maxLine = new LimitLine((float) budgetGoal.maxMonthlyGoal, "Max goal");
                maxLine.setLineColor(0xFFE53935);
                maxLine.setTextColor(0xFFE53935);
                maxLine.setLineWidth(1.5f);
                maxLine.enableDashedLine(10f, 8f, 0f);
                maxLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                barChart.getAxisLeft().addLimitLine(maxLine);
            }
            if (budgetGoal.minMonthlyGoal > 0) {
                LimitLine minLine = new LimitLine((float) budgetGoal.minMonthlyGoal, "Min goal");
                minLine.setLineColor(0xFF4CAF50);
                minLine.setTextColor(0xFF4CAF50);
                minLine.setLineWidth(1.5f);
                minLine.enableDashedLine(10f, 8f, 0f);
                minLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
                barChart.getAxisLeft().addLimitLine(minLine);
            }
        }

        barChart.setData(data);
        barChart.animateY(800);
        barChart.invalidate();
    }

    private void setupPieChart() {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setHoleRadius(58f);
        pieChart.setCenterText("Spending");
        pieChart.setCenterTextColor(0xFFFFFFFF);
        pieChart.setCenterTextSize(18f);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setTextColor(0xFFFFFFFF);
    }

    private void setupBarChart() {
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setFitBars(true);
        barChart.setNoDataText("No expenses for this period");
        barChart.setNoDataTextColor(0xFF888888);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(0xFFFFFFFF);
        xAxis.setDrawGridLines(false);

        barChart.getAxisLeft().setTextColor(0xFFFFFFFF);
        barChart.getAxisLeft().setAxisMinimum(0f);
        barChart.getAxisRight().setEnabled(false);

        Legend l = barChart.getLegend();
        l.setTextColor(0xFFFFFFFF);
    }
}