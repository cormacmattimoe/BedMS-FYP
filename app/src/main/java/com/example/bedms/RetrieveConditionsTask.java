package com.example.bedms;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class RetrieveConditionsTask extends AsyncTask<String, Integer, String> {



    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

    @Override
    protected String doInBackground(String... params) {

        StringBuffer stringBuffer = new StringBuffer("");
        BufferedReader bufferedReader = null;
        try {
            // creating the Httpclient
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(params[0]);
            System.out.println("LINE 37");

            httpGet.addHeader("Content-Type", "application/json");
            httpGet.addHeader("Accept", "application/json");
            httpGet.addHeader("app_id", "bce7546b");
            httpGet.addHeader("app_key", "f7fe8d32be8ae8f40a8ac05242d1d01d");
            HttpResponse httpResponse = httpClient.execute(httpGet);
            InputStream inputStream = httpResponse.getEntity().getContent();
            bufferedReader = new BufferedReader(new InputStreamReader(
                    inputStream));

            String readLine = bufferedReader.readLine();
            if (readLine != null) {
                Matcher m = Pattern.compile("(?<=\"name\":\\\")(.*?)(?=\\\")")
                        .matcher(readLine);
                while (m.find()) {
                    ConditionCache.conditionCache.add(m.group());
                }

            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                    String s = stringBuffer.toString();
                    return s;
                } catch (IOException e) {
                    // TODO: handle exception
                }
            }
        }
        return "out of everything";
    }
}