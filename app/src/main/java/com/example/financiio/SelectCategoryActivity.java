package com.example.financiio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class SelectCategoryActivity extends AppCompatActivity implements CategoryRecyclerViewInterface {

    ArrayList<CategoryModel> categoryModels = new ArrayList<>();
    int[] categoryImages = {R.drawable.beverage, R.drawable.electricity, R.drawable.health, R.drawable.house,
            R.drawable.internet, R.drawable.other, R.drawable.salary,
    R.drawable.transportation, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water,
    R.drawable.water, R.drawable.water};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.category_recycler_view);

        setCategoryModels();
        CategoryRecyclerViewAdapter adapter = new CategoryRecyclerViewAdapter(this, categoryModels, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setCategoryModels() {
        String[] categoryNames = getResources().getStringArray(R.array.categories);
        for (int i=0; i<categoryNames.length; i++) {
            categoryModels.add(new CategoryModel(categoryNames[i], categoryImages[i]));
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(SelectCategoryActivity.this, InputActivity.class);
        startActivity(intent);
    }
}