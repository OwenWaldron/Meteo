package com.owaldron.meteo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public TextView txtCity,txtDate,txtTemp,txtAlt;
    public ImageView imgIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtCity=findViewById(R.id.txtCity);
        txtDate=findViewById(R.id.txtDate);
        txtTemp=findViewById(R.id.txtTemp);
        txtAlt=findViewById(R.id.txtAlt);
        imgIcon=findViewById(R.id.imgIcon);
        afficher("Toronto");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.research, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setQueryHint("Search for a city");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                afficher(query);
                InputMethodManager input = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus()!=null) {
                    input.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void afficher (String city) {
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=d6ece6f897f087217a0bf8869b8b829c&units=metric";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object=response.getJSONObject("main");
                    JSONArray array=response.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);

                    double low =main_object.getDouble("temp_min");
                    double high = main_object.getDouble("temp_max");
                    double temp = main_object.getDouble("temp");
                    Log.d("array",array.toString());
                    String desc = object.getString("description");
                    String icon = object.getString("icon");
                    double humid = main_object.getDouble("humidity");
                    double pressure = main_object.getDouble("pressure");

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMM dd");
                    String formatted_date=simpleDateFormat.format(calendar.getTime());

                    String imageUri = "http://openweathermap.org/img/w/"+icon+".png";
                    Uri myUri = Uri.parse(imageUri);
                    Picasso.with(MainActivity.this).load(myUri).resize(75,75).into(imgIcon);

                    txtCity.setText(city);
                    txtTemp.setText("Temp: "+temp+"° ( "+low+"° to "+high+"° )");
                    txtDate.setText(formatted_date);
                    txtAlt.setText("Humidity: "+humid+"   Pressure: "+pressure+"mb");
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