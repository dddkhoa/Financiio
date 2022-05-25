package com.example.financiio;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TransactionsFragment extends Fragment {

    ArrayList<Input> transactionModels = new ArrayList<>();
    public int[] categoryImages = {R.drawable.beverage, R.drawable.electricity, R.drawable.health, R.drawable.house,
            R.drawable.internet, R.drawable.other, R.drawable.salary,
            R.drawable.transportation, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water,
            R.drawable.water, R.drawable.water};

    DatabaseReference inputRef;
    FirebaseAuth mAuth;

    RecyclerView transactionsRecyclerView;
    TransactionsRecyclerViewAdapter adapter;

    public TransactionsFragment() {
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

        View rootView = inflater.inflate(R.layout.fragment_transactions, container, false);

        mAuth = FirebaseAuth.getInstance();
        inputRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        Log.d("Ref", inputRef.get().toString());

        TextView totalBalanceText = rootView.findViewById(R.id.balanceAmount);
        transactionsRecyclerView = rootView.findViewById(R.id.transactions_recycler_view);
        transactionsRecyclerView.setHasFixedSize(true);
        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Input> options = new FirebaseRecyclerOptions.Builder<Input>()
                .setQuery(inputRef, Input.class)
                .build();

        Log.d("Options", options + "");

        adapter = new TransactionsRecyclerViewAdapter(options);
        transactionsRecyclerView.setAdapter(adapter);

        inputRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalAmount = 0;
                for (DataSnapshot snap: snapshot.getChildren()) {
                    Input input = snap.getValue(Input.class);
                    totalAmount += input.getMoneyInput();
                    totalBalanceText.setText(String.valueOf(totalAmount));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}