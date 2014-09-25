package fr.vlille.durieux.vlille;



import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.util.*;

import durieux.vlille.api.Station;
import durieux.vlille.api.VLille;
import durieux.vlille.api.VLilleImpl;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class StationList extends Fragment {

    final HashMap<Integer, Station> stations = new HashMap<Integer, Station>();

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static StationList newInstance() {
        StationList fragment = new StationList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public StationList() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_station_list, container, false);

        final ListView listview = (ListView) rootView.findViewById(R.id.stations_list);

        if(listview != null) {
            final StationAdapter adapter = new StationAdapter(getActivity(),
                    android.R.layout.simple_list_item_1, this.stations);
            
            final VLille vlille = new VLilleImpl();

            LocationListener ll = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    adapter.updateWithLocation(location);
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };

            LocationManager lm = (LocationManager) getActivity().getSystemService(getActivity().getApplicationContext().LOCATION_SERVICE);
            if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, ll);
            else if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, ll);

            class RetrieveStationTask extends AsyncTask<String, java.util.Map<Integer, Station>, java.util.Map<Integer, Station>> {
                private Exception exception;

                protected java.util.Map<Integer, Station> doInBackground(String... urls) {
                    try {
                        return vlille.getStationsWithDetails();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                protected void onPostExecute(java.util.Map<Integer, Station> listStation) {
                    if(listStation == null) return;
                    adapter.clear();
                    adapter.addAll(listStation.values());
                }
            }
            new RetrieveStationTask().execute();

            listview.setAdapter(adapter);
        }
        return rootView;
    }
}
