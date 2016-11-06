package com.example.android.sunshine.app;

/**
 * Created by wys on 2016/11/5.
 */

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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.sunshine.app.utils.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.action_refresh){
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
        temperatures.add("2016/11/4 25 cloudy");
        temperatures.add("2016/11/5 23 snow");
        temperatures.add("2016/11/6 26 rain");
        temperatures.add("2016/11/7 22 cloudy");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, temperatures);
        ListView listView = (ListView) rootView.findViewById(R.id.listView_forecast);

        listView.setAdapter(arrayAdapter);
        return rootView;
    }

    class FetchWeatherTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String weatherUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?id=1816670&mode=json&units=metric&cnt=7&APPID=f24bd4b2d265dd3c8861f0642fa64045";
            String baseUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?";
            final String cityCode = "id";
            final String mode = "mode";
            final String uniits = "units";
            final String days = "cnt";
            final String appID = "APPID";
            Uri builtUri = Uri.parse(baseUrl).buildUpon()
                    .appendQueryParameter(cityCode, strings[0])
                    .appendQueryParameter(mode,"json")
                    .appendQueryParameter(uniits, "metric")
                    .appendQueryParameter(days, "7")
                    .appendQueryParameter(appID, "f24bd4b2d265dd3c8861f0642fa64045")
                    .build();
            String result = null;
            try {
//                Log.v(FetchWeatherTask.class.getSimpleName(), "url" + builtUri.toString());

                result = HttpUtil.httpGET(builtUri.toString());
                Log.v(FetchWeatherTask.class.getSimpleName(), "weatherString" + result);
            } catch (IOException e) {
                result = "";
                Log.e(ForecastFragment.class.getSimpleName(), e.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result){

        }
    }
}