package com.example.android.sunshine.app;

/**
 * Created by wys on 2016/11/5.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Build;
import com.example.android.sunshine.app.utils.HttpUtil;
import com.example.android.sunshine.app.utils.WeatherDataParser;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }

    ArrayAdapter<String> arrayAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart(){
        super.onStart();
        updateWeather();
    }

    private void updateWeather(){
        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity(), arrayAdapter);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String location = prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
        String units = prefs.getString(getString(R.string.pref_units_key), getString(R.string.pref_units_metric));
        weatherTask.execute(location, units);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        List<String> temperatures = new ArrayList<String>();
        this.arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, temperatures);
        ListView listView = (ListView) rootView.findViewById(R.id.listView_forecast);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String forecast = arrayAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(intent);
            }
        });


        return rootView;
    }


}