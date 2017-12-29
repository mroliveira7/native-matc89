package com.mateus.tripadvisorapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetalhesActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, Animation.AnimationListener{
    private Localizacao localizacao;

    LocalizacaoDAO localizacaoDAO;

    private TextView titulo;
    private TextView endereco;
    private ImageView image;
    private RatingBar ratingBar;
    private TextView price;
    private TextView phone;
    private SupportMapFragment mapFragment;

    private FloatingActionButton favorito;

    private TranslateAnimation moveLefttoRight;
    private TranslateAnimation moveRighttoLeft;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.detailsMap);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        moveLefttoRight = new TranslateAnimation(0, 400, 0, 0);
        moveLefttoRight.setDuration(250);
        moveLefttoRight.setFillAfter(true);
        moveLefttoRight.setAnimationListener(this);

        moveRighttoLeft = new TranslateAnimation(400, 0, 0, 0);
        moveRighttoLeft.setDuration(250);
        moveRighttoLeft.setFillAfter(true);
        moveRighttoLeft.setAnimationListener(this);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            localizacao = (Localizacao) extras.getParcelable("LOCAL");

            setTitle(localizacao.getTitle());

            titulo = (TextView) findViewById(R.id.textViewDetalhesTitle);
            endereco = (TextView) findViewById(R.id.textViewDetalhesEndereco);
            image = (ImageView) findViewById(R.id.imageViewDetalhes);
            ratingBar = (RatingBar) findViewById(R.id.ratingBarDetalhes);
            price = (TextView) findViewById(R.id.textViewDetalhesPrice);
            phone = (TextView) findViewById(R.id.textViewDetalhesPhone);

            titulo.setText(localizacao.getTitle());
            endereco.setText(localizacao.getAddress());
            ratingBar.setRating(localizacao.getRating());
            price.setText(localizacao.getPrice());
            phone.setText(localizacao.getPhone());

            localizacaoDAO = new LocalizacaoDAO(this);
            localizacaoDAO.open();

            new GetImageTask(image).execute(localizacao.getImg_url());
        }

        favorito = (FloatingActionButton) findViewById(R.id.favoritoButton);
        favorito.setOnClickListener(favoritarListener);

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

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng coordenadas = new LatLng(localizacao.getLat(), localizacao.getLon());

        Marker marker = map.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title(localizacao.getTitle()));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 15));
        map.animateCamera(CameraUpdateFactory.zoomIn());
        map.animateCamera(CameraUpdateFactory.zoomTo(15) , 2000, null);
        map.getUiSettings().setAllGesturesEnabled(false);
        map.setOnMarkerClickListener(this);
        map.setOnMapClickListener(this);
    }


    private View.OnClickListener favoritarListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SharedPreferences prefs = getPreferences(MODE_PRIVATE);

            SharedPreferences.Editor editor = prefs.edit();

            String id_location = localizacao.getId_location();

            editor.putString("favorito", id_location);
            editor.commit();
            boolean isFavorito = localizacaoDAO.localizacaoIsFavorito(id_location);
            if (isFavorito){
                long id = localizacaoDAO.removeId(id_location);
                Toast.makeText(getApplicationContext(), "Removido dos favoritos", Toast.LENGTH_SHORT).show();
            }else{
                long id = localizacaoDAO.insertId(id_location);
                Toast.makeText(getApplicationContext(), "Adicionado aos favoritos", Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (favorito.getVisibility() == View.VISIBLE) {
            favorito.startAnimation(moveLefttoRight);
        }

        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (favorito.getVisibility() == View.GONE) {
            favorito.setVisibility(View.VISIBLE);
            favorito.startAnimation(moveRighttoLeft);
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
        if (animation == moveRighttoLeft) {
            favorito.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == moveLefttoRight) {
            favorito.setVisibility(View.GONE);
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

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

