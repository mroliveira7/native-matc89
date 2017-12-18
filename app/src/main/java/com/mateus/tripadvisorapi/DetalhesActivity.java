package com.mateus.tripadvisorapi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetalhesActivity extends AppCompatActivity {
    private Localizacao localizacao;

    private TextView titulo;
    private TextView descricao;
    private ImageView image;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            localizacao = (Localizacao) extras.getParcelable("LOCAL");

            titulo = (TextView) findViewById(R.id.textViewDetalhesTitle);
            descricao = (TextView) findViewById(R.id.textViewDetalhesDescricao);
            image = (ImageView) findViewById(R.id.imageViewDetalhes);
            ratingBar = (RatingBar) findViewById(R.id.ratingBarDetalhes);

            titulo.setText(localizacao.getTitle());
            descricao.setText(localizacao.getAddress());
            ratingBar.setRating(localizacao.getRating());

            new GetImageTask(image).execute(localizacao.getImg_url());
        }

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

    private class GetImageTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public GetImageTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                return BitmapFactory.decodeStream(conn.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                cancel(true);
            }

            return null;
        }

        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            ImageView imageView = imageViewReference.get();

            if (imageView != null) {
                if (result != null) {
                    image.setImageBitmap(result);
                } else {
                    image.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_restaurant));
                }
            }
        }
    }
}

