package com.example.financiio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

//public class TransactionsRecyclerViewAdapter extends RecyclerView.Adapter<TransactionsRecyclerViewAdapter.MyViewHolder> {
//
//    Context context;
//    ArrayList<Input> transactionModels;
//
//    public TransactionsRecyclerViewAdapter(Context context, ArrayList<Input> transactionModels) {
//        this.context = context;
//        this.transactionModels = transactionModels;
//    }
//
//    @NonNull
//    @Override
//    public TransactionsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.transaction_recycler_view_row, parent, false);
//        return new TransactionsRecyclerViewAdapter.MyViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull TransactionsRecyclerViewAdapter.MyViewHolder holder, int position) {
//        holder.moneyInput.setText(Double.toString(transactionModels.get(position).getMoneyInput()));
//        holder.categoryName.setText(transactionModels.get(position).getCategoryName());
//        holder.datePicker.setText(transactionModels.get(position).getDate());
//        holder.categoryImage.setImageResource(transactionModels.get(position).getCategoryImage());
//    }
//
//    @Override
//    public int getItemCount() {
//        return transactionModels.size();
//    }
//
//    public static class MyViewHolder extends RecyclerView.ViewHolder {
//
//        ImageView categoryImage;
//        TextView categoryName, noteText, datePicker, moneyInput;
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            categoryImage = itemView.findViewById(R.id.categoryImage);
//            categoryName = itemView.findViewById(R.id.categoryName);
//            noteText = itemView.findViewById(R.id.noteText);
//            datePicker = itemView.findViewById(R.id.datePicker);
//            moneyInput = itemView.findViewById(R.id.numberInput);
//        }
//    }
//}

public class TransactionsRecyclerViewAdapter extends FirebaseRecyclerAdapter<Input, TransactionsRecyclerViewAdapter.MyViewHolder> {

    public TransactionsRecyclerViewAdapter(@NonNull FirebaseRecyclerOptions<Input> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TransactionsRecyclerViewAdapter.MyViewHolder holder, int position, @NonNull Input model) {
        holder.setCategoryName(model.getCategoryName());
        holder.setDatePicker(model.getDate());
        holder.setMoneyInput(model.getMoneyInput());
        holder.setNoteText(model.getNoteText());

        String category = model.getCategoryName();

        if (category != null) {
            switch (model.getCategoryName()) {
                case "Salary":
                    holder.categoryImage.setImageResource(R.drawable.salary);
                    break;
                case "Other Income":
                    holder.categoryImage.setImageResource(R.drawable.other);
                    break;
                case "Food & Beverage":
                    holder.categoryImage.setImageResource(R.drawable.beverage);
                    break;
                case "Transportation":
                    holder.categoryImage.setImageResource(R.drawable.transportation);
                    break;
                case "Rentals":
                    holder.categoryImage.setImageResource(R.drawable.beverage);
                    break;
                case "Water Bill":
                    holder.categoryImage.setImageResource(R.drawable.water);
                    break;
                case "Electricity Bill":
                    holder.categoryImage.setImageResource(R.drawable.electricity);
                    break;
                case "Internet Bill":
                    holder.categoryImage.setImageResource(R.drawable.internet);
                    break;
                case "Accessories":
                    holder.categoryImage.setImageResource(R.drawable.beverage);
                    break;
                case "Other Spending":
                    holder.categoryImage.setImageResource(R.drawable.beverage);
                    break;
                case "Healthcare Bill":
                    holder.categoryImage.setImageResource(R.drawable.health);
                    break;
                case "Pets Bill":
                    holder.categoryImage.setImageResource(R.drawable.beverage);
                    break;
                case "Netflix Bill":
                    holder.categoryImage.setImageResource(R.drawable.beverage);
                    break;
                case "Spotify Bill":
                    holder.categoryImage.setImageResource(R.drawable.beverage);
                    break;
                default:
                    holder.categoryImage.setImageResource(R.drawable.salary);
                    break;
            }
        } else {
            holder.categoryImage.setImageResource(R.drawable.other);
        }
    }

    @NonNull
    @Override
    public TransactionsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.transaction_recycler_view_row, parent, false);
        return new MyViewHolder(view);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView categoryImage;
        TextView categoryName, noteText, datePicker, moneyInput;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.categoryImage);
            categoryName = itemView.findViewById(R.id.categoryName);
            noteText = itemView.findViewById(R.id.noteText);
            datePicker = itemView.findViewById(R.id.datePicker);
            moneyInput = itemView.findViewById(R.id.moneyInput);
        }

        public void setCategoryName(String name) {
            TextView newCategoryName = itemView.findViewById(R.id.categoryName);
            newCategoryName.setText(name);
        }

        public void setNoteText(String note) {
            TextView newNoteText = itemView.findViewById(R.id.noteText);
            newNoteText.setText(note);
        }

        public void setDatePicker(String date) {
            TextView newDatePicker = itemView.findViewById(R.id.datePicker);
            newDatePicker.setText(date);
        }

        public void setMoneyInput(double money) {
            TextView newMoneyInput = itemView.findViewById(R.id.moneyInput);
            newMoneyInput.setText(Double.toString(money));
        }
    }

}
