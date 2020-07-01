package com.vikdev.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    Button mButton;
    EditText city;
    TextView result;
    private RequestQueue mRequestQueue;
    private ConstraintLayout mConstraintLayout;

    //    http://api.openweathermap.org/data/2.5/weather?q=ghaziabad&appid=b4232fb4ecefcadd0a024f247d0f9c23
    String baseURL = "https://api.openweathermap.org/data/2.5/weather?q=";
    String API = "&appid=b4232fb4ecefcadd0a024f247d0f9c23";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRequestQueue = MySingleton.getInstance(this).getRequest();

        mButton = findViewById(R.id.button);
        city = findViewById(R.id.getCity);
        result = findViewById(R.id.result);
        mConstraintLayout = findViewById(R.id.layout);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText("");
                showData();
            }
        });
    }

    public void showData() {
        String name = city.getText().toString();
        name = name.replaceAll("[^a-zA-Z0-9]", "");
        String myURL;
        if (TextUtils.isEmpty(name)) {
            city.setError("Please input city name");
        } else {
            myURL = baseURL + name + API;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, myURL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String description = "";
                    String main = "";
                    try {
                        JSONArray jsonArray = response.getJSONArray("weather");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            description = jsonObject.getString("description").toUpperCase();
                            main = jsonObject.getString("main").toLowerCase();
                            changeImage(main);
                        }
                        JSONObject jsonObject = response.getJSONObject("main");
                        String temp = jsonObject.getString("temp");
                        Double convert = Double.parseDouble(temp);
                        DecimalFormat df = new DecimalFormat("##.##");
                        convert = (convert - 273.15);
                        String celsius = String.valueOf(df.format(convert)) + " °C";

                        result.append(description + "\n\n" + "Temp: " + celsius + "\n");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
            mRequestQueue.add(request);
        }
    }

    private void changeImage(String weather) {
        Log.d("Temp", weather);

        switch (weather) {
            case "haze":
                mConstraintLayout.setBackgroundResource(R.drawable.haze);
                result.setTextColor(Color.WHITE);
                break;
            case "clouds":
                mConstraintLayout.setBackgroundResource(R.drawable.cloud);
                break;
            case "clear":
                mConstraintLayout.setBackgroundResource(R.drawable.clear);
                result.setTextColor(Color.WHITE);
                break;
            case "fog":
                mConstraintLayout.setBackgroundResource(R.drawable.fog);
                break;
            case "snow":
                mConstraintLayout.setBackgroundResource(R.drawable.snow);
                result.setTextColor(Color.WHITE);
                break;
            case "rain":
                mConstraintLayout.setBackgroundResource(R.drawable.rain);
                result.setTextColor(Color.WHITE);
                break;
            case "thunderstorm":
                mConstraintLayout.setBackgroundResource(R.drawable.thunderstorm);
                result.setTextColor(Color.WHITE);
                break;
            default:
                mConstraintLayout.setBackgroundResource(R.drawable.winter);
        }
    }
}
