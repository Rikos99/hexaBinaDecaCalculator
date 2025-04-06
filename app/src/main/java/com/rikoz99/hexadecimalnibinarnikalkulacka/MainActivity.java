package com.rikoz99.hexadecimalnibinarnikalkulacka;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends NavigationActivity
{

    EditText input1_ET, input2_ET;
    TextView history;
    Spinner mode_SPIN; //0 - decimal, 1 - binary, 2 - hexadecimal
    Button compute_BTN;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setBottomMenu(R.id.bottomNavItemCalc);
        this.setToolbar(getString(R.string.navCalc), false);


        input1_ET =     findViewById(R.id.inputDEC1);
        input2_ET =     findViewById(R.id.inputDEC2);
        mode_SPIN =     findViewById(R.id.spinner_mode);
        compute_BTN =   findViewById(R.id.compute_BTN);
        history =       findViewById(R.id.history_TV);


        //Nastavení drop down menu

        //https://developer.android.com/develop/ui/views/components/spinner

        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.spinner_mode,
                android.R.layout.simple_spinner_item
        );
        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        mode_SPIN.setAdapter(adapter);

        mode_SPIN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) //nastavení hints current modu do inputů
            {
                if(mode_SPIN.getSelectedItemPosition() == 0)
                {
                    input1_ET.setHint(getString(R.string.inputDEC1));
                    input2_ET.setHint(getString(R.string.inputDEC2));
                }
                else if(mode_SPIN.getSelectedItemPosition() == 1)
                {
                    input1_ET.setHint(getString(R.string.inputBIN1));
                    input2_ET.setHint(getString(R.string.inputBIN2));
                }
                else
                {
                    input1_ET.setHint(getString(R.string.inputHEX1));
                    input2_ET.setHint(getString(R.string.inputHEX2));
                }

                input1_ET.setText("");
                input2_ET.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        compute_BTN.setOnClickListener(v -> //výpočet hodnot & nastavení výsledku do outputu
        {
            try
            {
                String result, resultToHistory;
                File path = getFilesDir();
                File file = new File(path, getString(R.string.historyFile));
                if(mode_SPIN.getSelectedItemPosition() == 0) //DEC
                {
                    double dec1, dec2;

                    dec1 = Double.parseDouble(input1_ET.getText().toString());
                    dec2 = Double.parseDouble(input2_ET.getText().toString());
                    result = String.valueOf(dec1 + dec2);
                    resultToHistory = getString(R.string.decimal) + ": ";
                }
                else if(mode_SPIN.getSelectedItemPosition() == 1) //BIN
                {
                    int bin1, bin2;

                    bin1 = Integer.parseInt(input1_ET.getText().toString(), 2);
                    bin2 = Integer.parseInt(input2_ET.getText().toString(), 2);
                    result = Integer.toBinaryString(bin1 + bin2);
                    resultToHistory = getString(R.string.binary) + ": ";
                }
                else //HEX
                {
                    int hex1, hex2;

                    hex1 = Integer.parseInt(input1_ET.getText().toString(), 16);
                    hex2 = Integer.parseInt(input2_ET.getText().toString(), 16);
                    result = Integer.toString(hex1 + hex2, 16);
                    resultToHistory = getString(R.string.hexadecimal) + ": ";
                }

                resultToHistory += input1_ET.getText().toString() + " + " + input2_ET.getText().toString() + " = " + result + "\n";

                try
                {
                    FileWriter fileWriter = new FileWriter(file, true); // pro přidávání textu na konec souboru // druhý parametr = append (boolean)
                    fileWriter.write(resultToHistory);
                    fileWriter.close();
                } catch (IOException e) { Toast.makeText(MainActivity.this, getString(R.string.errorMainWriteToFile), Toast.LENGTH_SHORT).show(); }

                CharSequence tmp = history.getText();
                history.setText(resultToHistory);
                history.append(tmp);
            }
            catch (NumberFormatException e)
            {
                Toast.makeText(MainActivity.this,  getString(R.string.errorEnterBothValues), Toast.LENGTH_SHORT).show();
            }
        });

        naplnitHistorii();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_default, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("input1", input1_ET.getText().toString());
        outState.putString("input2", input2_ET.getText().toString());

        super.onSaveInstanceState(outState);
    }
    /*
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        input1_ET.setText(savedInstanceState.getString("input1"));
        input2_ET.setText(savedInstanceState.getString("input2"));
        result_TV.setText(savedInstanceState.getString("vysledek"));

        super.onRestoreInstanceState(savedInstanceState);
    }
     */

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.optionsMenuItemClear)
        {
            vymazatHistorii();
        }
        else if(itemId == R.id.optionsMenuItemSettings)
        {
            Intent goToSettings = new Intent(this, SettingsActivity.class);
            startActivity(goToSettings);
        }
        return super.onOptionsItemSelected(item);
    }

    public void naplnitHistorii()
    {

        File path = getFilesDir();
        File file = new File(path, getString(R.string.historyFile));

        if(file.exists())
        {
            int length = (int) file.length();

            byte[] bytes = new byte[length];

            try
            {
                FileInputStream stream = new FileInputStream(file);
                stream.read(bytes);
                stream.close();
            }
            catch (IOException e) { Toast.makeText(MainActivity.this, getString(R.string.errorHistoryReadFromFile), Toast.LENGTH_SHORT).show(); }

            history.setText(new String(bytes));
        }
        else
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException e) { throw new RuntimeException(e); }
        }
    }

    public void vymazatHistorii()
    {
        File file = new File(getFilesDir(), getString(R.string.historyFile));
        try
        {
            new FileWriter(file, false).write("");
            history.setText("");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}