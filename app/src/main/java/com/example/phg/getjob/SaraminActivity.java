package com.example.phg.getjob;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
/**
 * Created by PHG on 2018-01-13.
 */

public class SaraminActivity extends Activity implements OnClickListener {

    Button btsaram;
    EditText etsaram;
    TextView tvsaram;
    DownloadWebpageTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saramin);

       btsaram = (Button) findViewById(R.id.bt_saramin);
        etsaram = (EditText) findViewById(R.id.et_saramin);
       tvsaram = (TextView) findViewById(R.id.tv_saramin);

       btsaram.setOnClickListener(this);
      /*  String serviceURL = "http://api.saramin.co.kr/job-search?";*/
       /* new DownloadWebpageTask().execute(serviceURL);*/

        Intent it = getIntent();
        String data = it.getStringExtra("value");
        etsaram.setText(data);
    }



    class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {


            String strText = etsaram.getText().toString() ;
            String url = "http://api.saramin.co.kr/job-search?keywords="+strText; //사람인 제공 api의 url


            XmlPullParserFactory factory;
            XmlPullParser parser;
            URL xmlUrl;
            String returnResult = "";
            try {
                boolean flag1 = false;
                boolean flag2 = false;
                boolean flag3 = false;
                xmlUrl = new URL(url);
                xmlUrl.openConnection().getInputStream();
                factory = XmlPullParserFactory.newInstance();
                parser = factory.newPullParser();
                parser.setInput(xmlUrl.openStream(), "utf-8");
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {//마지막일때까지 반복
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT://문서처음
                            break;
                        case XmlPullParser.END_DOCUMENT://문서끝
                            break;
                        case XmlPullParser.START_TAG://TAG 시작일때
                            if (parser.getName().equals("title")) {//대괄호 title이면 flag1 true
                                flag1 = true;
                            }
                            /*if (parser.getName().equals("position")){ //대괄호 industry이면 flag2 true
                                flag2 = true;
                            }*/
                            if (parser.getName().equals("keyword")){ //대괄호 industry이면 flag3 true
                                flag3 = true;
                            }
                            break;

                        case XmlPullParser.END_TAG:
                            break;
                        case XmlPullParser.TEXT:
                            if (flag1 == true) {
                                returnResult += parser.getText()+"\n";
                                flag1 = false;
                            }
                          /*  if (flag2 == true){
                                returnResult += parser.getText()+"\n";
                                flag2 = false;
                            }*/
                            if (flag3 == true){
                            returnResult += parser.getText()+"\n"+"\n";
                            flag3 = false;
                        }
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {

            }

            return returnResult;




        /*    try {
                return (String) downloadUrl((String) strings[0]);
            } catch (IOException e){
                return "다운로드 실패";
            }*/
        }

        @Override
        protected void onPostExecute(String result) {

           tvsaram.setText(result);




            /*try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(result));
                int eventType = xpp.getEventType();

                String keywords = "";
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if(eventType == XmlPullParser.START_DOCUMENT){
                        ;
                    }
                    else if(eventType == XmlPullParser.START_TAG) {
                        String tag_name = xpp.getName();
                        if(ta)
                    }
                }

            } catch (Exception e) {
                tv.setText(e.getMessage());
            }*/

        }


      /*  private String downloadUrl(String myurl) throws IOException {

            HttpURLConnection conn = null;
            try{
                URL url = new URL(myurl);
                conn = (HttpURLConnection)url.openConnection();
                BufferedInputStream buf = new BufferedInputStream( conn.getInputStream());
                BufferedReader bufreader = new BufferedReader(new InputStreamReader(buf, "utf-8"));
                String line = null;
                String page = "";
                while((line = bufreader.readLine()) != null) {
                    page += line;

                }
                return page;
            }
            finally {
                conn.disconnect();
            }
        }*/


    }
    @Override
    public void onClick(View view) {
        task = new DownloadWebpageTask();
        task.execute(etsaram.getText().toString());
        etsaram.setText("");
    }

}
