package com.rikoz99.hexadecimalnibinarnikalkulacka;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;

public class AboutActivity extends NavigationActivity {

    Button goToGitHub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        this.setBottomMenu(R.id.bottomNavItemAbout);
        this.setToolbar(getString(R.string.toolbarAbout), true);

        goToGitHub = findViewById(R.id.buttonGitHub);
        goToGitHub.setOnClickListener(view -> {
            Intent intentGoToGitHub = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Rikos99"));
            startActivity(intentGoToGitHub);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_about, menu);
        return true;
    }
}