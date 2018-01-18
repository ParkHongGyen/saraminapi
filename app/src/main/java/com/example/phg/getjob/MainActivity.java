package com.example.phg.getjob;

import android.app.TabActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends TabActivity {


    TextView tvresult;//번역창
    EditText ettrans;//입력창
    Button bttranslate;//번역 버튼

    Button sb; //채용정보 검색 버튼튼
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
        tabSpec4.setContent(R.id.content4);
        tabHost.addTab(tabSpec4);

        tabHost.setCurrentTab(0);
        ////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////DB
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

        tvresult = (TextView)findViewById(R.id.tv_result);
        ettrans = (EditText)findViewById(R.id.etsource);
        bttranslate = (Button) findViewById(R.id.bt_translate);



        bttranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ettrans.getText().toString().length()==0){
                    Toast.makeText(MainActivity.this, "내용을 입력하세요." ,Toast.LENGTH_SHORT).show();
                    ettrans.requestFocus();
                    return;
                }
                NaverTranslateTask asyncTask = new NaverTranslateTask();
                String sText = ettrans.getText().toString();
                asyncTask.execute(sText);



            }
        });


    } ////////oncreate

   public class NaverTranslateTask extends AsyncTask<String,Void,String>
   {
       public String resultText;
       String clientId = "qPRWHkJcumXLFGtvzWo2";//애플리케이션 클라이언트 아이디값";
       String clientSecret = "yjQBWLhQ50";//애플리케이션 클라이언트 시크릿값";
       String sourceLang = "en";
       String targetLang = "ko";

       @Override
       protected void onPreExecute() {
           super.onPreExecute();
       }

       @Override
       protected String doInBackground(String... strings) {
           String sourceText = strings[0];
           try {
               String text = URLEncoder.encode(sourceText, "UTF-8");
               String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
               URL url = new URL(apiURL);
               HttpURLConnection con = (HttpURLConnection)url.openConnection();
               con.setRequestMethod("POST");
               con.setRequestProperty("X-Naver-Client-Id", clientId);
               con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
               // post request
               String postParams = "source="+sourceLang+"&target="+targetLang+"&text=" + text;
               con.setDoOutput(true);
               DataOutputStream wr = new DataOutputStream(con.getOutputStream());
               wr.writeBytes(postParams);
               wr.flush();
               wr.close();
               int responseCode = con.getResponseCode();
               BufferedReader br;
               if(responseCode==200) { // 정상 호출
                   br = new BufferedReader(new InputStreamReader(con.getInputStream()));
               } else {  // 에러 발생
                   br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
               }
               String inputLine;
               StringBuffer response = new StringBuffer();
               while ((inputLine = br.readLine()) != null) {
                   response.append(inputLine);
               }
               br.close();
               //System.out.println(response.toString());
               return response.toString();

           } catch (Exception e) {
               //System.out.println(e);
               Log.d("error", e.getMessage());
               return null;
           }
       }
//번역된 결과를 받아서 처리 부분
       @Override
       protected void onPostExecute(String s) {
           super.onPostExecute(s);

           Gson gson = new GsonBuilder().create();
           JsonParser parser = new JsonParser();
           JsonElement rootObj = parser.parse(s.toString())
                   //원하는 데이터 까지 찾아 들어간다.
                   .getAsJsonObject().get("message")
                   .getAsJsonObject().get("result");
           //안드로이드 객체에 담기
           TranslatedItem items = gson.fromJson(rootObj.toString(), TranslatedItem.class);
           //Log.d("result", items.getTranslatedText());
           //번역결과를 텍스트뷰에 넣는다.
           tvresult.setText(items.getTranslatedText());
       }

       private class TranslatedItem{
           String translatedText;

           public String getTranslatedText(){
               return translatedText;
           }
       }
   }



    ///////////////////////////////////////////////////////////////////////페이지 버튼
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
        final RadioGroup rg = (RadioGroup)findViewById(R.id.radiogroup1);
        sb = (Button)findViewById(R.id.searchb);
        int id = rg.getCheckedRadioButtonId();
        RadioButton rb = (RadioButton) findViewById(id);
        String result = rb.getText().toString();

        Intent it = new Intent(getApplicationContext(), SaraminActivity.class);

        it.putExtra("value", " result");
        startActivity(it);
    }

    /////////////////////////////////////////////////////////////////////네이버사전


    /*class  NaverSearchTask  extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings) {

            String clientId = "qPRWHkJcumXLFGtvzWo2";//애플리케이션 클라이언트 아이디값";
            String clientSecret = "yjQBWLhQ50";//애플리케이션 클라이언트 시크릿값";
            String url = "https://openapi.naver.com/v1/papago/n2mt";
            XmlPullParserFactory factory;
            XmlPullParser parser;
            URL xmlUrl = new URL(url);
            String returnResult = "";
            try{
                boolean flag1 = false;

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
    }*/
//////////////////////////////////////////////////




}
