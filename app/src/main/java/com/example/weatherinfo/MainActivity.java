package com.example.weatherinfo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androdocs.httprequest.HttpRequest;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    String CITY;
    String API = "a9f683b1b3e35d51f57086b5a4485c90";
    ImageView search;
    EditText etCity;
    TextView city,country,time,temp,forecast,humidity,sunrises,sunsets,pressure,windSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        {
            etCity = (EditText) findViewById(R.id.Your_city);
            search = (ImageView) findViewById(R.id.search);

            // CALL ALL ANSWERS :

            city = (TextView) findViewById(R.id.city);
            country = (TextView) findViewById(R.id.country);
            time = (TextView) findViewById(R.id.time);
            temp = (TextView) findViewById(R.id.temp);
            forecast = (TextView) findViewById(R.id.forecast);
            humidity = (TextView) findViewById(R.id.humidity);
            sunrises = (TextView) findViewById(R.id.sunrises);
            sunsets = (TextView) findViewById(R.id.sunsets);
            pressure = (TextView) findViewById(R.id.pressure);
            windSpeed = (TextView) findViewById(R.id.wind_speed);

            // CLICK ON SEARCH BUTTON :

            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CITY = etCity.getText().toString();
                    new weatherTask().execute();
                }
            });
        }
    }

     class weatherTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=" + API);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);
                JSONObject wind = jsonObj.getJSONObject("wind");
                JSONObject sys = jsonObj.getJSONObject("sys");



                // CALL VALUE IN API :
                String city_name = jsonObj.getString("name");
                String countryname = sys.getString("country");
                Long updatedAt = jsonObj.getLong("dt");
                String updatedAtText = "Last Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                String temperature = main.getString("temp");
                String cast = weather.getString("description");
                String humi_dity = main.getString("humidity");
                String pre = main.getString("pressure");
                String windspeed = wind.getString("speed");
                Long rise = sys.getLong("sunrise");
                String sunrise = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(rise * 1000));
                Long set = sys.getLong("sunset");
                String sunset = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(set * 1000));






                city.setText(city_name);
                country.setText(countryname);
                time.setText(updatedAtText);
                temp.setText(temperature + "??C");
                forecast.setText(cast);
                humidity.setText(humi_dity);
                sunrises.setText(sunrise);
                sunsets.setText(sunset);
                pressure.setText(pre);
                windSpeed.setText(windspeed);
                Toasty.info(MainActivity.this, "Temprature in "+city_name+" is "+ temperature +" degree", Toast.LENGTH_LONG, true).show();

            } catch (Exception e) {


                Toasty.error(MainActivity.this, "City  "+CITY+"  Not found", Toast.LENGTH_LONG, true).show();
            }
        }
    }
}
