package com.metacoders.popularnewsapprestful;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.metacoders.popularnewsapprestful.api.ApiClient;
import com.metacoders.popularnewsapprestful.api.ApiInterface;
import com.metacoders.popularnewsapprestful.models.Article;
import com.metacoders.popularnewsapprestful.models.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.metacoders.popularnewsapprestful.Utils.API_KEY;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private Adapter adapter;
    private String TAG = MainActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        loadJson();

    }

    public void loadJson(){

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        String country = Utils.getCountry();

        Call<News> call;
        call = apiInterface.getNews(country, API_KEY);

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body().getArticle() != null) {

                    if (!articles.isEmpty()) {
                        articles.clear();
                    }

                    articles = response.body().getArticle();
                    adapter = new Adapter(articles, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(MainActivity.this, "No articles found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {

            }
        });

    }
}
