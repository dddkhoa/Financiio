package com.example.financiio;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BudgetFragment extends Fragment implements BudgetRecyclerViewInterface{

    TextView budgetTitle, budgetDescription;
    Button addBudget;

    BudgetAdapter budgetAdapter;

    public int[] categoryImages = {R.drawable.ic_salary,  R.drawable.ic_saving, R.drawable.ic_other_income, R.drawable.ic_accessory,
            R.drawable.ic_book, R.drawable.ic_clothes, R.drawable.ic_computer,
            R.drawable.ic_cosmetic, R.drawable.ic_drink, R.drawable.ic_electric, R.drawable.ic_entertainmmment,
            R.drawable.ic_fitness, R.drawable.ic_food, R.drawable.ic_fuel, R.drawable.ic_gift, R.drawable.ic_grocery,
            R.drawable.ic_laundry, R.drawable.ic_loan, R.drawable.ic_medical,
            R.drawable.ic_phone,  R.drawable.ic_rental,
            R.drawable.ic_shopping, R.drawable.ic_transport, R.drawable.ic_water,R.drawable.ic_other};

    RecyclerView budgetRecyclerView;
    FirebaseDatabase database;
    DatabaseReference budgetRef, inputRef;
    FirebaseAuth mAuth;
    ArrayList<Budget> budgetList;



    public BudgetFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_budget, container, false);

        budgetTitle = rootView.findViewById(R.id.budgetTitle);
        budgetDescription = rootView.findViewById(R.id.budgetDescription);

        mAuth = FirebaseAuth.getInstance();

        addBudget = rootView.findViewById(R.id.addBudget);
        addBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UpdateBudgetActivity.class));
            }
        });

        database = FirebaseDatabase.getInstance();
        budgetRef = database.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Budget");
        inputRef = database.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Transactions");

        budgetRecyclerView = rootView.findViewById(R.id.budgetRecyclerView);
        budgetRecyclerView.setHasFixedSize(true);
        budgetRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        budgetList = new ArrayList<>();
        budgetAdapter = new BudgetAdapter(getContext(), budgetList, this);
        budgetRecyclerView.setAdapter(budgetAdapter);


        inputRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                budgetRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (budgetList != null) {
                            budgetList.clear();
                        }
                        for (DataSnapshot snap: snapshot.getChildren()) {
                            Budget budget = snap.getValue(Budget.class);
                            budgetList.add(budget);
                        }
                        budgetAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return rootView;
    }

    @Override
    public void onClick(int position) {

    }
}