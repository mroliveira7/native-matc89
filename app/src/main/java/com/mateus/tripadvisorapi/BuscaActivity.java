package com.mateus.tripadvisorapi;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BuscaActivity extends AppCompatActivity implements AsyncResponse, AbsListView.OnScrollListener {
    private ListView listView;
    private LocalizacaoAdapter adapter;
    private ArrayList<Localizacao> itens;
    private Bundle extras;
    private ProgressBar progressBar;

    private static int itemOffset = 0;
    private static final int ITEM_OFFSET_INCREMENT = 20;
    private static int ITEM_OFFSET_MAX = 0;

    private int preLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca);

        progressBar = (ProgressBar) findViewById(R.id.progressBarBusca);
        extras = getIntent().getExtras();

        if (extras != null) {
            processExtras(extras);
        }

        listView = (ListView)findViewById(R.id.listViewBusca);
        itens = new ArrayList<Localizacao>();

        listView.setAdapter(new LocalizacaoAdapter(this, itens));
        listView.setOnScrollListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int pos, long l) {
                Localizacao local = itens.get(pos);
                Intent intent = new Intent(BuscaActivity.this, DetalhesActivity.class);
                intent.putExtra("LOCAL", local);

                startActivity(intent);

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void processExtras (Bundle extras) {
        String cityName = extras.getString(MainActivity.CITY_NAME);
        String latitude = extras.getString(MainActivity.CITY_LAT);
        String longitude = extras.getString(MainActivity.CITY_LON);

        progressBar.setVisibility(View.VISIBLE);

        if (cityName != null) {
            setTitle(cityName);

            if (cityName.indexOf("-") != -1) {
                cityName = cityName.substring(0, cityName.indexOf("-"));
            }

            if (cityName.indexOf(",") != -1) {
                cityName = cityName.substring(0, cityName.indexOf(","));
            }

            startSearch(cityName);
        } else {
            latitude = latitude.replaceAll(",", ".");
            longitude = longitude.replaceAll(",", ".");

            startSearch(latitude, longitude);
        }
    }

    private void startSearch (String... strings) {
        String offset = String.format("%d", itemOffset);

        if (ITEM_OFFSET_MAX != 0 && (itemOffset + ITEM_OFFSET_INCREMENT) > ITEM_OFFSET_MAX) {
            return;
        }

        if (strings.length == 1) {
            ExecuteSearch executeSearch = new ExecuteSearch();
            executeSearch.delegate = this;
            executeSearch.execute(strings[0], offset);
        } else {
            ExecuteSearch executeSearch = new ExecuteSearch();
            executeSearch.delegate = this;
            executeSearch.execute(strings[0], strings[1], offset);
        }

        itemOffset = itemOffset + ITEM_OFFSET_INCREMENT;
    }

    @Override
    public void processFinish(JSONObject response) {
        String id, title, address, price, phone, img_url, url;
        float rating;

        try {
            ITEM_OFFSET_MAX = response.getInt("total");

            JSONArray jsonArray = response.getJSONArray("businesses");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                id = obj.getString("id");
                title = obj.getString("name");

                JSONObject a = obj.getJSONObject("location");
                JSONArray addressArray = a.getJSONArray("display_address");
                address = addressArray.get(0).toString();

                rating =    obj.has("rating") ? BigDecimal.valueOf(obj.getDouble("rating")).floatValue() : 0;
                price =     obj.has("price") ? obj.getString("price") : " ";
                phone =     obj.has("display_phone") ? obj.getString("display_phone") : "";
                img_url =   obj.has("image_url") ? obj.getString("image_url") : null;
                url = obj.getString("url");

                itens.add(new Localizacao(id, title, address, rating, price, phone, img_url, url));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {}

    @Override
    public void onScroll(AbsListView absListView, int first, int visibleCount, int total) {
        switch (absListView.getId()) {
            case R.id.listViewBusca:
                final int lastItem = first + visibleCount;

                if (lastItem == total) {
                    if (preLast != lastItem) {
                        preLast = lastItem;
                        // Faz nova chamada ao chegar no final da lista
                        processExtras(extras);
                    }
                }
        }
    }

    private class ExecuteSearch extends AsyncTask <String, Void, String> {

        private String baseUrl = "https://api.yelp.com/v3/businesses/search?";
        private String locationParam = "&location=";
        private String offset = "&offset=";
        private String latParam = "&latitude=";
        private String lonParam = "&longitude=";

        public AsyncResponse delegate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url;

            if (strings.length == 2) {
                url = baseUrl.concat(locationParam).concat(strings[0]);
            } else {
                url = baseUrl.concat(latParam).concat(strings[0]).concat(lonParam).concat(strings[1]);
            }

            url = url.concat(offset).concat(strings[strings.length-1]);

            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .header("Authorization", "Bearer " + getResources().getString(R.string.API_KEY))
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();

                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                delegate.processFinish(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
