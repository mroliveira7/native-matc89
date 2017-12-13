package com.mateus.tripadvisorapi;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class BuscaActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> listAdapter;
    private LocalizacaoAdapter adapter;
    private Localizacao[] itens = {
            new Localizacao("Brasil", "Bahia", "Salvador","Porto da Barra","5 estrelas"),
            new Localizacao("Brasil", "Bahia", "Salvador","Acarajé da Dinha","4.5 estrelas")
    };
    private List<Localizacao> grupos = new ArrayList<Localizacao>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca);
        listView = (ListView)findViewById(R.id.listView);
        adapter = new LocalizacaoAdapter(this, 0, itens);
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
        });
    }
}
