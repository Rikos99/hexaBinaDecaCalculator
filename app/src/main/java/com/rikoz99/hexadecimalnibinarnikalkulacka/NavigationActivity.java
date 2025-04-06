package com.rikoz99.hexadecimalnibinarnikalkulacka;


import android.content.Intent;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class NavigationActivity extends AppCompatActivity
{
    Toolbar toolbar;
    BottomNavigationView bottomNav;

    protected void setBottomMenu(int selectedItemID)
    {
        Intent intCalc = new Intent(this, MainActivity.class);
        Intent intAbout = new Intent(this, AboutActivity.class);
        Intent intConv = new Intent(this, ConversionActivity.class);

        bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(selectedItemID);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID = item.getItemId();
                if(itemID == R.id.bottomNavItemCalc)
                {
                    startActivity(intCalc);
                }
                else if(itemID == R.id.bottomNavItemConversion)
                {
                    startActivity(intConv);
                }
                else if(itemID == R.id.bottomNavItemAbout)
                {
                    startActivity(intAbout);
                }
                return false;
            }
        });
    }
    protected void setToolbar(String title, boolean showUp)
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(showUp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.options_menu_default, menu);
        return super.onCreateOptionsMenu(menu);
    }
    /*
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.optionsMenuItemSettings)
        {
            Intent intSettings = new Intent(this, SettingsActivity.class);
            startActivity(intSettings);
            //Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

     */
}
