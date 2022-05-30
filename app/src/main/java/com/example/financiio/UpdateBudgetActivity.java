package com.example.financiio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateBudgetActivity extends AppCompatActivity {
    private EditText newBudget;
    private TextView categoryName, saveInput;
    private ImageView categoryImage, backButton;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    FirebaseDatabase database;
    Budget updatedBudget;
    DatabaseReference budgetRef;
    FirebaseAuth mAuth;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String NUMBER = "Number";

    public int[] categoryImages = {R.drawable.ic_salary,  R.drawable.ic_saving, R.drawable.ic_other_income, R.drawable.ic_accessory,
            R.drawable.ic_book, R.drawable.ic_clothes, R.drawable.ic_computer,
            R.drawable.ic_cosmetic, R.drawable.ic_drink, R.drawable.ic_electric, R.drawable.ic_entertainmmment,
            R.drawable.ic_fitness, R.drawable.ic_food, R.drawable.ic_fuel, R.drawable.ic_gift, R.drawable.ic_grocery,
            R.drawable.ic_laundry, R.drawable.ic_loan, R.drawable.ic_medical,
            R.drawable.ic_phone,  R.drawable.ic_rental,
            R.drawable.ic_shopping, R.drawable.ic_transport, R.drawable.ic_water,R.drawable.ic_other};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setContentView(R.layout.activity_update_budget);

        mAuth = FirebaseAuth.getInstance();

        newBudget = (EditText) findViewById(R.id.budgetUpdate);
        categoryName =  (TextView) findViewById(R.id.budgetCategory);
        categoryImage = (ImageView) findViewById(R.id.categoryImage2);
        backButton = (ImageView) findViewById(R.id.backButtonn);
        saveInput = (TextView) findViewById(R.id.saveButton);

        categoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateBudgetActivity.this, SelectCategoryForBudgetActivity.class));
            }
        });
        Intent intent = getIntent();
        String currentCategoryName = intent.getStringExtra("Category");
        categoryName.setText(currentCategoryName);
        categoryImage
                .setImageResource(categoryImages[intent.getIntExtra("Image", categoryImages.length - 1)]);

        saveInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String valueUpdateBudget = newBudget.getText().toString();
                String category = categoryName.getText().toString();
                database = FirebaseDatabase.getInstance();
                budgetRef = database.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Budget");
                updatedBudget = new Budget(category, Double.parseDouble(valueUpdateBudget), 0, 0);

                String id = budgetRef.push().getKey();
                budgetRef.child(id).setValue(updatedBudget);

                Intent intent = new Intent(UpdateBudgetActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateBudgetActivity.this, HomeActivity.class));
            }
        });

    }
}