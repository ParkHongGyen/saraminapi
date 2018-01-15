package com.example.phg.getjob;

import android.app.TabActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;

public class MainActivity extends TabActivity {

    Button b1;
    EditText et1;
    TextView tv1;
    NaverSearchTask task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button) findViewById(R.id.button1);
        et1 = (EditText) findViewById(R.id.editText1);
        tv1 = (TextView) findViewById(R.id.textView1);



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
        tabSpec4.setContent(R.id.content4);
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
///////////////////////////////////////////////////////////////////////////////네이버 사전




    }
///////////////////////////////////////////////////////////////////////
    public void pnggo1(View v) //정보처리기사기출 이벤트
    {
        Uri uri = Uri.parse("http://www.gunsys.com/cbt_list/index.php?cbt=gisa");//인터넷주소
        Intent it  = new Intent(Intent.ACTION_VIEW,uri);//인텐트 설정
        startActivity(it);//실행
    }
    public void pnggo2(View v) //정보처리기사기출 이벤트
    {
        Uri uri = Uri.parse("http://www.q-net.or.kr/crf005.do?id=crf00503&jmCd=1320");//인터넷주소
        Intent it  = new Intent(Intent.ACTION_VIEW,uri);//인텐트 설정
        startActivity(it);//실행
    }

//////////////////////////////////////////////////saramin액티비티
    public void saramingo(View v){
        Intent it = new Intent(getApplicationContext(), SaraminActivity.class);
        startActivity(it);
    }

    /////////////////////////////////////////////////////////////////////네이버사전


    class  NaverSearchTask  extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings) {
            String clientId = "qPRWHkJcumXLFGtvzWo2";//애플리케이션 클라이언트 아이디값";
            String clientSecret = "yjQBWLhQ50";//애플리케이션 클라이언트 시크릿값";
            String url = "https://openapi.naver.com/v1/papago/n2mt";
            XmlPullParserFactory factory;
            XmlPullParser parser;
            URL xmlUrl;
            String returnResult = "";
            try{
                boolean flag1 = false;
                xmlUrl = new URL(url);
                xmlUrl.openConnection().getInputStream();
                factory = XmlPullParserFactory.newInstance();
                parser = factory.newPullParser();
                parser.setInput(xmlUrl.openStream(), "utf-8");
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("200")) {
                                flag1 = true;
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                        case XmlPullParser.TEXT:
                            if (flag1 == true) {
                                returnResult += parser.getText()+"\n";
                                flag1 = false;
                            }
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {

            }

            return returnResult;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public void papago(View view) {
        task = new NaverSearchTask();
        task.execute(et1.getText().toString());
        et1.setText("");
    }
//////////////////////////////////////////////////


}
