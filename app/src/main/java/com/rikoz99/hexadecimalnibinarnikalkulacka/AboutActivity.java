package com.rikoz99.hexadecimalnibinarnikalkulacka;


import androidx.annotation.NonNull;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    TextView responseTV;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        this.setBottomMenu(R.id.bottomNavItemAbout);
        this.setToolbar(getString(R.string.navAbout), true);

        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);

        responseTV = findViewById(R.id.aboutAPIresponse);

        goToGitHub = findViewById(R.id.buttonGitHub);
        goToGitHub.setOnClickListener(view -> {
            Intent intentGoToGitHub = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Rikos99"));
            startActivity(intentGoToGitHub);
        });

        checkBakalari = findViewById(R.id.buttonBakalari);
        checkBakalari.setOnClickListener(view -> {
            if(sharedPreferences.getString("username", "").isEmpty())
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
                        .method("POST", RequestBody.create(null, "client_id=ANDR&grant_type=password&username=" + sharedPreferences.getString("username", "") + "&password=" + sharedPreferences.getString("password", "")))
                        .build();
                System.out.println(request.body().toString());
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e)
                    {
                        AboutActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                responseTV.setText(R.string.aboutWrongURL);
                                System.out.println(e.getMessage());
                                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        responseBody = response.body().string();
                        final int responseStatus = response.code();
                        System.out.println(responseStatus);
                        AboutActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {
                                if(responseStatus == 400)
                                {
                                    responseTV.setText(R.string.aboutWrongLogin);
                                }
                                else {
                                    showResponse();
                                }
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
        responseTV.setText(responseBody);

        try {
            JSONObject json = new JSONObject(responseBody);
            if(!json.getString("access_token").isEmpty())
            {
                responseTV.setText(R.string.aboutBakalariWorks);
            }
            else
            {
                responseTV.setText(R.string.aboutBakalariNotWorks);
            }
        }
        catch (JSONException e) {
            Log.d("JSONExc", e.toString());
        }

    }
}