package com.example.financiio;

public class Budget {

    private String categoryName;
    private double totalBudget, leftAmount, spendAmount;



    public Budget() {

    }

    public Budget(String categoryName, double totalBudget, double leftAmount, double spendAmount) {
        this.categoryName = categoryName;
        this.totalBudget = totalBudget;
        this.leftAmount = leftAmount;
        this.spendAmount = spendAmount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(double totalBudget) {
        this.totalBudget = totalBudget;
    }

    public double getLeftAmount() {
        return leftAmount;
    }

    public void setLeftAmount(double leftAmount) {
        this.leftAmount = leftAmount;
    }

    public double getSpendAmount() {
        return spendAmount;
    }

    public void setSpendAmount(double spendAmount) {
        this.spendAmount = spendAmount;
    }
}
