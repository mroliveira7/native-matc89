package com.mateus.tripadvisorapi;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
            String cityName = extras.getString(MainActivity.CITY_NAME);
            setTitle(cityName);
            ExecuteSearch executeSearch = new ExecuteSearch();
            executeSearch.execute(cityName);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //@Override
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int i, long l) {
                //String nomeItem = (String)listView.getItemAtPosition(i);
                 Intent intent = new Intent(BuscaActivity.this, DetalhesActivity.class);
                 // Passar dados do local para renderizar na prox pagina
                 startActivity(intent);
                //String nomeItem = (String)listView.getItemAtPosition(i);
                //  Toast.makeText(RoomActivity.this, "Clicou em" + nomeItem, Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private class ExecuteSearch extends AsyncTask <String, Void, String> {

        private String baseUrl = "https://api.yelp.com/v3/businesses/search?";
        private String locationParam = "&location=";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String cityName = strings[0], url;

            if (cityName.indexOf("-") != -1) {
                cityName = cityName.substring(0, cityName.indexOf("-"));
            }

            if (cityName.indexOf(",") != -1) {
                cityName = cityName.substring(0, cityName.indexOf(","));
            }

            url = baseUrl.concat(locationParam).concat(cityName);

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

            ArrayList<Localizacao> itens = new ArrayList<>();

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

                        rating =    obj.has("rating") ? obj.getLong("rating") : 0;
                        price =     obj.has("price") ? obj.getString("price") : " ";
                        phone =     obj.has("display_phone") ? obj.getString("display_phone") : "";
                        img_url =   obj.has("image_url") ? obj.getString("image_url") : null;
                        url = obj.getString("url");

                        itens.add(new Localizacao(id, title, address, rating, price, phone, img_url, url));
                    }

                    Log.i("LENGTH", Integer.toString(itens.size()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            listView = (ListView)findViewById(R.id.listView7);

            adapter = new LocalizacaoAdapter(getApplicationContext(), 0, itens);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int i, long l) {
                    //String nomeItem = (String)listView.getItemAtPosition(i);
                    Intent intent = new Intent(BuscaActivity.this, DetalhesActivity.class);
                    // Passar dados do local para renderizar na prox pagina
                    startActivity(intent);
                    //String nomeItem = (String)listView.getItemAtPosition(i);
                    //  Toast.makeText(RoomActivity.this, "Clicou em" + nomeItem, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
