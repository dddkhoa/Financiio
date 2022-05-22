package com.example.financiio;

public class Input {
    private double moneyInput;
    private String categoryName;
    private String noteText;
    private String date;

    public Input() {}

    public Input(double moneyInput, String categoryName, String date) {
        this.moneyInput = moneyInput;
        this.categoryName = categoryName;
        this.noteText = "";
        this.date = date;
    }

    public Input(double moneyInput, String categoryName,  String noteText, String date) {
        this.moneyInput = moneyInput;
        this.categoryName = categoryName;
        this.noteText = noteText;
        this.date = date;
    }

    public double getMoneyInput() {
        return moneyInput;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getNoteText() {
        return noteText;
    }

    public String getDate() {
        return date;
    }
}
