package com.example.financiio.view;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.example.financiio.R;
import com.example.financiio.classifier.BayesClassifier;
import com.example.financiio.classifier.Model;
import com.example.financiio.entity.Person;
import com.example.financiio.entity.Transaction;
import com.example.financiio.persistence.Persistence;
import com.example.financiio.persistence.DbPersistence;
import com.example.financiio.sms.BankTransactionReportParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class MainActivity extends FragmentActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "MainActivity";

    public static final String BUNDLE_MESSAGE_BODY_KEY = "message_body_key";
    public static final String BUNDLE_TRANSACTION_KEY = "transaction_key";

    ListView lvTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    BankTransactionReportParser parser = new BankTransactionReportParser();

    void analyseSmsClicked() {

        String address = "Bank";
        Log.i(TAG, "clicked for address: " + address);

        Model model = new Model(loadFromAssets("model.json"));
        BayesClassifier classifier = new BayesClassifier(model);
        parser.setClassifier(classifier);

        Persistence persistence = new DbPersistence(this);
        List<Transaction> transactions = persistence.loadTransaction();
        if (transactions.isEmpty()) {

            transactions = getTransactionByAddress(address);
            persistence.storeTransactions(transactions);
        }

        BaseAdapter adapter = new TransactionsListAdapter(this, transactions);

        lvTransactions = (ListView) findViewById(R.id.lvTransactions);

        lvTransactions.setAdapter(adapter);
        lvTransactions.setOnItemClickListener(this);

    }

    private String loadFromAssets(String fileName) {
        String file;
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            file = new String(buffer);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object selected = parent.getAdapter().getItem(position);
        if (selected instanceof Transaction) {
            Transaction t = (Transaction) selected;
            Log.d(TAG, "Selected item: " + t.toString());


            DialogFragment dialog = new TransactionDetailsDialog();
            Bundle dialogBundle = new Bundle();
            dialogBundle.putString(BUNDLE_MESSAGE_BODY_KEY, getMessageBodyById(t.getMessageId()));
            dialogBundle.putParcelable(BUNDLE_TRANSACTION_KEY, t);
            dialog.setArguments(dialogBundle);

            dialog.show(getSupportFragmentManager(), "");
        }
    }

    /*
     * Get sms by sender name
     */
    @SuppressLint("Range")
    private List<Transaction> getTransactionByAddress(String address) {
        List<Transaction> transactions = new ArrayList<>();
        String selection = "address = ?";
        String[] selectionArgs = new String[]{address};
        Uri uri = Uri.parse("content://sms/inbox");

        Cursor cursor = getContentResolver().query(uri, null, selection, selectionArgs, null);
        if (cursor == null) {
            Log.e(TAG, "Cursor is null. Uri: " + uri + "; selection: [" + selection + "] with args: " + Arrays.toString(selectionArgs));
        }

        int i = 0;
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                if (i++ > 50)
                    break;
                @SuppressLint("Range") Transaction t = parser.parse(cursor.getString(cursor.getColumnIndex("body")));
                if (t != null) {
                    t.setMessageId(cursor.getInt(cursor.getColumnIndex("_id")));
                    transactions.add(t);
                }
            }
        }
        return transactions;
    }

    @SuppressLint("Range")
    private String getMessageBodyById(int id) {
        String selection = "_id = ?";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        Uri uri = Uri.parse("content://sms/inbox");

        Cursor cursor = getContentResolver().query(uri, null, selection, selectionArgs, null);
        if (cursor == null) {
            Log.e(TAG, "Cursor is null. Uri: " + uri + "; selection: [" + selection + "] with args: " + Arrays.toString(selectionArgs));
        }

        if (cursor.getCount() != 1) {
            Log.e(TAG, "There are more then one message with id=" + id);
            return null;
        } else {
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex("body"));
        }
    }


    private Person resolveContactInformation(String personId) {
        if (personId == null)
            return null;
        ContentResolver cr = getContentResolver();

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
        String[] selectionArgs = {personId};

        Log.i(TAG, "URI: " + uri.toString() + ", ID: " + personId);
        Cursor cursor = cr.query(uri, null, selection, selectionArgs, null);

        if (cursor == null) {
            Log.e(TAG, "Cursor is null. Uri: " + uri + "; selection: [" + selection + "] with args: " + Arrays.toString(selectionArgs));
        }

        if (cursor.getCount() > 0) {
            if (cursor.getCount() > 1) {
                Log.e(TAG, "There are more the one contact with id " + personId);
                return null;
            }
            cursor.moveToFirst();
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            Person person = new Person();
            person.setName(name);
            person.setContactId(personId);
            person.setPhoneNumber(phone);

            return person;
        }
        return null;
    }
}



