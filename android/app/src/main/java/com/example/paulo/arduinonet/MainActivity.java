package com.example.paulo.arduinonet;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.paulo.arduinonet.R.id.luminosidade_text_view;
import static com.example.paulo.arduinonet.R.id.temperatura_text_view;
import static com.example.paulo.arduinonet.R.id.tensao_text_view;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Button buttonAtualizar = (Button) findViewById(button);
        FetchArduinoTask arduinoTask = new FetchArduinoTask();
        while(buttonAtualizar.hasOnClickListeners()){
            arduinoTask.execute();
        }*/
    }

    public void updateMeasurement(View view){
        FetchArduinoTask arduinoTask = new FetchArduinoTask();
        arduinoTask.execute();
    }

    public class FetchArduinoTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchArduinoTask.class.getSimpleName();

        private String[] getMeasureDataFromJson(String measureJsonStr) throws JSONException{

            JSONObject measuresJson = new JSONObject(measureJsonStr);
            String[] measuresStr = new String[3];
            measuresStr[0] = measuresJson.getString("voltage");
            measuresStr[1] = measuresJson.getString("temperature");
            measuresStr[2] = measuresJson.getString("luminosity");

            return measuresStr;
        }

        @Override
        protected String[] doInBackground(String... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String measurementJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                //final String QUERY_PARAM = "sensor";
                final String ARDUINO_CONNECTION_BASE_URL =
                        "http://192.168.0.117:8081/prjExemploRest1/echo/readData";

                Uri builtUri = Uri.parse(ARDUINO_CONNECTION_BASE_URL).buildUpon()
                        /*.appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                        .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                        */.build();

                URL url = new URL(builtUri.toString());
                Log.v("URL enviada", url.toString());

                // Create the request to OpenWeatherMap, and open the connection
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
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                measurementJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMeasureDataFromJson(measurementJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                TextView textView = (TextView) findViewById(tensao_text_view);
                textView.setText(result[0] + " V");

                textView = (TextView) findViewById(temperatura_text_view);
                textView.setText(result[1] + " ºC");

                textView = (TextView) findViewById(luminosidade_text_view);
                textView.setText(result[2] + " V");
                // New data is back from the server.  Hooray!
            }
        }
    }
}
