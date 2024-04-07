package com.impetus.newsreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.impetus.newsreader.Models.NewsApiResponse;
import com.impetus.newsreader.Models.NewsHeadlines;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SelectListener, View.OnClickListener{
    RecyclerView recyclerView;
    CustomAdapter adapter;
    ProgressDialog progressDialog;
    SearchView searchView;
    Button b1,b2,b3,b4,b5,b6,b7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.serch_view);
        b1 = findViewById(R.id.btn_1);
        b2 = findViewById(R.id.btn_2);
        b3 = findViewById(R.id.btn_3);
        b4 = findViewById(R.id.btn_4);
        b5 = findViewById(R.id.btn_5);
        b6 = findViewById(R.id.btn_6);
        b7 = findViewById(R.id.btn_7);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressDialog.setTitle("Fetching news articles of " + query);
                progressDialog.dismiss();
                RequestManager manager = new RequestManager(MainActivity.this);
                manager.getNewsHeadlines(listner,"general",query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Fetching new News...");
        progressDialog.show();

        RequestManager manager = new RequestManager(this);
        manager.getNewsHeadlines(listner, "general", null);
    }

    private final OnFetchDataListner<NewsApiResponse> listner = new OnFetchDataListner<NewsApiResponse>() {
        @Override
        public void onFetchData(List<NewsHeadlines> list, String message) {
            if(list.isEmpty()){
                Toast.makeText(MainActivity.this, "No data found of your query !", Toast.LENGTH_SHORT).show();
            }else{
                showNews(list);
                progressDialog.dismiss();
            }
        }

        @Override
        public void onError(String message) {
            // Handle error
            Toast.makeText(MainActivity.this, "Fetching error occured !", Toast.LENGTH_SHORT).show();
        }
    };



    private void showNews(List<NewsHeadlines> list) {
        recyclerView = findViewById(R.id.recycler_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new CustomAdapter(this,list,this); // Corrected constructor call
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void OnNewsClicked(NewsHeadlines headlines) {
        startActivity(new Intent(MainActivity.this, DetailsActivity.class).putExtra("data",headlines));

    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String category = button.getText().toString();
        progressDialog.setTitle("Fetching new articles of " + category);
        progressDialog.show();
        RequestManager manager = new RequestManager(this);
        manager.getNewsHeadlines(listner,category,null);
    }
}
