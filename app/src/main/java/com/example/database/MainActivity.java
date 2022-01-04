package com.example.database;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    EditText editText;
    EditText editText2;
    TextView textView;

    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String databaseName = editText.getText().toString();
                createDatabase(databaseName);
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tableName = editText2.getText().toString();
                createTable(tableName);

            }
        });

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertRecord();
            }
        });

        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeQuery();
            }
        });

    }

    public void createDatabase(String databaseName) {
        println("createDatabase 호출됨");

        try {
            database = openOrCreateDatabase(databaseName, MODE_PRIVATE, null);
            println("데이터베이스 생성됨 : " + databaseName);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void createTable(String tableName) {
        println("createTable 호출됨");
        

        try {
            if (database == null) {
                println("데이터베이스를 먼저 열어주세요");
                return;
            }

            String sql = "create table if not exists tableName (_id integer PRIMARY KEY, name text, age integer, mobile text)";
            database.execSQL(sql);
            println("테이블 생성됨 :" + tableName);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
/*create table 생성 명령어 in not exist 조건문, ()안에는 칼럼생성+타입, _id SQL DB에서 구분자로 사용(커스텀가능), 타입+키+자동부여명령, 칼럼명 타입, 칼럼명 타입,~~)"*/
    //깃 브랜치 테스트
    public void insertRecord() {
        println("insertRecord 호출됨");

        if (database == null) {
            println("데이터베이스를 먼저 열어주세요");
            return;
        }

        String tableName = editText2.getText().toString();

        if (tableName == null) {
            println("테이블 이름을 입력하세요.");
            return;
        }

        String sql = "insert into " + tableName + " values ('john', 20, '010-1000-1000')";
        database.execSQL(sql);
        println("레코드 추가함");

    }

    //executeQuery DB에 데이터 입력 등
    public void executeQuery() {
        println("executeQuery 호출됨");

        try {
            String tableName = editText2.getText().toString();
            if (tableName == null) {
                println("테이블 이름을 입력하세요");
                return;
            }
            String sql = "select _id, name, age, mobile from " + tableName;
            Cursor cursor = database.rawQuery(sql, null); //rawQuery DB 데이터 조회
            //cursor DB상의 레코드를 하나씩 넘기면서 접근
            int recordCount = cursor.getCount();
            println("레코드 갯수 :" + recordCount);

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();

                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int age = cursor.getInt(2);
                String mobile = cursor.getString(3);

                println("레코드 #" + i + " : " + id + "," + name + "," + age + "," + mobile);

            }

            cursor.close(); //커서도 항상 클로즈 해줘야함(한정된자원)
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void println(String data) {
        textView.append(data+"\n");
    }
}