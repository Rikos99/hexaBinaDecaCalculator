package com.rikoz99.hexadecimalnibinarnikalkulacka;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
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

        try {
            FileInputStream stream = new FileInputStream(file);
            stream.read(bytes);
            stream.close();
        } catch (IOException e) {
            Toast.makeText(HistoryActivity.this, getString(R.string.errorHistoryReadFromFile), Toast.LENGTH_SHORT).show();
        }

        history.setText(new String(bytes));
    }

    private boolean vymazatHistorii()
    {
        File file = new File(getFilesDir(), getString(R.string.historyFile));
        return file.delete();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_history, menu);
        return true;
    }
}