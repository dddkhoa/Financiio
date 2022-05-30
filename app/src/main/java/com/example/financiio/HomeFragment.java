package com.example.financiio;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.params.BlackLevelPattern;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.drawable.Drawable;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.ValueEventListener;

import java.util.*;

public class HomeFragment extends Fragment {


    private PieChart pieChart,incomePieChart;

    FirebaseDatabase database;
    DatabaseReference inputRef, moneyRef;
    FirebaseAuth mAuth;
    ValueEventListener listener;

    static double spendingTotal,incomeTotal;

    ArrayList<Integer> colors = new ArrayList<>();

    RecyclerView transactionsRecyclerView;
    FirebaseRecyclerAdapter<Input, TransactionsFragment.MyViewHolder> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        moneyRef = database.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("wallet");
        inputRef = database.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Transactions");

        TextView spendingTotalText = rootView.findViewById(R.id.spendingAmount);
        TextView incomeTotalText = rootView.findViewById(R.id.incomeAmount);

        pieChart = rootView.findViewById(R.id.spending_piechart);
        incomePieChart = rootView.findViewById(R.id.income_piechart);

        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        pieChart.setClickable(true);
        pieChart.setHighlightPerTapEnabled(false);
        pieChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture){

            }
            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture){

            }
            @Override
            public void onChartLongPressed(MotionEvent me){

            }
            @Override
            public void onChartDoubleTapped(MotionEvent me){

            }
            @Override
            public void onChartSingleTapped(MotionEvent me){
                Intent intent = new Intent(getActivity(), ChartDisplay.class);
                intent.putExtra("name", "Spending");
                startActivity(intent);
            }
            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY){

            }
            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY){

            }
            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY){

            }
        });

        incomePieChart.setClickable(true);
        incomePieChart.setHighlightPerTapEnabled(false);
        incomePieChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture){

            }
            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture){

            }
            @Override
            public void onChartLongPressed(MotionEvent me){

            }
            @Override
            public void onChartDoubleTapped(MotionEvent me){

            }
            @Override
            public void onChartSingleTapped(MotionEvent me){
                Intent intent = new Intent(getActivity(), ChartDisplay.class);
                intent.putExtra("name", "Income");
                startActivity(intent);
            }
            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY){

            }
            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY){

            }
            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY){

            }
        });

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HomeFragment.spendingTotal = 0;
                HomeFragment.incomeTotal = 0;
                boolean visit;
                ArrayList<Input> snapSpending = new ArrayList<>();
                ArrayList<Input> snapIncome = new ArrayList<>();
                for (DataSnapshot snap: snapshot.getChildren()) {
                    Input input = snap.getValue(Input.class);
                    if (input.getCategoryName().equals("Salary") | input.getCategoryName().equals("Saving") | input.getCategoryName().equals("Other Income")) {
                        visit = true;
                        for (Input curInput: snapIncome) {
                            if (curInput.getCategoryName().equals(input.getCategoryName())) {
                                curInput.setMoneyInput(curInput.getMoneyInput() + input.getMoneyInput());
                                visit = false;
                                break;
                            }
                        }
                        if (visit) snapIncome.add(input);
                        HomeFragment.incomeTotal += input.getMoneyInput();
                    } else {
                        visit = true;
                        for (Input curInput: snapSpending) {
                            if (curInput.getCategoryName().equals(input.getCategoryName())) {
                                curInput.setMoneyInput(curInput.getMoneyInput() + input.getMoneyInput());
                                visit = false;
                                break;
                            }
                        }
                        if (visit) snapSpending.add(input);
                        HomeFragment.spendingTotal += input.getMoneyInput();
                    }
                }
                spendingTotalText.setText(String.format(Locale.getDefault(), "%,.1f", HomeFragment.spendingTotal) + "VND");
                incomeTotalText.setText(String.format(Locale.getDefault(), "%,.1f", HomeFragment.incomeTotal) + "VND");
                moneyRef.setValue(HomeFragment.spendingTotal);
                pieChart = buildPieChart(pieChart, snapSpending, "Expense");
                incomePieChart = buildPieChart(incomePieChart, snapIncome, "Income");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        inputRef.addValueEventListener(listener);
        return rootView;
    }

    private PieChart buildPieChart(PieChart piechart, ArrayList<Input> spendingData, String name) {

        piechart.setDrawHoleEnabled(true);
        piechart.setUsePercentValues(true);
        piechart.setHoleRadius(55f);
        piechart.setEntryLabelTextSize(14f);
        piechart.setEntryLabelColor(Color.BLACK);
        piechart.setCenterText(name);
        piechart.setCenterTextSize(24f);
        piechart.getDescription().setEnabled(false);
        piechart.getLegend().setEnabled(false);

        piechart.setExtraBottomOffset(20f);

        double moneySum = spendingTotal;
        if (name.equals("Income")) moneySum = incomeTotal;

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Input input: spendingData) {
            float value = (float) (input.getMoneyInput()/moneySum);
            String label = input.getCategoryName();
            entries.add(new PieEntry(value, label));
        }

        ArrayList<Integer> curColors;
        if (entries.isEmpty()) {
            entries.add(new PieEntry(1.0f, ""));
            curColors = new ArrayList<>();
            curColors.add(Color.parseColor("#dcdcdc"));
        }
        else curColors = colors;
        PieDataSet dataSet = new PieDataSet(entries, null);
        dataSet.setColors(curColors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(piechart));
        data.setValueTextSize(0f);
        data.setValueTextColor(Color.BLACK);

        piechart.setData(data);
        piechart.invalidate();

        piechart.animateY(800, Easing.EaseInOutQuad);

        return piechart;
    }

}