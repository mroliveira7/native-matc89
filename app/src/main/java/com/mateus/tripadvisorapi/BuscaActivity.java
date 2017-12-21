package com.mateus.tripadvisorapi;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BuscaActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> listAdapter;
    private LocalizacaoAdapter adapter;
    private ArrayList<Localizacao> itens;

    private List<Localizacao> grupos = new ArrayList<Localizacao>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            processExtras(extras);
        }

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

        if (cityName != null) {
            setTitle(cityName);

            if (cityName.indexOf("-") != -1) {
                cityName = cityName.substring(0, cityName.indexOf("-"));
            }

            if (cityName.indexOf(",") != -1) {
                cityName = cityName.substring(0, cityName.indexOf(","));
            }

            ExecuteSearch executeSearch = new ExecuteSearch();
            executeSearch.execute(cityName);
        } else {
            latitude = latitude.replaceAll(",", ".");
            longitude = longitude.replaceAll(",", ".");

            ExecuteSearch executeSearch = new ExecuteSearch();
            executeSearch.execute(latitude, longitude);
        }
    }

    private class ExecuteSearch extends AsyncTask <String, Void, String> {

        private String baseUrl = "https://api.yelp.com/v3/businesses/search?";
        private String locationParam = "&location=";
        private String latParam = "&latitude=";
        private String lonParam = "&longitude=";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url;

            if (strings.length == 1) {
                url = baseUrl.concat(locationParam).concat(strings[0]);
            } else {
                url = baseUrl.concat(latParam).concat(strings[0]).concat(lonParam).concat(strings[1]);
            }

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

            final ArrayList<Localizacao> itens = new ArrayList<>();

            if(result != null) {
                String id, title, address, price, phone, img_url, url;
                float rating;

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("businesses");

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
                }
            }

            listView = (ListView)findViewById(R.id.listViewBusca);

            adapter = new LocalizacaoAdapter(getApplicationContext(), 0, itens);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int pos, long l) {
                    Localizacao local = itens.get(pos);
                    Intent intent = new Intent(BuscaActivity.this, DetalhesActivity.class);
                    intent.putExtra("LOCAL", local);

                    startActivity(intent);

                }
            });
        }
    }
}
