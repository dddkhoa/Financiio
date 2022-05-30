package com.example.financiio;

public class Input {
    private double moneyInput;
    private String categoryName;
    private String inputID;
    private String noteText;
    private String date;
    private int categoryImage, day, month, year;
    private long dateInMilliseconds, invertedDateInMilliseconds;

    public Input() {}


    public Input(double moneyInput, String categoryName, String noteText, String date, int categoryImage) {
        this.moneyInput = moneyInput;
        this.categoryName = categoryName;
        this.noteText = noteText;
        this.date = date;
        this.categoryImage = categoryImage;
    }

    public Input(double moneyInput, String categoryName,  String noteText, String date) {
        this.moneyInput = moneyInput;
        this.categoryName = categoryName;
        this.noteText = noteText;
        this.date = date;
    }

    public Input(double moneyInput, String categoryName, String noteText, String date, int day, int month, int year, long dateInMilliseconds) {
        this.moneyInput = moneyInput;
        this.categoryName = categoryName;
        this.noteText = noteText;
        this.date = date;
        this.day = day;
        this.month = month;
        this.year = year;
        this.dateInMilliseconds = dateInMilliseconds;
        this.invertedDateInMilliseconds = -1 * dateInMilliseconds;
    }


    public double getMoneyInput() {
        return moneyInput;
    }

    public void setMoneyInput(double moneyInput) {
        this.moneyInput = moneyInput;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getInputID() {
        return inputID;
    }

    public void setInputID(String inputID) {
        this.inputID = inputID;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(int categoryImage) {
        this.categoryImage = categoryImage;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public long getDateInMilliseconds() {
        return dateInMilliseconds;
    }

    public void setDateInMilliseconds(long dateInMilliseconds) {
        this.dateInMilliseconds = dateInMilliseconds;
    }

    public long getInvertedDateInMilliseconds() {
        return invertedDateInMilliseconds;
    }

    public void setInvertedDateInMilliseconds(long invertedDateInMilliseconds) {
        this.invertedDateInMilliseconds = invertedDateInMilliseconds;
    }
}
