package mg.studio.weatherappdesign;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    String wText0;
    String wText1;
    String wText2;
    String wText3;
    String wText4;
    String[] wDays = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};

    public int getSmallImg(String weather) {
        switch (weather) {
            case "Clear":
                return R.drawable.sunny_small;
            case "Wind":
                return R.drawable.windy_small;
            case "Clouds":
                return R.drawable.partly_sunny_small;
            case "Rain":
                return R.drawable.rainy_small;
        }
        return R.drawable.sunny_small;
    }

    public int getUpImg(String weather) {
        switch (weather) {
            case "Clear":
                return R.drawable.sunny_up;
            case "Wind":
                return R.drawable.windy_up;
            case "Clouds":
                return R.drawable.partly_sunny_up;
            case "Rain":
                return R.drawable.rainy_up;
        }
        return R.drawable.sunny_up;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("EEEE");
        ft = new SimpleDateFormat("yyyy-MM-dd");
        String todaystr = ft.format(date);
        String datestr = ft.format(date);
        ((TextView) findViewById(R.id.tv_date)).setText(datestr);
        ((TextView) findViewById(R.id.todayText)).setText(todaystr);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int weekIndex = cal.get(Calendar.DAY_OF_WEEK) - 1;
        ((TextView) findViewById(R.id.nextText1)).setText(wDays[weekIndex++]);
        ((TextView) findViewById(R.id.nextText2)).setText(wDays[weekIndex++]);
        ((TextView) findViewById(R.id.nextText3)).setText(wDays[weekIndex++]);
        ((TextView) findViewById(R.id.nextText4)).setText(wDays[weekIndex]);
    }

    public void btnClick(View view) {
        new DownloadUpdate().execute();
    }


    private class DownloadUpdate extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {

            String stringUrl = "http://api.openweathermap.org/data/2.5/forecast?q=chongqing&units=metric&appid=2948450b5d896d9e5acb6ec59ce4e011";

            HttpURLConnection urlConnection = null;
            BufferedReader reader;

            try {
                URL url = new URL(stringUrl);

                // Create the request to get the information from the server, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Mainly needed for debugging
                    Log.d("TAG", line);
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                //The temperature
                String jsonString = buffer.toString();
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonObjectlist = jsonObject.getJSONArray("list");

                JSONObject jsonObjectDay = jsonObjectlist.getJSONObject(0);
                JSONObject jsonObjectMain = jsonObjectDay.getJSONObject("main");
                String temperature = jsonObjectMain.getString("temp");

                // forecast weather and change the image for the next four days...
                JSONArray weatherList;

                jsonObjectDay = jsonObjectlist.getJSONObject(0);
                weatherList = jsonObjectDay.getJSONArray("weather");
                jsonObjectMain = weatherList.getJSONObject(0);
                wText0 = jsonObjectMain.getString("main");
                Log.i("TAG", jsonObjectDay.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ImageView) findViewById(R.id.img_weather_condition)).setImageDrawable(getResources().getDrawable(getUpImg(wText0)));//setImageResource(getWeatherSmallImg(weatherText));
                    }
                });

                jsonObjectDay = jsonObjectlist.getJSONObject(1);
                weatherList = jsonObjectDay.getJSONArray("weather");
                jsonObjectMain = weatherList.getJSONObject(0);
                wText1 = jsonObjectMain.getString("main");
                Log.i("TAG", jsonObjectDay.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ImageView) findViewById(R.id.nextImg1)).setImageResource(getSmallImg(wText1));
                    }
                });


                jsonObjectDay = jsonObjectlist.getJSONObject(2);
                weatherList = jsonObjectDay.getJSONArray("weather");
                jsonObjectMain = weatherList.getJSONObject(0);
                wText2 = jsonObjectMain.getString("main");
                Log.i("TAG", jsonObjectDay.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ImageView) findViewById(R.id.nextImg2)).setImageResource(getSmallImg(wText2));
                    }
                });

                jsonObjectDay = jsonObjectlist.getJSONObject(3);
                weatherList = jsonObjectDay.getJSONArray("weather");
                jsonObjectMain = weatherList.getJSONObject(0);
                wText3 = jsonObjectMain.getString("main");
                Log.i("TAG", jsonObjectDay.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ImageView) findViewById(R.id.nextImg3)).setImageResource(getSmallImg(wText3));
                    }
                });

                jsonObjectDay = jsonObjectlist.getJSONObject(4);
                weatherList = jsonObjectDay.getJSONArray("weather");
                jsonObjectMain = weatherList.getJSONObject(0);
                wText4 = jsonObjectMain.getString("main");
                Log.i("TAG", jsonObjectDay.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ImageView) findViewById(R.id.nextImg4)).setImageResource(getSmallImg(wText4));
                    }
                });
                return temperature;
                //return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String temperature) {
            //Update the temperature displayed
            //Log.d("TAG",temperature);
                Toast.makeText(MainActivity.this, "Information updated.", Toast.LENGTH_SHORT).show();
                ((TextView) findViewById(R.id.todaytemperature)).setText(temperature);
            }
        }
    }


