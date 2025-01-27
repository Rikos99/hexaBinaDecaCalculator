package com.rikoz99.hexadecimalnibinarnikalkulacka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends NavigationActivity {

    EditText input1_ET, input2_ET;
    TextView result_TV;
    Spinner mode_SPIN; //0 - decimal, 1 - binary, 2 - hexadecimal
    Button compute_BTN, restore_BTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setBottomMenu(R.id.bottomNavItemCalc);
        this.setToolbar(getString(R.string.navCalc), false);


        input1_ET = findViewById(R.id.inputDEC1);
        input2_ET = findViewById(R.id.inputDEC2);
        result_TV = findViewById(R.id.textView);
        mode_SPIN = findViewById(R.id.spinner_mode);
        compute_BTN = findViewById(R.id.compute_BTN);
        restore_BTN = findViewById(R.id.restoreMain_BTN);

        if(savedInstanceState != null)
        {
            result_TV.setText(savedInstanceState.getString("vysledek"));
        }

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
                result_TV.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        compute_BTN.setOnClickListener(v -> //výpočet hodnot & nastavení výsledku do outputu
        {
            try
            {
                SharedPreferences.Editor editor = getSharedPreferences("preferences", MODE_PRIVATE).edit();
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


                editor.putString("input1", input1_ET.getText().toString());
                editor.putString("input2", input2_ET.getText().toString());
                editor.apply(); //async uložení; synchronní metodou commit

                resultToHistory += input1_ET.getText().toString() + " + " + input2_ET.getText().toString() + " = " + result + "\n";

                try {
                    FileWriter fileWriter = new FileWriter(file, true); // pro přidávání textu na konec souboru // druhý parametr = append (boolean)
                    fileWriter.write(resultToHistory);
                    fileWriter.close();
                } catch (IOException e) {
                    Toast.makeText(MainActivity.this, getString(R.string.errorMainWriteToFile), Toast.LENGTH_SHORT).show();
                }

                result_TV.setText(result);
            }
            catch (NumberFormatException e)
            {
                Toast.makeText(MainActivity.this,  getString(R.string.errorEnterBothValues), Toast.LENGTH_SHORT).show();
            }
        });

        restore_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
                input1_ET.setText(sharedPreferences.getString("input1", ""));
                input2_ET.setText(sharedPreferences.getString("input2", ""));
            }
        });
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
        outState.putString("vysledek", result_TV.getText().toString());

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
}