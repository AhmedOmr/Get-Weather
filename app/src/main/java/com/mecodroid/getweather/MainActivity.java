package com.mecodroid.getweather;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ImageView  iconView;
    TextView nameView, timeView, tempView;
    EditText editText;
    String s;
    CheckInternet checkInternet;
    boolean b, a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iconView = findViewById(R.id.weathericon);
        nameView = findViewById(R.id.name);
        timeView = findViewById(R.id.localtime);
        tempView = findViewById(R.id.temp);
        editText = findViewById(R.id.editText);
        s = editText.getText().toString();
        checkInternet = new CheckInternet(MainActivity.this);
        a = checkInternet.Is_Connecting();
        if (!a) {
            Toast.makeText(MainActivity.this, "please check Internet Connection", Toast.LENGTH_LONG).show();
            finish();
        } else {
            getWeather weather = new getWeather();
            //Pass US Zipcode, UK Postcode, Canada Postalcode, IP address, Latitude/Longitude (decimal degree) or city name
            weather.execute("cairo");
        }
    }

    public void show_weather(View view) {
        checkInternet = new CheckInternet(MainActivity.this);
        b = checkInternet.Is_Connecting();
        if (!b) {
            Toast.makeText(MainActivity.this, "please check Internet Connection", Toast.LENGTH_SHORT).show();

        } else {

            getWeather weather = new getWeather();
            //Pass US Zipcode, UK Postcode, Canada Postalcode, IP address, Latitude/Longitude (decimal degree) or city name
            weather.execute(editText.getText().toString());
        }
    }

    class getWeather extends AsyncTask<String, Void, String> {
        StringBuilder sb = new StringBuilder();

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://api.apixu.com/v1/current.json?key=77d2de2c78154d45b88120453181407&q=" + strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(isr);
                String s = "";
                while ((s = br.readLine()) != null) {
                    sb.append(s);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //UI
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject location = jsonObject.getJSONObject("location");
                String localtime = location.getString("localtime");
                String region = location.getString("region");
                String name = location.getString("name");
                String country = location.getString("country");
                final double lat = location.getDouble("lat");
                final double lon = location.getDouble("lon");

                JSONObject current = jsonObject.getJSONObject("current");
                double temp_c = current.getDouble("temp_c");
                JSONObject condition = current.getJSONObject("condition");
                String text = condition.getString("text");
                String icon = condition.getString("icon");

                nameView.setText(name + ", " + region + ", " + country);
                timeView.setText(localtime);
                tempView.setText(String.valueOf(temp_c) + " °C" + "\n" + text);

                Picasso.with(MainActivity.this).load("http:" + icon).into(iconView);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
