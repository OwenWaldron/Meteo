package com.owaldron.meteo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public TextView txtCity,txtDate,txtTemp,txtAlt;
    public Button btnTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtCity=findViewById(R.id.txtCity);
        txtDate=findViewById(R.id.txtDate);
        txtTemp=findViewById(R.id.txtTemp);
        txtAlt=findViewById(R.id.txtAlt);
        btnTemp=findViewById(R.id.btnTemp);
        afficher("Toronto");
    }

    public void afficher (String city) {
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=d6ece6f897f087217a0bf8869b8b829c&units=metric";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object=response.getJSONObject("main");
                    JSONArray array=response.getJSONArray("weather");
                    Log.d("TEMP", "HELLO CAN YOU HEAR ME");
                    Log.d("array",array.toString());
                    txtCity.setText(array.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("IT CAUGHT", "ERROR IT CUAGHT");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERRORRESPONS","EROORO RERADEIOFORG HI"+error);
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}