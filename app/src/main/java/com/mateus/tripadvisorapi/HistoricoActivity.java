package com.mateus.tripadvisorapi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HistoricoActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> listAdapter;
    private LocalizacaoAdapter adapter;
    private List<Localizacao> grupos = new ArrayList<Localizacao>();
    private ArrayList<Localizacao> itens;
    private TextView statusTextView;


    private LocalizacaoDAO localizacaoDAO;
    //private FavoritosActivity.ExecuteSearch executeSearch;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        localizacaoDAO = new LocalizacaoDAO(this);
        localizacaoDAO.open();

        listView = (ListView) findViewById(R.id.listViewBusca);
        statusTextView = (TextView)findViewById(R.id.statusTextViewBusca);

        handler = new Handler();

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        listView = (ListView)findViewById(R.id.listView3);
        /*adapter = new LocalizacaoAdapter(this, 0, itens);
        listView.setAdapter(adapter);
        // listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //@Override
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int i, long l) {
                //String nomeItem = (String)listView.getItemAtPosition(i);
                // Intent intent = new Intent(RoomActivity.this, HomeActivity.class);
                //String nomeItem = (String)listView.getItemAtPosition(i);
                //  Toast.makeText(RoomActivity.this, "Clicou em" + nomeItem, Toast.LENGTH_SHORT).show();
            }
        });*/

        createListView();
    }

//    protected void onStop() {
//        super.onStop();
//
//        if (executeSearch != null) { executeSearch.cancel(true); }
//        localizacaoDAO.close();
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void createListView() {
        itens = localizacaoDAO.getAllRestaurantsByHistorico();
        final LocalizacaoAdapter adapter = new LocalizacaoAdapter(this, itens);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int pos, long l) {
                Localizacao local = itens.get(pos);
                Log.d("teste", local.getTitle());
                Intent intent = new Intent(HistoricoActivity.this, DetalhesActivity.class);
                intent.putExtra("LOCAL", local);

                startActivity(intent);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // SCROLL STOP
                        adapter.setSCROLL_STOP(true);
                        handler.postDelayed(updateListView, 500);

                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        // SCROLL SCROLLING
                        adapter.setSCROLL_STOP(false);
                        handler.removeCallbacks(updateListView);

                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int first, int vistibleCount, int totalItemCount) {

            }
        });

        //progressBar.setVisibility(View.GONE);
        //statusTextView.setVisibility(View.GONE);
    }

    public Runnable updateListView = new Runnable() {
        @Override
        public void run() {
            int first, last;

            first = listView.getFirstVisiblePosition();
            last = listView.getLastVisiblePosition();


            for (int i = first; i <= last; i++) {
                final int dataPosition = i - listView.getHeaderViewsCount();
                final int childPosition = i - listView.getFirstVisiblePosition();

                if (dataPosition >= 0
                        && dataPosition < listView.getAdapter().getCount()
                        && listView.getChildAt(childPosition) != null) {
                    listView.getAdapter().getView(i, listView.getChildAt(childPosition), listView);
                }
            }
        }
    };


}
