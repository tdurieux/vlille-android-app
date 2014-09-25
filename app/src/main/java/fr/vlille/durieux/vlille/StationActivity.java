package fr.vlille.durieux.vlille;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class StationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);



        Intent intent = getIntent();
        int id = intent.getIntExtra("station_id", 0);
        String name = intent.getStringExtra("station_name");
        String distance = intent.getStringExtra("station_distance");
        String address = intent.getStringExtra("station_address");
        int bikes = intent.getIntExtra("station_bikes", 0);
        int free = intent.getIntExtra("station_free", 0);
        Double lat = intent.getDoubleExtra("station_lat", 0);
        Double lng =  intent.getDoubleExtra("station_lng", 0);

        getActionBar().setTitle(name);

        ((TextView)findViewById(R.id.distance)).setText(distance);
        ((TextView)findViewById(R.id.name)).setText(name);
        ((TextView)findViewById(R.id.address)).setText(address);
        ((TextView)findViewById(R.id.bikes)).setText(bikes + "");
        ((TextView)findViewById(R.id.free)).setText(free + "");


        MapFragment mMapView = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        GoogleMap map = mMapView.getMap();
        if(map != null) {
            final LatLng PERTH = new LatLng(lat, lng);
            Marker perth = map.addMarker(new MarkerOptions().position(PERTH).flat(true));
            CameraUpdate center=
                    CameraUpdateFactory.newLatLng(PERTH);
            CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

            map.moveCamera(center);
            map.animateCamera(zoom);

            map.getUiSettings().setZoomControlsEnabled(false);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.station, menu);
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
}
