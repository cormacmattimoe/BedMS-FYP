package com.example.bedms;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;




    public class MyTask extends AsyncTask<String, Integer, String> {

        Context c;
        TextView t;
        ArrayList<Condition> conditions = new ArrayList<Condition>();
        ArrayList<StringBuffer> stringConditions = new ArrayList<StringBuffer>();
        ArrayList<String> readlineCons = new ArrayList<String>();

        public MyTask(Context c, TextView t) {
            this.c = c;
            this.t = t;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            t.setText(values[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            t.setText("setting up to fetch data...");

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            t.setText(result);
        }

        @Override
        protected String doInBackground(String... params) {

            StringBuffer stringBuffer = new StringBuffer("");
            BufferedReader bufferedReader = null;
            try {
                // creating the Httpclient
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("https://api.infermedica.com/v2/conditions/c_926");

                // adding the headers to the HttpPost
                httpGet.addHeader("Content-Type", "application/json");
                httpGet.addHeader("Accept", "application/json");
                httpGet.addHeader("app_id", "bce7546b");
                httpGet.addHeader("app_key", "f7fe8d32be8ae8f40a8ac05242d1d01d");

                //URLConnection httpURLConnection = new HttpURLConnection();


//          data.setText("execute call next...");

                HttpResponse httpResponse = httpClient.execute(httpGet);
                InputStream inputStream = httpResponse.getEntity().getContent();
                bufferedReader = new BufferedReader(new InputStreamReader(
                        inputStream));

                String readLine = bufferedReader.readLine();
                while (readLine != null) {
                    System.out.println("This is readline " + readLine.toString());
                    stringBuffer.append(readLine);
                    stringBuffer.append("\n");
                    readLine = bufferedReader.readLine();
                  //  System.out.println("This is readline index " +    readLine.indexOf(4));
                    stringConditions.add(stringBuffer);
                    stringConditions.get(0);
                    readlineCons.add(readLine);

/*

               // initialized as you can
                    JsonParser parser = new JsonParser();
                    JSONObject json = (JSONObject) parser.parse(readlineCons);


                    Gson g = new Gson();
                    readLine = g.toJson(conditions);

                    HashMap<Condition, StringBuffer> conditionsHash = new HashMap<Condition, StringBuffer>();
                   // conditionsHash.put()//
                    for (int i = 0; i < conditions.size(); i++) {
                        JSONObject jsonObject = re.getJSONObject(i);

                        Condition coony = new Condition();
                        coony.setName(jsonObject.optString("name"));
                        conditions.add(coony);

                    }

                    System.out.println("This is con index " +   stringBuffer.charAt(1));
                    Condition con = new Condition();
                    con.getName();
                    Iterator<Condition> it=conditions.iterator();
                    while(it.hasNext()){

                        Condition obj = it.next();

                        if(obj.getName().equals("Name"))
                        {
                            String name =obj.getName();

                        //    stringConditions.add(name);

                            //do whatever u wanted to do with it now
                        }
                    }

 */

                }
            } catch (Exception e) {
                // TODO: handle exception
                String val = e.toString();
              //  data.setText(val);
                return val;
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                        String s = stringBuffer.toString();
                        //data.setText(s)
                        ;
                        return s;
                    } catch (IOException e) {
                        // TODO: handle exception
                    }
                }
            }
            return "out of everything";
        }
    }
