package fr.vlille.durieux.vlille;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import durieux.vlille.api.Station;

/**
 * Created by durieux on 25/09/14.
 */
public class StationAdapter extends ArrayAdapter<Station> {

    private Location location;

    private HashMap<Integer, Station> stations;
    public StationAdapter(Context context, int textViewResourceId,
                              HashMap<Integer, Station> stations) {
        super(context, textViewResourceId,new ArrayList<Station>(stations.values()));
        this.stations = stations;
        setNotifyOnChange(true);
    }


    public void updateWithLocation(Location location) {
        this.location = location;
        notifyDataSetChanged();
    }

    @Override
    public void addAll(Station... items) {
        super.addAll(items);
        for (int i = 0; i < items.length; i++) {
            Station s = items[i];
            stations.put(s.getId(), s);
        }
    }

    @Override
    public void clear() {
        super.clear();
        stations = new HashMap<Integer, Station>();
    }

    @Override
    public void add(Station object) {
        super.add(object);
        stations.put(object.getId(), object);
    }

    @Override
    public void addAll(Collection<? extends Station> collection) {
        super.addAll(collection);
        Iterator<? extends Station> it = collection.iterator();
        while (it.hasNext()) {
            Station s = it.next();
            stations.put(s.getId(), s);
        }
    }

    @Override
    public int getCount() {
        return stations.size();
    }

    @Override
    public void remove(Station object) {
        super.remove(object);
        stations.remove(object.getId());
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) getContext()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        //Log.d("GetView", "convertView " + convertView);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.station_item, null);
        }
        holder = new ViewHolder();

        holder.favoris = (CheckBox) convertView
                .findViewById(R.id.favoris);
        holder.name = (TextView) convertView
                .findViewById(R.id.name);
        holder.bikes = (TextView) convertView.findViewById(R.id.bikes);
        holder.free = (TextView) convertView
                .findViewById(R.id.free);
        holder.distance = (TextView) convertView
                .findViewById(R.id.distance);

        final Station station = getItem(position);
        //Log.d("GetView", "Position " + position + " Station " + station.toString());
        holder.favoris.setActivated(false);
        holder.name.setText(station.getName());
        holder.bikes.setText(station.getBikes()+"");
        holder.free.setText((station.getBikes() - station.getAttachs()) + "");
        final String strDistance;
        if(this.location != null) {
            Location stationLocation = new Location("");
            stationLocation.setLatitude(station.getLng());
            stationLocation.setLongitude(station.getLat());

            float distance = this.location.distanceTo(stationLocation);
            strDistance = convertDistance(distance);
            holder.distance.setText(strDistance);
        } else {
            strDistance = "";
        }

        convertView.setTag(holder);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Selection item", "Select item " + station);
                selectItem(station, strDistance);
            }
        });
        return convertView;
    }

    private String convertDistance(float meter) {
        if(meter < 1) {
            return ((int) meter*100) + "cm";
        }
        if (meter >= 1000 ) {
            return ((int) meter / 1000) + "km";
        }
        return ((int) meter) + "m";
    }

    private void selectItem(Station item, String distance) {
        Intent intent = new Intent(getContext().getApplicationContext(), StationActivity.class);
        intent.putExtra("station_id", item.getId());
        intent.putExtra("station_name", item.getName());
        intent.putExtra("station_distance", distance);
        intent.putExtra("station_address", item.getAddress());
        intent.putExtra("station_bikes", item.getBikes());
        intent.putExtra("station_free", item.getBikes() - item.getAttachs());
        intent.putExtra("station_lat", item.getLat());
        intent.putExtra("station_lng", item.getLng());

        getContext().startActivity(intent);
    }

    private class ViewHolder {
        CheckBox favoris;
        TextView name;
        TextView bikes;
        TextView free;
        TextView distance;
    }
}