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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BuscaActivity extends AppCompatActivity implements AsyncResponse {
    private ListView listView;
    private ArrayList<Localizacao> itens;
    private Bundle extras;
    private ProgressBar progressBar;
    private TextView statusTextView;

    private LocalizacaoDAO localizacaoDAO;
    private ExecuteSearch executeSearch;

    //  Deslocamento do indice durante a busca
    private static int itemOffset = 0;
    //  Valor máximo permitido pela API
    private static final int ITEM_OFFSET_INCREMENT = 50;
    //  Limite de resultados da API por cada busca
    private static final int ITEM_OFFSET_LIMIT = 1000;
    //  Caso tenha menos de 1000 itens
    private static int ITEM_OFFSET_MAX = 0;

    private String urlParams;
    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca);

        itemOffset = 0;

        localizacaoDAO = new LocalizacaoDAO(this);
        localizacaoDAO.open();

        listView = (ListView) findViewById(R.id.listViewBusca);
        statusTextView = (TextView)findViewById(R.id.statusTextViewBusca);

        progressBar = (ProgressBar) findViewById(R.id.progressBarBusca);
        extras = getIntent().getExtras();

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
    protected void onStop() {
        super.onStop();

        if (executeSearch != null) { executeSearch.cancel(true); }
        localizacaoDAO.close();
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
        String locationParam = "&location=";

        cityName = extras.getString(MainActivity.CITY_NAME);
        progressBar.setVisibility(View.VISIBLE);

        setTitle(cityName);

        if (cityName.indexOf("-") != -1) {
            cityName = cityName.substring(0, cityName.indexOf("-"));
        }

        if (cityName.indexOf(",") != -1) {
            cityName = cityName.substring(0, cityName.indexOf(","));
        }

        if (cityName.charAt(cityName.length()-1) == ' '){
            cityName = cityName.substring(0, cityName.length()-1);
        }

        urlParams = locationParam.concat(cityName);


        if (localizacaoDAO.databaseHasCity(cityName)) {
            createListView();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            statusTextView.setVisibility(View.VISIBLE);

            startSearch();
        }
    }

    private void createListView() {
        itens = localizacaoDAO.getAllRestaurants(cityName);

        listView.setAdapter(new LocalizacaoAdapter(this, itens));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int pos, long l) {
                Localizacao local = itens.get(pos);
                Intent intent = new Intent(BuscaActivity.this, DetalhesActivity.class);
                intent.putExtra("LOCAL", local);

                startActivity(intent);
            }
        });

        progressBar.setVisibility(View.GONE);
        statusTextView.setVisibility(View.GONE);
    }

    /**
     * Começa a baixar os dados de cada cidade.
     * O limite permitido pela API é de até 1000 localizações
     */
    private void startSearch () {
        String offset = String.format("%d", itemOffset);
        String increment = String.format("%d", ITEM_OFFSET_INCREMENT);
        Log.i("LOG", String.format("ITEM OFFSET MAX %d",ITEM_OFFSET_MAX));
        Log.i("LOG", String.format("ITEM OFFSET %d",itemOffset));

        if (ITEM_OFFSET_MAX != 0 && (itemOffset + ITEM_OFFSET_INCREMENT) > ITEM_OFFSET_MAX || (itemOffset + ITEM_OFFSET_INCREMENT) > ITEM_OFFSET_LIMIT) {
            createListView();

            return;
        }

        statusTextView.setText(String.format("Carregando dados (%d de %d)...", itemOffset, ITEM_OFFSET_MAX));

        executeSearch = new ExecuteSearch();
        executeSearch.delegate = this;
        executeSearch.execute(urlParams, offset, increment);

        itemOffset = itemOffset + ITEM_OFFSET_INCREMENT;
    }

    /**
     * Insere no banco os dados obtidos na requisição
     * @param response
     */
    @Override
    public void processFinish(JSONObject response) {


        String id, title, address, address1, address2, address3, city, price, phone, img_url, url;
        float lat, lon, rating;
        Localizacao loc;

        try {
            ITEM_OFFSET_MAX = response.has("total") ? response.getInt("total") : null;
            ITEM_OFFSET_MAX = ITEM_OFFSET_MAX > ITEM_OFFSET_LIMIT ? ITEM_OFFSET_LIMIT : ITEM_OFFSET_MAX;

            JSONArray jsonArray = response.getJSONArray("businesses");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                id = obj.getString("id");
                title = obj.getString("name");

                JSONObject a = obj.getJSONObject("location");
                address1 = a.getString("address1");
                address2 = a.getString("address2");
                address3 = a.getString("address3");

                address = address1;
                address = address2.length() > 0 ? address.concat(" ").concat(address2) : address;
                address = address3.length() > 0 ? address.concat(" ").concat(address3) : address;

                city = a.getString("city");

                JSONObject l = obj.getJSONObject("coordinates");
                lat = BigDecimal.valueOf(l.getDouble("latitude")).floatValue();
                lon = BigDecimal.valueOf(l.getDouble("longitude")).floatValue();

                rating =    obj.has("rating") ? BigDecimal.valueOf(obj.getDouble("rating")).floatValue() : 0;
                price =     obj.has("price") ? obj.getString("price") : " ";
                phone =     obj.has("display_phone") ? obj.getString("display_phone") : "";
                img_url =   obj.has("image_url") ? obj.getString("image_url") : null;
                url = obj.getString("url");

                loc = new Localizacao(id, title, address, city, lat, lon, rating, price, phone, img_url, url);
                new InsertRow().execute(loc);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            // Ao terminar de inserir, realiza uma nova busca por mais resultados
            // A API só perimite até 50 resultados por busca
            startSearch();
        }
    }

    private class InsertRow extends AsyncTask<Localizacao, Void, Void> {

        @Override
        protected Void doInBackground(Localizacao... localizacoes) {
            localizacaoDAO.insetLocalizacao(localizacoes[0]);
            return null;
        }
    }

    /**
     * Executa a busca em uma thread separada
     */
    private class ExecuteSearch extends AsyncTask <String, Void, String> {

        private String baseUrl = "https://api.yelp.com/v3/businesses/search?";
        private String offset = "&offset=";
        private String limit = "&limit=";
        private String categorie = "&categories=restaurants";

        public AsyncResponse delegate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url;

            url = baseUrl.concat(strings[0]).concat(offset).concat(strings[1]).concat(limit).concat(strings[2]).concat(categorie);
            Log.i("LOG", url);

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
