package com.example.jona.asistentevirtual.Models;

import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherModel {

    private static final String APIXU_API_CLIENT = "0b65639864ea4bbdb93195540182604";
    private View view;
    private RequestQueue queue;
    private String resultCurrentWeather;

    public WeatherModel(View view) {
        this.view = view;
        queue = Volley.newRequestQueue(this.view.getContext());
    }

    public void CurrentWeather(final WeatherCallback weatherCallback) {

        String url = "https://api.apixu.com/v1/current.json?key=" + APIXU_API_CLIENT + "&q=Latacunga&lang=es";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObjectLocation = response.getJSONObject("location");
                    String locationName = jsonObjectLocation.getString("name"); // Nombre de la ciudad.

                    JSONObject jsonObjectCurrent = response.getJSONObject("current");
                    String humidity = jsonObjectCurrent.getInt("humidity") + "%"; // Porcentaje de la humedad.
                    String degreesC = Math.round(jsonObjectCurrent.getDouble("temp_c")) + "°C";
                    String degreesF = Math.round(jsonObjectCurrent.getDouble("temp_f")) + "°F";
                    String windPerHour = jsonObjectCurrent.getString("wind_kph") + "km/h"; // Velocidad del viento en kilómetros por hora.
                    String currentCondition = jsonObjectCurrent.getJSONObject("condition").getString("text"); // Condición en la que se encuentra el clima.

                    // Crear la respuesta.
                    resultCurrentWeather = "La ciudad de " + locationName + " se encuentra " + currentCondition + " con una humedad del "
                            + humidity + ". Además, la velocidad del viento ésta a " + windPerHour + " con una temperatura de "
                            + degreesC + " o " + degreesF + ".";

                    weatherCallback.getResponseWeather(resultCurrentWeather);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }

    public interface WeatherCallback {
        void getResponseWeather(String response);
    }
}
