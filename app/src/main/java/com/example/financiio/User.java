package com.example.financiio;

public class User {


    public String fullName, email;
    public int expense;

    public User() {

    }

    public User(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
        this.expense = 0;
    }

}