package com.example.financiio;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TransactionsFragment extends Fragment {

    public int[] categoryImages = {R.drawable.beverage, R.drawable.electricity, R.drawable.health, R.drawable.house,
            R.drawable.internet, R.drawable.other, R.drawable.salary,
            R.drawable.transportation, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water,
            R.drawable.water, R.drawable.water};

    DatabaseReference inputRef, reference;
    FirebaseAuth mAuth;

    RecyclerView transactionsRecyclerView;

    String post_key = "";
    String myCategoryName = "";
    double myMoneyInput = 0;
    String myNoteText = "";
    String myDate = "";
    int myCategoryImage = 0;

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
        inputRef = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());

        TextView totalBalanceText = rootView.findViewById(R.id.balanceAmount);
        transactionsRecyclerView = rootView.findViewById(R.id.transactions_recycler_view);
        transactionsRecyclerView.setHasFixedSize(true);
        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Input> options = new FirebaseRecyclerOptions.Builder<Input>()
                .setQuery(inputRef, Input.class)
                .build();

        FirebaseRecyclerAdapter<Input, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Input, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Input model) {
                holder.setCategoryName(model.getCategoryName());
                holder.setDatePicker(model.getDate());
                holder.setMoneyInput(model.getMoneyInput());
                holder.setNoteText(model.getNoteText());

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
            newMoneyInput.setText(Double.toString(money));
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