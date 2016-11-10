package com.example.android.sunshine.app;

/**
 * Created by wys on 2016/11/5.
 */

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
            FetchWeatherTask weatherTask = new FetchWeatherTask();
            weatherTask.execute("1816670");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        List<String> temperatures = new ArrayList<>();
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
        FetchWeatherTask weatherTask = new FetchWeatherTask();
        weatherTask.execute("1816670");

        return rootView;
    }

    class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... strings) {
            String weatherUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?id=1816670&mode=json&units=metric&cnt=7&APPID=f24bd4b2d265dd3c8861f0642fa64045";
            String baseUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?";
            final String cityCode = "id";
            final String mode = "mode";
            final String uniits = "units";
            final String days = "cnt";
            final String appID = "APPID";
            Uri builtUri = Uri.parse(baseUrl).buildUpon()
                    .appendQueryParameter(cityCode, strings[0])
                    .appendQueryParameter(mode, "json")
                    .appendQueryParameter(uniits, "metric")
                    .appendQueryParameter(days, "7")
                    .appendQueryParameter(appID, "f24bd4b2d265dd3c8861f0642fa64045")
                    .build();
            String result = null;
            try {
                result = HttpUtil.httpGET(builtUri.toString());
            } catch (IOException e) {
                result = "";
                Log.e(ForecastFragment.class.getSimpleName(), e.toString());
            }
            String[] jsonResult = null;
            try {
                jsonResult = WeatherDataParser.getWeatherDataFromJson(result, 7);
            } catch (JSONException e) {
                Log.e(ForecastFragment.class.getSimpleName(), e.toString());
            }
            return jsonResult;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null){
                arrayAdapter.clear();
                for (String res : result){
                    arrayAdapter.add(res);
                }
            }
     }
    }
}