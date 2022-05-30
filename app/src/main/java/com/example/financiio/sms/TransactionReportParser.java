package com.example.financiio.sms;

import com.example.financiio.entity.Transaction;

/**
 *
 */
public interface TransactionReportParser {
    Transaction parse(String message);
}
