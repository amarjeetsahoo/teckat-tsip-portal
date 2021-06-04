package com.amarjeet.android.teckatproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FetchAnActivity extends AsyncTask<String, Void, String> {
    RecyclerView recyclerView;
    ArticleAdapter adaptor;
    ArrayList<Article> articles;
    public String ar="";
    public FetchAnActivity(RecyclerView recyclerView, ArticleAdapter adaptor, ArrayList<Article> articles) {
        this.recyclerView = recyclerView;
        this.adaptor = adaptor;
        this.articles = articles;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
    @Override
    protected String doInBackground(String... params) {
        try {
            ar=params[0];
            String link_url;
            link_url = "https://api.razorpay.com/v1/payments";
            URL url = new URL(link_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", "Basic cnpwX3Rlc3RfV3hSWmYxYlJrV0hEcm46N2F1bnVtbGY2WUdXcTJWY1NaamdEVktI");
            InputStream inputStream = connection.getInputStream();
            if (inputStream == null)
                return "Data is not fetched";
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        System.out.println(ar);
        String fname;
        String lname;
        String fullName;
        String mobile;
        String status;
        String course;
        int amount=0,finalAmt=0;
        String activ;
        String confirm;
        String referral;
        String money;
        try {
            JSONObject obj = new JSONObject(s);
            activ = obj.getString("items");
            JSONArray items = new JSONArray(activ);
            for (int i = 0; i < items.length(); i++) {
                JSONObject obj1 = items.getJSONObject(i);
                confirm = obj1.getString("notes");
                JSONObject obj2 = new JSONObject(confirm);
                fname = obj2.getString("first_name");//got firstname
                lname = obj2.getString("last_name");//got lastname
                fullName= ""+fname+" "+lname;           //got fullname
                mobile=obj2.getString("phone"); //got mobile number
                course=obj2.getString("workshop_course");//course name
                amount=obj1.getInt("amount");//got amount paid
                finalAmt=amount/100;
                money=""+finalAmt;
                referral = obj2.optString("referral").toUpperCase();// got referal
                if (referral.equals(ar)) {
                    Article article = new Article();
                    article.setPayName(fullName);
                    article.setPayMobile(mobile);
                    article.setPayAmount(money);
                    if(obj1.getString("status").equals("captured")) {
                        status = "Successful";
                        article.setPayStatus(status);
                    }
                    else if (obj1.getString("status").equals("failed")) {
                        status = "Initiated";
                        article.setPayStatus(status);//got capture status
                    }
                    article.setPayCourse(course);
                    articles.add(article);
                } else continue;
            }
        } catch (JSONException e) {
            Log.d("JSONThread", "addItemsFromJSON: ", e);
        }
        adaptor.setData(articles);
        adaptor.notifyDataSetChanged();
    }
}