package com.example.financiio;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.MyViewHolder> {

    private final BudgetRecyclerViewInterface budgetRecyclerViewInterface;

    Context context;
    ArrayList<Budget> budgetList;

    public BudgetAdapter(Context context, ArrayList<Budget> budgetList, BudgetRecyclerViewInterface budgetRecyclerViewInterface) {
        this.context = context;
        this.budgetList = budgetList;
        this.budgetRecyclerViewInterface = budgetRecyclerViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.budget_recycler_row, parent, false);
        return  new MyViewHolder(view, budgetRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Budget budget = budgetList.get(position);
        holder.categoryName.setText(budget.getCategoryName());
        switch (budget.getCategoryName()) {
            case "Salary":
                holder.categoryImage.setImageResource(R.drawable.ic_salary);
                break;
            case "Saving":
                holder.categoryImage.setImageResource(R.drawable.ic_saving);
                break;
            case "Other Income":
                holder.categoryImage.setImageResource(R.drawable.ic_other_income);
                break;
            case "Accessory":
                holder.categoryImage.setImageResource(R.drawable.ic_accessory);
                break;
            case "Book":
                holder.categoryImage.setImageResource(R.drawable.ic_book);
                break;
            case "Clothes":
                holder.categoryImage.setImageResource(R.drawable.ic_clothes);
                break;
            case "Computer Bill":
                holder.categoryImage.setImageResource(R.drawable.ic_computer);
                break;
            case "Cosmetic":
                holder.categoryImage.setImageResource(R.drawable.ic_cosmetic);
                break;
            case "Drink":
                holder.categoryImage.setImageResource(R.drawable.ic_drink);
                break;
            case "Electricity Bill":
                holder.categoryImage.setImageResource(R.drawable.ic_electric);
                break;
            case "Entertainment Bill":
                holder.categoryImage.setImageResource(R.drawable.ic_entertainmmment);
                break;
            case "Fitness":
                holder.categoryImage.setImageResource(R.drawable.ic_fitness);
                break;
            case "Food":
                holder.categoryImage.setImageResource(R.drawable.ic_food);
                break;
            case "Gift":
                holder.categoryImage.setImageResource(R.drawable.ic_gift);
                break;
            case "Grocery":
                holder.categoryImage.setImageResource(R.drawable.ic_grocery);
                break;
            case "Laundry":
                holder.categoryImage.setImageResource(R.drawable.ic_laundry);
                break;
            case "Loan":
                holder.categoryImage.setImageResource(R.drawable.ic_loan);
                break;
            case "Medical":
                holder.categoryImage.setImageResource(R.drawable.ic_medical);
                break;
            case "Phone Bill":
                holder.categoryImage.setImageResource(R.drawable.ic_phone);
                break;
            case "Rental Bill":
                holder.categoryImage.setImageResource(R.drawable.ic_rental);
                break;
            case "Shopping Bill":
                holder.categoryImage.setImageResource(R.drawable.ic_shopping);
                break;
            case "Transportation":
                holder.categoryImage.setImageResource(R.drawable.ic_transport);
                break;
            case "Water Bill":
                holder.categoryImage.setImageResource(R.drawable.ic_water);
                break;
            case "Other Bill":
                holder.categoryImage.setImageResource(R.drawable.ic_other);
                break;
            default:
                holder.categoryImage.setImageResource(R.drawable.ic_accessory);
                break;
        }
        holder.leftText.setText("Left");
        holder.spendText.setText("Spent");
        holder.totalBudget.setText(String.format(Locale.getDefault(), "%,.1f", budget.getTotalBudget()) + "VND");
        holder.leftAmount.setText(String.format(Locale.getDefault(), "%,.1f", budget.getLeftAmount()) + "VND");
        holder.spendAmount.setText(String.format(Locale.getDefault(), "%,.1f", budget.getSpendAmount()) + "VND");
        holder.budgetProgress.setMax((int) budget.getTotalBudget());
        holder.budgetProgress.setProgress((int) budget.getSpendAmount());

    }

    @Override
    public int getItemCount() {
        return budgetList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName, spendText, spendAmount, leftText, leftAmount, totalBudget;
        ImageView categoryImage;
        ProgressBar budgetProgress;


        public MyViewHolder(@NonNull View itemView, BudgetRecyclerViewInterface budgetRecyclerViewInterface) {
            super(itemView);
            totalBudget = itemView.findViewById(R.id.totalBudget);
            categoryName = itemView.findViewById(R.id.categoryName);
            spendText = itemView.findViewById(R.id.spendText);
            spendAmount = itemView.findViewById(R.id.spendAmount);
            leftText = itemView.findViewById(R.id.leftText);
            leftAmount = itemView.findViewById(R.id.leftAmount);
            categoryImage = itemView.findViewById(R.id.categoryImage);
            budgetProgress = itemView.findViewById(R.id.budgetProgress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        budgetRecyclerViewInterface.onClick(position);
                    }
                }
            });
        }
    }
}
