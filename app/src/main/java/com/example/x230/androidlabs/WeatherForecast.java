package com.example.x230.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecast extends Activity {
    protected static final String ACTIVITY_NAME="WeatherForecast";

    TextView wSpeed;
    TextView minT;
    TextView maxT;
    TextView currentT;
    ImageView picture;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        wSpeed = findViewById(R.id.windSpeed);
        minT = findViewById(R.id.minTemp);
        maxT = findViewById(R.id.maxTemp);
        currentT = findViewById(R.id.currentTemp);
        picture = findViewById(R.id.weatherImage);

        progress = findViewById(R.id.forecastProgressBar);
        progress.setVisibility(View.VISIBLE);

       ForecastQuery forecast = new ForecastQuery();
       forecast.execute();
    }

    private class ForecastQuery extends AsyncTask<String,Integer,String>{
        String windSpeed, minTemp, maxTemp, currentTemp, iconName;
        Bitmap weatherPicture;

        protected String doInBackground(String ... args) {
            Log.i(ACTIVITY_NAME,"In doInBackground()");
            URL url = null;
            try {
                url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");
                Log.i(ACTIVITY_NAME,"succeeded in accessing website");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser parser = factory.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(conn.getInputStream(),"UTF-8");

                String value;
                while(parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                    switch(parser.getEventType()) {
                        case XmlPullParser.START_TAG:
                            String name=parser.getName();
                            if(name.equals("temperature")) {
                                minTemp = parser.getAttributeValue(null, "min");
                                this.publishProgress(25);
                                maxTemp = parser.getAttributeValue(null, "max");
                                this.publishProgress(50);
                                currentTemp = parser.getAttributeValue(null, "value");
                                this.publishProgress(75);
                            }
                            else if(name.equals("speed")) {
                                windSpeed = parser.getAttributeValue(null, "value");

                            }
                            else if(name.equals("weather")) {
                                iconName = parser.getAttributeValue(null, "icon");
                            }
                        case XmlPullParser.TEXT:
                            break;
                    }
                    parser.next();
                }
                Log.i(ACTIVITY_NAME,"finished parsing XML");

                URL bitmapURL = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                HttpURLConnection bmpConn = null;

                if(fileExistance(iconName+".png")){
                    Log.i(ACTIVITY_NAME,"image exists on local storage");
                    FileInputStream fis = null;
                    try{ fis=openFileInput(iconName+".png");}
                    catch(FileNotFoundException e){e.printStackTrace(); Log.i(ACTIVITY_NAME,"File not found locally");}
                    weatherPicture = BitmapFactory.decodeStream(fis);
                    Log.i(ACTIVITY_NAME, "Image loaded locally: "+iconName);
                }else {
                    try {
                        bmpConn = (HttpURLConnection) bitmapURL.openConnection();
                        bmpConn.connect();
                        int responseCode = bmpConn.getResponseCode();
                        if (responseCode == 200) {
                            weatherPicture = BitmapFactory.decodeStream(bmpConn.getInputStream());
                            FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                            weatherPicture.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                            outputStream.flush();
                            outputStream.close();
                            Log.i(ACTIVITY_NAME, "Image loaded from web-source: "+iconName);
                        }
                    } catch (Exception e) {
                    } finally {
                        if (bmpConn != null) bmpConn.disconnect();
                        this.publishProgress(100);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            Log.i(ACTIVITY_NAME,"end of do in background");
            return "";

        }
        protected void onProgressUpdate(Integer  value){
            progress.setProgress(value);
            progress.setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(String result){
            Log.i(ACTIVITY_NAME,"onPostExecute");

            wSpeed.setText(windSpeed);
            minT.setText(minTemp);
            maxT.setText(maxTemp);
            currentT.setText(currentTemp);
            picture.setImageBitmap(weatherPicture);

            Log.i(ACTIVITY_NAME,"setting the values");

            progress.setVisibility(View.INVISIBLE);
        }

        public boolean fileExistance(String fname){
            Log.i(ACTIVITY_NAME,"In fileExistance()");
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

    }
}
