package com.rikoz99.hexadecimalnibinarnikalkulacka;

import static com.rikoz99.hexadecimalnibinarnikalkulacka.R.*;

import android.os.Bundle;
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

public class ConversionActivity extends NavigationActivity {

    Spinner convModeFrom_SPIN, convModeTo_SPIN;
    EditText input_ET;
    TextView result_TV;
    Button convert_BTN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_conversion);
        this.setBottomMenu(R.id.bottomNavItemConversion);
        this.setToolbar(getString(R.string.navConv), true);


        //Nastavení 2 drop down menu

        //https://developer.android.com/develop/ui/views/components/spinner


        convModeFrom_SPIN = findViewById(R.id.convertSpinnerModeFrom);
        convModeTo_SPIN = findViewById(R.id.convertSpinnerModeTo);
        input_ET = findViewById(R.id.etCisloConv);
        result_TV = findViewById(R.id.textViewConvert);
        convert_BTN = findViewById(id.btnConvert);


        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter<CharSequence> adapterFrom = ArrayAdapter.createFromResource(
                this,
                array.convertSpinner_mode,
                android.R.layout.simple_spinner_item
        );

        // Specify the layout to use when the list of choices appears.
        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        convModeTo_SPIN.setAdapter(adapterFrom);

        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter<CharSequence> adapterTo = ArrayAdapter.createFromResource(
                this,
                array.convertSpinner_mode,
                android.R.layout.simple_spinner_item
        );
        // Specify the layout to use when the list of choices appears.
        adapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        convModeFrom_SPIN.setAdapter(adapterTo);


        convModeFrom_SPIN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0) //Dec
                {
                    input_ET.setHint(getString(string.convInputDEC));
                }
                else if(i == 1) //Bin
                {
                    input_ET.setHint(getString(R.string.convInputBIN));
                }
                else if(i == 2) //Hex
                {
                    input_ET.setHint(getString(R.string.convInputHEX));
                }

                input_ET.setText("");
                result_TV.setText("");
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        convert_BTN.setOnClickListener(new View.OnClickListener() { //Výpočet
            @Override
            public void onClick(View view) {
                //zkontrolovat validitu inputu
                try
                {
                    File path = getFilesDir();
                    File file = new File(path, getString(R.string.historyFile));

                    // vzít input, převést ho na decimální
                    int input=0;
                    String result = null, resultToHistory = null;
                    if(convModeFrom_SPIN.getSelectedItemPosition() == 0) //Dec
                    {
                        input = Integer.parseInt(input_ET.getText().toString());
                        resultToHistory = "(Dec) ";
                    }
                    else if(convModeFrom_SPIN.getSelectedItemPosition() == 1) //Bin
                    {
                        input = Integer.parseInt(input_ET.getText().toString(), 2);
                        resultToHistory = "(Bin) ";
                    }
                    else if (convModeFrom_SPIN.getSelectedItemPosition() == 2) //Hex
                    {
                        input = Integer.parseInt(input_ET.getText().toString(), 16);
                        resultToHistory = "(Hex) ";
                    }
                    resultToHistory += input + " -> ";

                    //decimální input převést na zvolený typ

                    if(convModeTo_SPIN.getSelectedItemPosition() == 0) //Dec
                    {
                        result = Integer.toString(input);
                        resultToHistory += "(Dec) ";
                    }
                    else if(convModeTo_SPIN.getSelectedItemPosition() == 1) //Bin
                    {
                        result = Integer.toString(input, 2);
                        resultToHistory += "(Bin) ";
                    }
                    else if (convModeTo_SPIN.getSelectedItemPosition() == 2) //Hex
                    {
                        result = Integer.toString(input, 16);
                        resultToHistory += "(Hex) ";
                    }
                    resultToHistory += result + "\n";
                    result_TV.setText(result);

                    try {
                        FileWriter fileWriter = new FileWriter(file, true); // pro přidávání textu na konec souboru // druhý parametr = append (boolean)
                        fileWriter.write(resultToHistory);
                        fileWriter.close();
                    } catch (IOException e) {
                        Toast.makeText(ConversionActivity.this, getString(R.string.errorMainWriteToFile), Toast.LENGTH_SHORT).show();
                    }
                }
                catch (NumberFormatException e)
                {
                    Toast.makeText(ConversionActivity.this, getString(R.string.errorConvEnterValidValue), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}