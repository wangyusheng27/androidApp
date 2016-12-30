package com.example.android.sunshine.app;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.sunshine.app.utils.HttpUtil;
import com.example.android.sunshine.app.utils.WeatherDataParser;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by wys on 2016/12/30.
 */

public class OldFetchWeatherTask extends AsyncTask<String, Void, String[]> {

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
                .appendQueryParameter(uniits, strings[1])
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
            jsonResult = WeatherDataParser.getWeatherDataFromJson(result, 7, strings[1]);
        } catch (JSONException e) {
            Log.e(ForecastFragment.class.getSimpleName(), e.toString());
        }
        return jsonResult;
    }

    @Override
    protected void onPostExecute(String[] result) {
//        if (result != null){
//            arrayAdapter.clear();
//            for (String res : result){
//                arrayAdapter.add(res);
//            }
//        }
    }
}