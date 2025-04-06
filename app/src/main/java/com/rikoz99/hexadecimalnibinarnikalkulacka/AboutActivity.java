package com.rikoz99.hexadecimalnibinarnikalkulacka;


import static okhttp3.MultipartBody.FORM;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class AboutActivity extends NavigationActivity
{
    Button goToGitHub, checkBakalari;
    String responseBody;
    TextView response;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        this.setBottomMenu(R.id.bottomNavItemAbout);
        this.setToolbar(getString(R.string.navAbout), true);

        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);

        response = findViewById(R.id.aboutAPIresponse);

        goToGitHub = findViewById(R.id.buttonGitHub);
        goToGitHub.setOnClickListener(view -> {
            Intent intentGoToGitHub = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Rikos99"));
            startActivity(intentGoToGitHub);
        });

        checkBakalari = findViewById(R.id.buttonBakalari);
        checkBakalari.setOnClickListener(view -> {
            if(sharedPreferences.getString("login", "").isEmpty())
            {
                Intent goToSettings = new Intent(this, SettingsActivity.class);
                startActivity(goToSettings);
            }
            else
            {
                // API request na Bakaláře
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .url(sharedPreferences.getString("url", "") + "api/login")
                        .post(RequestBody.create(FORM, "client_id=ANDR&grant_type=password&username=" + sharedPreferences.getString("username", "") + "&password=" + sharedPreferences.getString("password", "")))
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        responseBody = response.body().string();
                        AboutActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showResponse();
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.options_menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int itemId = item.getItemId();

        if(itemId == R.id.optionsMenuItemSettings)
        {
            Intent goToSettings = new Intent(this, SettingsActivity.class);
            startActivity(goToSettings);
        }
        return super.onOptionsItemSelected(item);
    }
    private void showResponse() {
        response.setText(responseBody);

        try {
            JSONObject json = new JSONObject(responseBody);
            Double temperature = json.getJSONObject("properties").getJSONArray("timeseries").getJSONObject(0).getJSONObject("data").getJSONObject("instant").getJSONObject("details").getDouble("air_temperature");

            response.setText(Double.toString(temperature));
        }
        catch (JSONException e) {
            Log.d("JSONExc", e.toString());
        }

    }
}