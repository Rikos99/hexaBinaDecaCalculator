package com.rikoz99.hexadecimalnibinarnikalkulacka;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    //TODO možnosti vypnutí a zapnutí historie, výchozí módy v kalkulačce a převaděči

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();

        //editor.putString("nastaveni", "hodnota");
        //editor.apply(); //async uložení; synchronní metodou commit

        SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        //získání hodnoty z preferences: sharedPreferences.getString("nastaveni", "hodnota"));
    }
}