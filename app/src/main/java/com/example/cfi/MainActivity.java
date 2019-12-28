package com.example.cfi;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView cityName, result;
    Button citySearch;
    private static final String TAG = "";

    class Weather extends AsyncTask<String, Void, String> { //first String is url, void means nothing, return type is String
        @Override
        protected String doInBackground(String... address) {
            //String... means multiple addresses can be sent. act like a array
            try {
                URL url = new URL(address[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int data = isr.read();
                String content = "";
                char ch;
                while (data != -1) {
                    ch = (char) data;
                    content = content + ch;
                    data = isr.read();

                }
                return content;

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public void search(View view) {
        cityName = findViewById(R.id.txt_cityName);
        citySearch = findViewById(R.id.btn_citySearch);
        result = findViewById(R.id.txt_result);

        String cName = cityName.getText().toString();


        String content;
        Weather weather = new Weather();
        try {
            content = weather.execute("https://openweathermap.org/data/2.5/weather?q=" +cName
                    +"&appid=b6907d289e10d714a6e88b30761fae22").get();
            Log.i(TAG, "content:" + content);     //check data retrieve
            //JSON
            JSONObject jsonObject = new JSONObject(content);
            String weatherData = jsonObject.getString("weather");
            String mainTemperature = jsonObject.getString("main");
            double visibility;

            Log.i(TAG, "weatherData" + weatherData);
            //weathr data is in Array
            JSONArray array = new JSONArray(weatherData);

            String main = "";
            String description = "";
            String temp = "";

            for (int i = 0; i < array.length(); i++) {
                JSONObject weatherPart = array.getJSONObject(i);
                main = weatherPart.getString("main");
                description = weatherPart.getString("description");
            }
            Log.i(TAG, "main: " + main);
            Log.i(TAG, "description: " + description);


            JSONObject mainObj = new JSONObject(mainTemperature);
            temp = mainObj.getString("temp");
            Log.i(TAG, "temp: " + temp);

            visibility = Double.parseDouble(jsonObject.getString("visibility"));
            int visibilityInKms = (int) visibility/1000;

            String resultText = "Main :" + main +
                    "\nDescription :" +description +
                    "\nTemperature :" + temp +"°C" +
                    "\nVisibility :" + visibilityInKms+"KM";
            result.setText(resultText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}
