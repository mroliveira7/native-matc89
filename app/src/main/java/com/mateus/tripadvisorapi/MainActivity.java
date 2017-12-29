package com.mateus.tripadvisorapi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private Button gpsButton;
    private AutoCompleteTextView autocomplete;

    private LocationManager locationManager;
    private Location location;

    public static final String CITY_NAME = "CITY_NAME";
    public static final int LOCATION_PERMISSION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        gpsButton = (Button) findViewById(R.id.gpsButton);
        gpsButton.setOnClickListener(this);

        autocomplete = (AutoCompleteTextView) findViewById(R.id.autocomplete);

        autocomplete.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.autocomplete_list_item));
        autocomplete.setOnItemClickListener(cityClickListerner);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;

        if (id == R.id.nav_favoritos) {
            intent = new Intent(this, FavoritosActivity.class);

            startActivity(intent);
        } else if (id == R.id.nav_historico) {
            intent = new Intent(this, HistoricoActivity.class);

            startActivity(intent);
        } else if (id == R.id.nav_creditos) {
            intent = new Intent(this, DesenvolvedorListActivity.class);

            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startSearchCity (String city) {
        Intent intent;
        intent = new Intent(getApplicationContext(), BuscaActivity.class);
        intent.putExtra(MainActivity.CITY_NAME, city);

        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gpsButton:
                updateLocation();
                break;
        }
    }

    private void updateLocation() {
        if ( Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);

            return;
        }

        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, false);

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(getApplicationContext(), "Não foi possível obter a localização.", Toast.LENGTH_SHORT).show();

                return;
            }

            if (locationManager.getLastKnownLocation(bestProvider) != null) {
                Log.i("LOCATION", "KNOWN LOCATION");
                location = locationManager.getLastKnownLocation(bestProvider);
                getAddressFromLocation();
            } else {
                locationManager.requestLocationUpdates(bestProvider, 1000, 1, locationListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateLocation();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Por favor, autorize o acesso a localização.", Toast.LENGTH_SHORT);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);

                    if( v != null) {
                        v.setGravity(Gravity.CENTER);
                    }

                    toast.show();
                }
        }
    }

    private void getAddressFromLocation() {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> address;

        try {
            address = geocoder.getFromLocation(this.location.getLatitude(), this.location.getLongitude(), 1);

            if (address.size() > 0) {
                Log.i("ADDRESS", address.get(0).getLocality());
                Intent intent;
                intent = new Intent(getApplicationContext(), BuscaActivity.class);
                intent.putExtra(MainActivity.CITY_NAME, address.get(0).getLocality());

                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Não foi possível obter a localização.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location.getAccuracy() <= 150) {
                locationManager.removeUpdates(this);

                getAddressFromLocation();
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {}

        @Override
        public void onProviderEnabled(String s) {}

        @Override
        public void onProviderDisabled(String s) {}
    };

    private AdapterView.OnItemClickListener cityClickListerner = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            String cityName = (String) adapterView.getItemAtPosition(position);

            Intent intent;
            intent = new Intent(getApplicationContext(), BuscaActivity.class);
            intent.putExtra(MainActivity.CITY_NAME, cityName);

            startActivity(intent);
        }
    };
}
