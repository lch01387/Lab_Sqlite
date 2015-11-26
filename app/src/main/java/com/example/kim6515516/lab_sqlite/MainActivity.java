package com.example.kim6515516.lab_sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Database 관련 객체들
    SQLiteDatabase db;
    String dbName = "idList.db"; // name of Database;
    String tableName = "idListTable"; // name of Table;
    int dbMode = Context.MODE_PRIVATE;


    // layout object
    EditText mEtName;
    EditText mEtName2;
    EditText mEtName3;
    EditText mEtName4;
    Button mBtInsert;
    Button mBtRead;
    Button mBtDelete;
    Button mBtRevise;


    ListView mList;
    ArrayAdapter<String> musicAdapter;
    ArrayList<String> nameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create databases
        db = openOrCreateDatabase(dbName,dbMode,null);
        // create table;
        createTable();

        mEtName = (EditText) findViewById(R.id.et_text);
        mEtName2 = (EditText) findViewById(R.id.et_text2);
        mEtName3 = (EditText) findViewById(R.id.et_text3);
        mEtName4 = (EditText) findViewById(R.id.et_text4);
        mBtInsert = (Button) findViewById(R.id.bt_insert);
        mBtRead = (Button) findViewById(R.id.bt_read);
        mBtRevise = (Button) findViewById(R.id.bt_revise);
        ListView mList = (ListView) findViewById(R.id.list_view);


        mBtInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEtName.getText().toString();
                insertData(name);
            }
        });

        mBtRead = (Button) findViewById(R.id.bt_read);
        mBtRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameList.clear();
                selectAll();
                musicAdapter.notifyDataSetChanged();
            }
        });

        mBtDelete = (Button) findViewById(R.id.bt_delete);
        mBtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = Integer.parseInt(mEtName2.getText().toString());
                removeData(index);
            }
        });

        mBtRevise.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String text = mEtName3.getText().toString();
                int index = Integer.parseInt(mEtName4.getText().toString());
                updateData(index, text);
            }
        });

        // Create listview
        nameList = new ArrayList<String>();
        musicAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, nameList);
        mList.setAdapter(musicAdapter);

    }


//    // Database 생성 및 열기
//    public void createDatabase(String dbName, int dbMode) {
//        db = openOrCreateDatabase(dbName, dbMode, null);
//    }

    // Table 생성
    public void createTable() {
        try {
            String sql = "create table " + tableName + "(id integer primary key autoincrement, " + "name text not null)";
            db.execSQL(sql);
        } catch (android.database.sqlite.SQLiteException e) {
            Log.d("Lab sqlite","error: "+ e);
        }
    }

    // Table 삭제
    public void removeTable() {
        String sql = "drop table " + tableName;
        db.execSQL(sql);
    }

    // Data 추가
    public void insertData(String name) {
        String sql = "insert into " + tableName + " values(NULL, '" + name + "');";
        db.execSQL(sql);
    }

    // Data 업데이트
    public void updateData(int index, String name) {
        String sql = "update " + tableName + " set name = '" + name + "' where id = " + index + ";";
        db.execSQL(sql);
    }

    // Data 삭제
    public void removeData(int index) {
        String sql = "delete from " + tableName + " where id = " + index + ";";
        db.execSQL(sql);
    }

    // Data 읽기(꺼내오기)
    public void selectData(int index) {
        String sql = "select * from " + tableName + " where id = " + index + ";";
        Cursor result = db.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if (result.moveToFirst()) {
            int id = result.getInt(0);
            String name = result.getString(1);
//            Toast.makeText(this, "index= " + id + " name=" + name, Toast.LENGTH_LONG).show();

            Log.d("lab_sqlite", "\"index= \" + id + \" name=\" + name ");
        }
        result.close();
    }


    // 모든 Data 읽기
    public void selectAll() {
        String sql = "select * from " + tableName + " order by id desc;";
        Cursor results = db.rawQuery(sql, null);

        results.moveToFirst();


        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            String name = results.getString(1);
//            Toast.makeText(this, "index= " + id + " name=" + name, Toast.LENGTH_LONG).show();
            Log.d("lab_sqlite", "index= " + id + " name=" + name);

            nameList.add(name + "(id : " + id + ")");

            results.moveToNext();
        }
        results.close();
    }

}


