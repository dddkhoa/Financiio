package com.example.financiio.sms;

import android.util.Log;

import com.example.financiio.classifier.BayesClassifier;
import com.example.financiio.entity.Transaction;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Currency;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CALL:
 VietinBank: 11/06/2019 10:55|
 TK: 10686672420|GD:
 -5,050,843VND|SDC:
 15,636,427VND|ND: Rut tien
 mat tai ATM, the 0482

 */
public class BankTransactionReportParser implements TransactionReportParser {

    private static final String TAG = "BankTransactionReportParser";

    private static final String BANK_ACC_NO = "TK\\W*\\d{8,17}\\b";
    private static final String EXPENDITURE = "(\\-[\\d]+[,]?[\\d]+[,]?[\\d]*)(?=VND)";
    private static final String INCOME = "(\\+[\\d]+[,]?[\\d]+[,]?[\\d]*)(?=VND)";
    private static final String MONEY_IN_ACC = "([\\d]+[,]?[\\d]+[,]?[\\d]*)(?=VND)";
    private static final String DATE = "(\\s*\\d{2}\\/\\d{2}\\/\\d{4})";
    private static final String PRINT_OUT_PATTERN =  DATE + BANK_ACC_NO + EXPENDITURE + INCOME + MONEY_IN_ACC;


    private static final String DATE_FORMAT_1 = "dd/MM/yyyy";
    private static final String DATE_FORMAT_2 = "dd.MM.yyyy";

    private static final String DATE_OUTPUT_FORMAT = "dd.MM.yyyy";

    BayesClassifier classifier;

    public void setClassifier(BayesClassifier classifier) {
        this.classifier = classifier;
    }

    @Override
    public Transaction parse(String message) {
        if (message.startsWith("TK")) {
            return parsePut(message);
        } else if (message.startsWith("")) {
            return parseCall(message);
        } else {
            Log.i(TAG, "Undefined message: " + message);
            return null;
        }
    }

    private Transaction parsePut(String messageBody) {
        Transaction t = new Transaction();

        String[] sections = messageBody.split("\\.");
        Log.d(TAG, "PUT message: " + messageBody);
        Log.d(TAG, "Put message sections: " + Arrays.toString(sections));
        if (sections.length < 2) {
            return t;
        }

        t.setPut(true);
        Matcher matcher;
        Pattern pattern = Pattern.compile(PRINT_OUT_PATTERN);
        matcher = pattern.matcher(sections[0]);
        if (matcher.matches()) {
            t.setCardNumber(matcher.group(1));
            BigDecimal amount = new BigDecimal(matcher.group(3).replace(',', '.'));
            t.setAmount(amount);
            t.setCurrency(Currency.getInstance(matcher.group(4)));

            String dateString = matcher.group(2);
            t.setDate(parseDate(dateString));
        }

        Log.i(TAG, "PUT - " + t.toString());

        return t;
    }

    private Transaction parseCall(String messageBody) {
        String[] sections = messageBody.split(";");

        Log.d(TAG, "Call message sections: " + Arrays.toString(sections));

        Transaction t = new Transaction();
        Matcher matcher;

        t.setPut(false);

        // parse acc number
        Pattern accNumberPattern = Pattern.compile(BANK_ACC_NO);
        matcher = accNumberPattern.matcher(sections[0]);
        if (matcher.matches()) {
            String accNumber = matcher.group(1);
            t.setCardNumber(accNumber);
        }

        // parse transaction sum and currency
        Pattern moneyPattern = Pattern.compile(MONEY_IN_ACC);
        matcher = moneyPattern.matcher(sections[1]);
        if (matcher.matches()) {
            BigDecimal amount = new BigDecimal(matcher.group(1).replace(',', '.'));
            String currency = matcher.group(2);
            t.setAmount(amount);
            t.setCurrency(Currency.getInstance(currency));
        }

        // parse transaction date
        String dateString = sections[2].split(":")[1];
        t.setDate(parseDate(dateString));


        Log.i(TAG, "CALL - " + t.toString());
        return t;
    }

    private Date parseDate(String dateString) {
        Pattern pattern1 = Pattern.compile(DATE);
        Pattern pattern2 = Pattern.compile(DATE);
        SimpleDateFormat inputDateFormat = null;
        if (pattern1.matcher(dateString).matches()) {
            inputDateFormat = new SimpleDateFormat(DATE_FORMAT_1);
        } else if (pattern2.matcher(dateString).matches()) {
            inputDateFormat = new SimpleDateFormat(DATE_FORMAT_2);
        }

        try {
            if (inputDateFormat != null) {
                Date date = inputDateFormat.parse(dateString);
                return date;
            }
        } catch (ParseException e) {
            System.out.println(inputDateFormat.toString());
            e.printStackTrace();
        }
        return null;
    }

}
