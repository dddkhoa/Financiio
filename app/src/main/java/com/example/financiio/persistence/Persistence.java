package com.example.financiio.persistence;

import com.example.financiio.entity.Transaction;

import java.util.List;

/**
 *
 */
public interface Persistence {

    List<Transaction> loadTransaction();

    void storeTransactions(List<Transaction> transactions);


}
