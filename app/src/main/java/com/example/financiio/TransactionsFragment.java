package com.example.financiio;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import com.google.firebase.database.ValueEventListener;


import java.util.Locale;

public class TransactionsFragment extends Fragment {
    public int[] categoryImages = {R.drawable.ic_salary, R.drawable.ic_saving, R.drawable.ic_other_income, R.drawable.ic_accessory,
            R.drawable.ic_book, R.drawable.ic_transport, R.drawable.ic_clothes, R.drawable.ic_drink, R.drawable.ic_computer,
            R.drawable.ic_cosmetic, R.drawable.ic_electric, R.drawable.ic_entertainmmment,
            R.drawable.ic_fitness, R.drawable.ic_food, R.drawable.ic_fuel, R.drawable.ic_gift, R.drawable.ic_grocery,
            R.drawable.ic_laundry, R.drawable.ic_loan, R.drawable.ic_medical,
            R.drawable.ic_phone, R.drawable.ic_rental,
            R.drawable.ic_shopping, R.drawable.ic_water, R.drawable.ic_other};

    FirebaseDatabase database;
    DatabaseReference inputRef, moneyRef;
    FirebaseAuth mAuth;
    ValueEventListener listener;

    RecyclerView transactionsRecyclerView;
    FirebaseRecyclerAdapter<Input, MyViewHolder> adapter;

    String post_key = "";
    String myCategoryName = "";
    double myMoneyInput = 0;
    String myNoteText = "";
    String myDate = "";
    int myCategoryImage = 0;
    static double walletTotal;

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
        database = FirebaseDatabase.getInstance();
        moneyRef = database.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("wallet");
        inputRef = database.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Transactions");

        TextView walletTotalText = rootView.findViewById(R.id.balanceAmount);
        transactionsRecyclerView = rootView.findViewById(R.id.transactions_recycler_view);
        transactionsRecyclerView.setHasFixedSize(true);
        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = inputRef.orderByChild("date");

        FirebaseRecyclerOptions<Input> options = new FirebaseRecyclerOptions.Builder<Input>()
                .setQuery(query, Input.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Input, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Input model) {
                holder.setCategoryName(model.getCategoryName());
                holder.setDatePicker(model.getDate());
                holder.setMoneyInput(model.getMoneyInput());
                holder.setNoteText(model.getNoteText());

                if (model.getCategoryName() != null) {
                    if (model.getCategoryName().equals("Salary") | model.getCategoryName().equals("Saving") | model.getCategoryName().equals("Other Income")) {
                        holder.moneyInput.setTextColor(getResources().getColor(R.color.GREEN));
                    } else {
                        holder.moneyInput.setTextColor(getResources().getColor(R.color.RED));
                    }
                }

                switch (model.getCategoryName()) {
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
                        holder.categoryImage.setImageResource(R.drawable.ic_transport);
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

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        post_key = getRef(holder.getAbsoluteAdapterPosition()).getKey();
                        myCategoryName = model.getCategoryName();
                        myMoneyInput = model.getMoneyInput();
                        myNoteText = model.getNoteText();

                        // NEED CHANGE FOR CATEGORY IMAGE HERE

                        myCategoryImage = model.getCategoryImage();
                        myDate = model.getDate();
                        updateData();
                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.transaction_recycler_view_row, parent, false);
                return new MyViewHolder(view);
            }
        };

        transactionsRecyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TransactionsFragment.walletTotal = 0;
                for (DataSnapshot snap: snapshot.getChildren()) {
                    Input input = snap.getValue(Input.class);
                    if (input.getCategoryName().equals("Salary") | input.getCategoryName().equals("Saving") | input.getCategoryName().equals("Other Income")) {
                        TransactionsFragment.walletTotal += input.getMoneyInput();
                    } else {
                        TransactionsFragment.walletTotal -= input.getMoneyInput();
                    }
                }
                walletTotalText.setText(String.format(Locale.getDefault(), "%,.1f", TransactionsFragment.walletTotal) + " VND");
                moneyRef.setValue(TransactionsFragment.walletTotal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        inputRef.addValueEventListener(listener);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        TransactionsFragment.walletTotal = 0;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

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
            newMoneyInput.setText(String.format(Locale.getDefault(), "%,.1f", money) + " VND");
        }
    }

    private void updateData() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View mView = inflater.inflate(R.layout.transactions_update, null);
        myDialog.setView(mView);

        final AlertDialog dialog = myDialog.create();

        ImageView updBtn = mView.findViewById(R.id.updBtn);
        ImageView delBtn = mView.findViewById(R.id.delBtn);

        EditText moneyInput = mView.findViewById(R.id.moneyInput);
        EditText noteText = mView.findViewById(R.id.noteText);
        TextView categoryName = mView.findViewById(R.id.categoryName);
        ImageView categoryImage = mView.findViewById(R.id.categoryImage);
        TextView datePicker = mView.findViewById(R.id.datePicker);

        moneyInput.setText(String.valueOf(myMoneyInput));
        categoryName.setText(myCategoryName);
        categoryImage.setImageResource(categoryImages[myCategoryImage]);
        noteText.setText(myNoteText);
        datePicker.setText(myDate);

        // LACKING CATEGORY SELECTION

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        final MaterialDatePicker materialDatePicker = builder.build();

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getChildFragmentManager(), "DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(selection -> datePicker.setText(materialDatePicker.getHeaderText()));

        updBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (moneyInput.getText() != null && categoryName.getText() != null & datePicker.getText() != null) {
                    String stringMoneyInput = moneyInput.getText().toString();
                    String date = datePicker.getText().toString();
                    String category = categoryName.getText().toString();

                    Input userInput;

                    if (stringMoneyInput.length() > 0 && category.length() > 0 && date.length() > 0) {
                        double moneyInput = Double.parseDouble(stringMoneyInput);
                        if (noteText.getText() != null) {
                            String note = noteText.getText().toString();
                            userInput = new Input(moneyInput, category, note, date);
                        } else {
                            userInput = new Input(moneyInput, category, null, date);
                        }

                        inputRef.child(post_key).setValue(userInput).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        alert("You must select amount of spending/earning, its category, and a date.");
                    }
                } else {
                    alert("You must select amount of spending/earning, its category, and a date.");
                }
                dialog.dismiss();
            }
        });


        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Delete this transaction?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                inputRef.child(post_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    private void alert(String message) {
        AlertDialog dlg = new AlertDialog.Builder(getContext())
                .setTitle("Oh no..")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        dlg.show();
    }
}