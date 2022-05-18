package com.example.financiio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.MyViewHolder> {

    private final CategoryRecyclerViewInterface recyclerViewInterface;

    Context context;
    ArrayList<CategoryModel> categoryModels;
    public CategoryRecyclerViewAdapter(Context context, ArrayList<CategoryModel> categoryModels, CategoryRecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.categoryModels = categoryModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public CategoryRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout (Give a look to the rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.category_recycler_view_row, parent, false);
        return new CategoryRecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRecyclerViewAdapter.MyViewHolder holder, int position) {
        // Assign values to the views created in category_recycler_view_row.xml
        // based on the position of the recycler view
        holder.categoryNameTextView.setText(categoryModels.get(position).getCategoryName());
        holder.categoryImageView.setImageResource(categoryModels.get(position).getCategoryImage());
    }

    @Override
    public int getItemCount() {
        // return the number of items want to show
        return categoryModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // somewhat similar to onCreate method

        ImageView categoryImageView;
        TextView categoryNameTextView;

        public MyViewHolder(@NonNull View itemView, CategoryRecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            categoryImageView = itemView.findViewById(R.id.categoryImage);
            categoryNameTextView = itemView.findViewById(R.id.categoryName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
