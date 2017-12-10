package com.example.emergencyphonenumber;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.emergencyphonenumber.adapter.PhoneListAdapter;
import com.example.emergencyphonenumber.db.PhoneDbHelper;
import com.example.emergencyphonenumber.model.PhoneItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private PhoneDbHelper mHelper;
    private SQLiteDatabase mDb;

    private ArrayList<PhoneItem> mPhoneItemList = new ArrayList<>();
    private PhoneListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mHelper = new PhoneDbHelper(this);
        mDb = mHelper.getReadableDatabase();

        loadDataFromDb();

        TextView summerry = findViewById(R.id.sum_top);
        int sum = mPhoneItemList.
        summerry.setText(sum);
        mAdapter = new PhoneListAdapter(
                this,
                R.layout.item,
                mPhoneItemList
        );

        ListView lv = findViewById(R.id.list_view);
        lv.setAdapter(mAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //PhoneItem item = mPhoneItemList.get(position);
                //String phoneNumber = item.number;

                //Intent intent = new Intent(Intent.ACTION_DIAL);
                //intent.setData(Uri.parse("tel:" + phoneNumber));
                //startActivity(intent);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setMessage("ยืนยันลบรายการ");
                String[] items = new String[]{"NO","YES"};

                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) { // ไม่ลบ


                        } else if (i == 1) { // ลบข้อมูล
                            PhoneItem item = mPhoneItemList.get(position);
                            int phoneId = item.id;

                            mDb.delete(
                                    PhoneDbHelper.TABLE_NAME,
                                    PhoneDbHelper.COL_ID + "=?",
                                    new String[]{String.valueOf(phoneId)}
                            );
                            loadDataFromDb();
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });
        Button incomeB = findViewById(R.id.income_button);
        incomeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddPhoneActivity.class);
                startActivityForResult(intent, 123);
            }
        });

        Button outcomeB = findViewById(R.id.outcom_button);
        incomeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddPhoneActivity2.class);
                startActivityForResult(intent, 123);
            }
        });

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPhoneActivity.class);
                startActivityForResult(intent, 123);
            }
        });*/

    } // ปิดเมธอด onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123) {
            if (resultCode == RESULT_OK) {
                loadDataFromDb();
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private void loadDataFromDb() {
        Cursor cursor = mDb.query(
                PhoneDbHelper.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        mPhoneItemList.clear();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(PhoneDbHelper.COL_ID));
            String title = cursor.getString(cursor.getColumnIndex(PhoneDbHelper.COL_TITLE));
            String numberminus = cursor.getString(cursor.getColumnIndex(PhoneDbHelper.COL_NUMBERMINUS));
            String numberplus = cursor.getString(cursor.getColumnIndex(PhoneDbHelper.COL_NUMBERPLUS));
            //String picture = cursor.getString(cursor.getColumnIndex(PhoneDbHelper.COL_PICTURE));

            PhoneItem item = new PhoneItem(id, title, numberminus, numberplus);
            mPhoneItemList.add(item);
        }
    }
} // ปิดคลาส MainActivity
