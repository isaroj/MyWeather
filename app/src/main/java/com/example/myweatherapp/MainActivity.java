package com.example.myweatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private EditText editText;
    private TextView textView;
    private LinearLayout linearLayout;
    //Take API KEY FROM openweathermap.org
    String BaseUrl="https://api.openweathermap.org/data/2.5/weather?q=";
    String API="&appid=GIVE YOUR API KEY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.show);
        editText=findViewById(R.id.city);
        textView=findViewById(R.id.result);
        final RelativeLayout relativeLayout=findViewById(R.id.layout);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editText.getText().toString())){
                    editText.setError("Enter city name!");
                    return;
                }
                String myURL = BaseUrl + editText.getText().toString() + API;
                textView.setText("");

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                Log.i("JSON", "JSON: " + jsonObject);

                                try {
                                    String info = jsonObject.getString("weather");
                                    Log.i("INFO", "INFO: " + info);
                                    String coord= jsonObject.getString("coord");
                                    String main=jsonObject.getString("main");

                                    JSONObject ob1=new JSONObject(main);
                                    String temp=String.format("%.2f",Double.parseDouble(ob1.getString("temp"))-273.15).toString();
                                    String temp_min=String.format("%.2f",Double.parseDouble(ob1.getString("temp_min"))-273.15).toString();
                                    String temp_max=String.format("%.2f",Double.parseDouble(ob1.getString("temp_max"))-273.15).toString();
                                    textView.append("Temp: "+temp+"°C \n");
                                    textView.append("Max_Temp: "+temp_max+"°C \n");
                                    textView.append("Min_Temp: "+temp_min+"°C \n");
                                    JSONObject ob=new JSONObject(coord);
                                    String lon=ob.getString("lon");
                                    textView.append("Longitude: "+lon);
                                    String lat=ob.getString("lat");
                                    textView.append("\nLatitude:"+lat+"\n");

                                    JSONArray ar = new JSONArray(info);

                                    for (int i = 0; i < ar.length(); i++) {
                                        JSONObject parObj = ar.getJSONObject(i);

                                        String myWeather = parObj.getString("main");
                                        String myDescription =parObj.getString("description");
                                        textView.append("Weather: "+myWeather);
                                        textView.append("\nDescription:"+myDescription);
                                        switch (myWeather)
                                        {
                                            case "Clouds":
                                            {
                                                relativeLayout.setBackgroundResource(R.drawable.clouds);
                                                break;
                                            }
                                            case "Sunny":
                                            {
                                                relativeLayout.setBackgroundResource(R.drawable.sunny);
                                                break;
                                            }
                                            case "Haze":
                                            {
                                                relativeLayout.setBackgroundResource(R.drawable.haze);
                                                break;
                                            }
                                            case "Drizzle":
                                            {
                                                relativeLayout.setBackgroundResource(R.drawable.drizzle);
                                                break;
                                            }
                                            case "Rain":
                                            {
                                                relativeLayout.setBackgroundResource(R.drawable.rainy);
                                                break;
                                            }
                                            case "Clear":
                                            {
                                                relativeLayout.setBackgroundResource(R.drawable.clear);
                                                break;
                                            }

                                        }

                                        Log.i("ID", "ID: " + parObj.getString("id"));
                                        Log.i("MAIN", "MAIN: " + parObj.getString("main"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                textView.setText("Sorry! Weather data for your city is not available!");

                            }
                        }
                );
                MySingleton.getInstance(MainActivity.this).addToRequestQue(jsonObjectRequest);
            }
        });

    }
}




