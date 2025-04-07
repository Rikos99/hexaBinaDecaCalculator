package com.rikoz99.hexadecimalnibinarnikalkulacka;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends NavigationActivity {

    //TODO možnosti vypnutí a zapnutí historie, výchozí módy v kalkulačce a převaděči
    Button Confirm;
    EditText URL, Username, Password;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.setToolbar(getString(R.string.navSettings), true);

        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
        Confirm = findViewById(R.id.settingsConfirm);
        URL = findViewById(R.id.settingsURL);
        Username = findViewById(R.id.settingsUsername);
        Password = findViewById(R.id.settingsPassword);

        //editor.putString("nastaveni", "hodnota");
        //editor.apply(); //async uložení; synchronní metodou commit

        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        //získání hodnoty z preferences: sharedPreferences.getString("nastaveni", "hodnota"));

        URL.setText(sharedPreferences.getString("url", "https://example.com/"));
        Username.setText(sharedPreferences.getString("username", "ItsAMeMario"));

        Confirm.setOnClickListener(v -> {
            if(!URL.getText().toString().isEmpty() && !Username.getText().toString().isEmpty() && !Password.getText().toString().isEmpty())
            {
                editor.putString("url", URL.getText().toString());
                editor.putString("username", Username.getText().toString());
                editor.putString("password", Password.getText().toString());
                editor.apply();
                finish();
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