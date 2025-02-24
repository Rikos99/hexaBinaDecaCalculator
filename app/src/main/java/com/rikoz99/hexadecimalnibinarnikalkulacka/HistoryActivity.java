package com.rikoz99.hexadecimalnibinarnikalkulacka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class HistoryActivity extends NavigationActivity {

    TextView history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        this.setBottomMenu(R.id.bottomNavItemHistory);
        this.setToolbar(getString(R.string.navHistory), true);

        history = findViewById(R.id.history_TV);

        File path = getFilesDir();
        File file = new File(path, getString(R.string.historyFile));

        int length = (int) file.length();

        byte[] bytes = new byte[length];

        try
        {
            FileInputStream stream = new FileInputStream(file);
            stream.read(bytes);
            stream.close();
        }
        catch (IOException e) { Toast.makeText(HistoryActivity.this, getString(R.string.errorHistoryReadFromFile), Toast.LENGTH_SHORT).show(); }

        history.setText(new String(bytes));
    }

    public boolean vymazatHistorii()
    {
        File file = new File(getFilesDir(), getString(R.string.historyFile));
        try
        {
            new FileWriter(file, false).write("");
            history.setText("");
            return true;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_default, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.optionsMenuItemClear)
        {
            vymazatHistorii();
        }
        return super.onOptionsItemSelected(item);
    }
}