package com.example.financiio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SelectCategoryActivity extends AppCompatActivity implements CategoryRecyclerViewInterface {

    ArrayList<CategoryModel> categoryModels = new ArrayList<>();
    public int[] categoryImages = {R.drawable.ic_salary,  R.drawable.ic_saving, R.drawable.ic_other_income, R.drawable.ic_accessory,
            R.drawable.ic_book, R.drawable.ic_clothes, R.drawable.ic_computer,
            R.drawable.ic_cosmetic, R.drawable.ic_drink, R.drawable.ic_electric, R.drawable.ic_entertainmmment,
            R.drawable.ic_fitness, R.drawable.ic_food, R.drawable.ic_fuel, R.drawable.ic_gift, R.drawable.ic_grocery,
            R.drawable.ic_laundry, R.drawable.ic_loan, R.drawable.ic_medical,
            R.drawable.ic_phone,  R.drawable.ic_rental,
            R.drawable.ic_shopping, R.drawable.ic_transport, R.drawable.ic_water,R.drawable.ic_other};

    public String[] categoryNames = {"Salary", "Saving", "Other Income", "Accessory", "Book",
            "Clothes", "Computer Bill",
            "Cosmetic","Drink", "Electricity Bill", "Entertainment Bill",
            "Fitness", "Food", "Fuel Bill", "Gift", "Grocery", "Laundry", "Loan",
            "Medical", "Phone Bill", "Rental Bill",
            "Shopping Bill", "Transportation", "Water Bill", "Other Bill"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        TextView selectCategoryText = (TextView) findViewById(R.id.selectCategory);
        ImageView backBtn = (ImageView) findViewById(R.id.backButton);
        ImageView findBtn = (ImageView) findViewById(R.id.findButton);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.category_recycler_view);

        setCategoryModels();
        CategoryRecyclerViewAdapter adapter = new CategoryRecyclerViewAdapter(this, categoryModels, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setCategoryModels() {
        for (int i=0; i<categoryNames.length; i++) {
            categoryModels.add(new CategoryModel(categoryNames[i], categoryImages[i]));
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(SelectCategoryActivity.this, InputActivity.class);
        CategoryModel currentCategory = categoryModels.get(position);
        intent.putExtra("Category", currentCategory.getCategoryName());
        intent.putExtra("Image", position);
        startActivity(intent);
    }

}