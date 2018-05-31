package com.example.jjong.lab6_3;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editText1, editText2;
    ListView listView;
    Button btnAdd,btnDel;
    SQLiteDatabase db;
    DBhelper helper;
    String studentName,studentId;
    String[]names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new DBhelper(getApplicationContext(), "student.db", null, 3);

        editText1=findViewById(R.id.name);
        editText2=findViewById(R.id.id);
        listView=findViewById(R.id.listView);
        btnAdd=findViewById(R.id.buttonAdd);
        btnDel=findViewById(R.id.buttonDelete);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText1.getText().toString().isEmpty()||editText2.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Fill all of blank",Toast.LENGTH_LONG).show();
                }else{
                    //add
                    studentName=editText1.getText().toString();
                    studentId=editText2.getText().toString();
                    insert(studentName,studentId);
                    invalidate();
                }
            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText1.getText().toString().isEmpty()&&editText2.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Fill blank",Toast.LENGTH_LONG).show();
                }else if(!editText1.getText().toString().isEmpty()&&editText2.getText().toString().isEmpty()){
                    studentName=editText1.getText().toString();
                    deleteName(studentName);
                    Toast.makeText(getApplicationContext(),"이름으로 삭제",Toast.LENGTH_LONG).show();
                    invalidate();
                }
                else if(editText1.getText().toString().isEmpty()&&!editText2.getText().toString().isEmpty()){
                    studentId=editText2.getText().toString();
                    deleteId(studentId);
                    Toast.makeText(getApplicationContext(),"id로 삭제",Toast.LENGTH_LONG).show();
                    invalidate();
                }
                else{
                    studentName=editText1.getText().toString();
                    deleteName(studentName);
                    Toast.makeText(getApplicationContext(),"이름으로 삭제",Toast.LENGTH_LONG).show();
                    invalidate();
                }
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();

    }

    public void insert(String name, String id){

        db=helper.getWritableDatabase();

        ContentValues values=new ContentValues();

        values.put("studentName",name);
        values.put("studentId",id);
        db.insert("student",null,values);
        Log.i("db1", name + "정상적으로 삽입 되었습니다."+id);
    }

    public void deleteName (String name) {
        db = helper.getWritableDatabase();

        db.delete("student", "studentName=?", new String[]{name});
        Log.i("db1", name + "정상적으로 삭제 되었습니다.");
    }

    public void deleteId (String id) {
        db = helper.getWritableDatabase();

        db.delete("student", "studentId=?", new String[]{id});
        Log.i("db1", id + "정상적으로 삭제 되었습니다.");
    }
    public void select() {

        db = helper.getReadableDatabase();
        Cursor c = db.query("student", null, null, null, null, null, null);

        names=new String[c.getCount()];
        int count=0;

        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex("studentName"));
            String id = c.getString(c.getColumnIndex("studentId"));

            names[count] =name+" "+id;

            Log.i("db1", "name : " + name + ", id : " + id);
            count++;
        }
        c.close();
    }

    private void  invalidate(){
        select();
        ArrayAdapter <String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,names);

        listView.setAdapter(adapter);
    }

}
