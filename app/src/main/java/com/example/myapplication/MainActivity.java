package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    Fragment mainFragment;
    EditText inputToDo;
    Context context;
    // NoteDataBase 를 초기화
    public static NoteDataBase noteDataBase = null;

    public void openDatabase() {

        if (noteDataBase != null) {
            noteDataBase.close();
            noteDataBase = null;
        }

        noteDataBase = NoteDataBase.getInstance(this);
        boolean isOpen = noteDataBase.open();
        if (isOpen) {
            Log.d(TAG, "Note database is open.");
        } else {
            Log.d(TAG, "Note database is not open.");
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (noteDataBase != null) {
            noteDataBase.close();
            noteDataBase = null;
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 콜백함수
        super.onCreate(savedInstanceState);
        // 화면을 연결함
        setContentView(R.layout.activity_main);
        setTitle("지금 해야 할 일");




        mainFragment = new MainFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (saveToDo()) {
                    Toast.makeText(getApplicationContext(), "할 일이 저장되었습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });

        openDatabase();

    }

    private boolean saveToDo() {
        inputToDo = findViewById(R.id.inputToDo);
        String todo = inputToDo.getText().toString();

        if  (todo.isEmpty()) {
            Toast.makeText(getApplicationContext(), "할 일을 입력해주세요.", Toast.LENGTH_LONG).show();
            return false;
        }

        String sqlSave = "insert into " + NoteDataBase.TABLE_NOTE + " values(null, '" + todo + "');";

        NoteDataBase database = NoteDataBase.getInstance(context);
        database.execSQL(sqlSave);

        inputToDo.setText("");

        return true;
    }
}