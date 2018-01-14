package com.example.phg.getjob;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MainActivity extends TabActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//////////////////////////////////////////////////////////////////////tab host
        TabHost tabHost = getTabHost();//(1)

        TabSpec tabSpec1 = tabHost.newTabSpec("Tab1").setIndicator("채용정보"); //이름이랑 생성
        tabSpec1.setContent(R.id.content1);//탭눌럿을때 뜨는거
        tabHost.addTab(tabSpec1);//탭추가

        TabSpec tabSpec2 = tabHost.newTabSpec("Tab2").setIndicator("스케줄");
        tabSpec2.setContent(R.id.content2);
        tabHost.addTab(tabSpec2);

        TabSpec tabSpec3 = tabHost.newTabSpec("Tab3").setIndicator("자격증");
        tabSpec3.setContent(R.id.content3);
        tabHost.addTab(tabSpec3);

        TabSpec tabSpec4 = tabHost.newTabSpec("Tab4").setIndicator("영어사전");
        tabSpec4.setContent(R.id.content3);
        tabHost.addTab(tabSpec4);

        tabHost.setCurrentTab(0);
        ////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////db
        final dbHelper dbHelp = new dbHelper(getApplicationContext(), "LIKE.db", null, 1);//db조건

        final EditText etlike = (EditText)findViewById(R.id.etLike);//참조
        final EditText etmemo = (EditText)findViewById(R.id.etMemo);//횟수
        final TextView txresult = (TextView)findViewById(R.id.txResult);//결과창

        Button btnInsert = (Button) findViewById(R.id.btadd);//버튼 인식
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//추가 테이블명 이름, 숫자
                String name = etlike.getText().toString();//스트링값 받음
                String price = etmemo.getText().toString();//edittext에서 받는다
                dbHelp.insert("insert into LIKE_LIST values(null, '" + name + "', " + price + ");");//Db에 insert한다
                txresult.setText( dbHelp.PrintData() );//결과창
            }
        });
        Button btnDelete = (Button) findViewById(R.id.btdel);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etlike.getText().toString();//스트링값 받음
                dbHelp.delete("delete from LIKE_LIST where name = '" + name + "';");//삭제

                txresult.setText( dbHelp.PrintData() );//결과창에띄운다
            }
        });
////////////////////////////////////////////////////////////////////////////////////////



    }


//////////////////////////////////////////////////saramin액티비티
    public void saramingo(View v){
        Intent it = new Intent(getApplicationContext(), SaraminActivity.class);
        startActivity(it);
    }
//////////////////////////////////////////////////

}
