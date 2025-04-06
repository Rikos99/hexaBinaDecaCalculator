package com.rikoz99.hexadecimalnibinarnikalkulacka;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends NavigationActivity {

    //TODO možnosti vypnutí a zapnutí historie, výchozí módy v kalkulačce a převaděči
    Button Confirm;
    EditText URL, Email, Password;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.setToolbar(getString(R.string.navSettings), true);

        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();

        //editor.putString("nastaveni", "hodnota");
        //editor.apply(); //async uložení; synchronní metodou commit

        SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        //získání hodnoty z preferences: sharedPreferences.getString("nastaveni", "hodnota"));

        Confirm.setOnClickListener(v -> {
            if(!URL.getText().toString().isEmpty() && !Email.getText().toString().isEmpty() && !Password.getText().toString().isEmpty())
            {
                editor.putString("url", URL.getText().toString());
                editor.putString("username", Email.getText().toString());
                editor.putString("password", Password.getText().toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.options_menu_settings, menu);
        return true;
    }
}