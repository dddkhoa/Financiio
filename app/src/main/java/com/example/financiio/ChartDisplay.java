package com.example.financiio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.*;

public class ChartDisplay extends AppCompatActivity implements CategoryRecyclerViewInterface{

    private PieChart pieChart;
    private double moneySum;

    ArrayList<Integer> colors;

    DatabaseReference inputRef;
    ValueEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        TextView title = findViewById(R.id.categoryTitle);
        inputRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Transactions");

        pieChart = findViewById(R.id.activity_chart_piechart);

        Intent intent = getIntent();
        title.setText("Report");
        String curChart = intent.getStringExtra("name");

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean visit;
                moneySum = 0;
                ArrayList<Input> snapSpending = new ArrayList<>();
                ArrayList<Input> snapIncome = new ArrayList<>();
                snapIncome.clear();
                snapSpending.clear();
                for (DataSnapshot snap: snapshot.getChildren()) {
                    Input input = snap.getValue(Input.class);
                    visit = true;
                    if (input.getCategoryName().equals("Salary") | input.getCategoryName().equals("Saving") | input.getCategoryName().equals("Other Income")) {
                        for (Input curInput: snapIncome) {
                            if (curInput.getCategoryName().equals(input.getCategoryName())) {
                                curInput.setMoneyInput(curInput.getMoneyInput() + input.getMoneyInput());
                                visit = false;
                                break;
                            }
                        }
                        if (visit) snapIncome.add(input);
                        if (curChart.equals("Income")) {
                            moneySum += input.getMoneyInput();
                        }
                    }
                    else {
                        for (Input curInput: snapSpending) {
                            if (curInput.getCategoryName().equals(input.getCategoryName())) {
                                curInput.setMoneyInput(curInput.getMoneyInput() + input.getMoneyInput());
                                visit = false;
                                break;
                            }
                        }
                        if (visit) snapSpending.add(input);
                        if (curChart.equals("Spending")) {
                            moneySum += input.getMoneyInput();
                        }
                    }
                    setupPieChart(curChart);
                    if (curChart.equals("Spending"))  {
                        loadPieChartData(snapSpending);
                    }
                    else {
                        loadPieChartData(snapIncome);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        inputRef.addValueEventListener(listener);

    }

    private void setupPieChart(String name) {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(55f);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(0f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText(name + " by category");
        pieChart.setCenterTextSize(24f);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setTextSize(15f);
        l.setEnabled(true);

    }

    private void loadPieChartData(ArrayList<Input> spendingData) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Input input: spendingData) {
            float value = (float) (input.getMoneyInput()/moneySum);
            String label = input.getCategoryName();
            entries.add(new PieEntry(value,label));
        }

        colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        if (entries.isEmpty()) {
            entries.add(new PieEntry(1.0f, ""));
            colors = new ArrayList<>();
            colors.add(Color.parseColor("#dcdcdc"));
        }

        PieDataSet dataSet = new PieDataSet(entries, null);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.BLACK);
        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(900, Easing.EaseInOutQuad);
    }

    @Override
    public void onItemClick(int position) {
    }

}




