package com.example.financiio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InputActivity extends AppCompatActivity {

    private EditText numberInput, noteText;
    private TextView categoryName, datePicker, saveInput;
    private ImageView categoryImage, noteImage, dateImage, backButton;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String NOTE = "Note";
    public static final String NUMBER = "Number";
    public static final String DATE = "Date";

    public int[] categoryImages = {R.drawable.ic_salary,  R.drawable.ic_saving, R.drawable.ic_other_income, R.drawable.ic_accessory,
            R.drawable.ic_book, R.drawable.ic_transport, R.drawable.ic_clothes, R.drawable.ic_drink, R.drawable.ic_computer,
            R.drawable.ic_cosmetic, R.drawable.ic_electric, R.drawable.ic_entertainmmment,
            R.drawable.ic_fitness, R.drawable.ic_food, R.drawable.ic_fuel, R.drawable.ic_gift, R.drawable.ic_grocery,
            R.drawable.ic_laundry, R.drawable.ic_loan, R.drawable.ic_medical,
            R.drawable.ic_phone,  R.drawable.ic_rental,
            R.drawable.ic_shopping, R.drawable.ic_water,R.drawable.ic_other};


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Input userInput;

    FirebaseDatabase database;
    DatabaseReference inputRef;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setContentView(R.layout.activity_input);

        mAuth = FirebaseAuth.getInstance();

        numberInput = (EditText) findViewById(R.id.moneyInput);
        categoryName =  (TextView) findViewById(R.id.categoryName);
        categoryImage = (ImageView) findViewById(R.id.categoryImage);
        noteText = (EditText) findViewById(R.id.noteText);
        noteImage = (ImageView) findViewById(R.id.noteImage);
        datePicker = (TextView) findViewById(R.id.datePicker);
        dateImage = (ImageView) findViewById(R.id.dateImage);
        backButton = (ImageView) findViewById(R.id.backButton);
        saveInput = (TextView) findViewById(R.id.saveText);


        categoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InputActivity.this, SelectCategoryActivity.class));
            }
        });


        Intent intent = getIntent();
        String currentCategoryName = intent.getStringExtra("Category");
        categoryName.setText(currentCategoryName);
        if (currentCategoryName != null) {
            if (currentCategoryName.equals("Salary") | currentCategoryName.equals("Other Income") | currentCategoryName.equals("Saving")) {
                numberInput.setTextColor(getResources().getColor(R.color.GREEN));
            } else {
                numberInput.setTextColor(getResources().getColor(R.color.RED));
            }
        }
        categoryImage.setImageResource(categoryImages[intent.getIntExtra("Image", categoryImages.length - 1)]);

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        final MaterialDatePicker materialDatePicker = builder.build();

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(selection -> datePicker.setText(materialDatePicker.getHeaderText()));

        saveInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberInput.getText() != null && categoryName.getText() != null & datePicker.getText() != null) {
                    String stringMoneyInput = numberInput.getText().toString();
                    String date = datePicker.getText().toString();
                    String category = categoryName.getText().toString();

                    if (stringMoneyInput.length() > 0 && category.length() > 0 && date.length() > 0) {
                        double moneyInput = Double.parseDouble(stringMoneyInput);
                        if (noteText.getText() != null) {
                            String note = noteText.getText().toString();
                            userInput = new Input(moneyInput, category, note, date);
                        } else {
                            userInput = new Input(moneyInput, category, null, date);
                        }

                        // Push User Input to Database
                        database = FirebaseDatabase.getInstance();
                        inputRef = database.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Transactions");
                        String id = inputRef.push().getKey();
                        inputRef.child(id).setValue(userInput).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(InputActivity.this, HomeActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(InputActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                        alert("You must select amount of spending/earning, its category, and a date.");
                    }
                } else {
                    alert("You must select amount of spending/earning, its category, and a date.");
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InputActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

    private void alert(String message) {
        AlertDialog dlg = new AlertDialog.Builder(InputActivity.this)
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
    protected double getDouble(@NonNull final SharedPreferences sharedPreferences, final String key, final double defaultValue) {
        if ( !sharedPreferences.contains(key))
            return defaultValue;
        return Double.longBitsToDouble(sharedPreferences.getLong(key, 0));
    }

    @Override
    protected void onPause() {
        super.onPause();
        double moneyInput = Double.parseDouble(numberInput.getText().toString());
        String note = noteText.getText().toString();
        String date = datePicker.getText().toString();
        editor.putLong(NUMBER, Double.doubleToRawLongBits(moneyInput));
        editor.putString(NOTE, note);
        editor.putString(DATE, date);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (numberInput.getText() != null) {
            double moneyInput = getDouble(sharedPreferences, NUMBER, 0);
            numberInput.setText(Double.toString(moneyInput));
        }

        if (noteText.getText() != null) {
            String note = sharedPreferences.getString(NOTE, "");
            noteText.setText(note);
        }

        if (datePicker.getText() != null) {
            String date = sharedPreferences.getString(DATE, "");
            datePicker.setText(date);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        editor.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        editor.clear();
        editor.commit();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        editor.clear();
        editor.commit();
    }
}