package com.mateus.tripadvisorapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class HistoricoActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> listAdapter;
    private LocalizacaoAdapter adapter;
    private List<Localizacao> grupos = new ArrayList<Localizacao>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
