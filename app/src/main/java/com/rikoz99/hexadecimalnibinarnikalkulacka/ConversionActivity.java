package com.rikoz99.hexadecimalnibinarnikalkulacka;

import static com.rikoz99.hexadecimalnibinarnikalkulacka.R.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;

public class ConversionActivity extends NavigationActivity {

    Spinner convModeFrom_SPIN, convModeTo_SPIN;
    EditText input_ET;
    Button convert_BTN;
    DbHelper dbHelper;
    ListView history;


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
        convert_BTN = findViewById(id.btnConvert);
        history = findViewById(R.id.listViewHistory);

        dbHelper = new DbHelper(getApplicationContext());

        //Nastavení spinnerů

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

        naplnitHistorii();

        convModeFrom_SPIN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
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
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        //Výpočet
        convert_BTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                //zkontrolovat validitu inputu
                try {
                    File path = getFilesDir();
                    File file = new File(path, getString(R.string.historyFile));

                    // vzít input, převést ho na decimální
                    int input = 0;
                    String result = null, resultToHistory = null;
                    if (convModeFrom_SPIN.getSelectedItemPosition() == 0) //Dec
                    {
                        input = Integer.parseInt(input_ET.getText().toString());
                        resultToHistory = "(Dec) ";
                    }
                    else if (convModeFrom_SPIN.getSelectedItemPosition() == 1) //Bin
                    {
                        input = Integer.parseInt(input_ET.getText().toString(), 2);
                        resultToHistory = "(Bin) ";
                    }
                    else if (convModeFrom_SPIN.getSelectedItemPosition() == 2) //Hex
                    {
                        input = Integer.parseInt(input_ET.getText().toString(), 16);
                        resultToHistory = "(Hex) ";
                    }
                    resultToHistory += input_ET.getText() + " -> ";

                    //decimální input převést na zvolený typ

                    if (convModeTo_SPIN.getSelectedItemPosition() == 0) //Dec
                    {
                        result = Integer.toString(input);
                        resultToHistory += "(Dec) ";
                    } else if (convModeTo_SPIN.getSelectedItemPosition() == 1) //Bin
                    {
                        result = Integer.toString(input, 2);
                        resultToHistory += "(Bin) ";
                    } else if (convModeTo_SPIN.getSelectedItemPosition() == 2) //Hex
                    {
                        result = Integer.toString(input, 16);
                        resultToHistory += "(Hex) ";
                    }
                    resultToHistory += result + "\n";

                    insertNewConversion(input_ET.getText().toString(), convModeFrom_SPIN.getSelectedItemPosition(), convModeTo_SPIN.getSelectedItemPosition());

                    naplnitHistorii();
                }
                catch (NumberFormatException e)
                {
                    Toast.makeText(ConversionActivity.this, getString(R.string.errorConvEnterValidValue), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void insertNewConversion(String number, int from, int to)
    {
        ConversionEntryModel conversion = new ConversionEntryModel(-1, number, from, to);
        long conversionId = dbHelper.insertConversion(conversion);
        Toast.makeText(ConversionActivity.this, "Záznam úspěšně vložen do DB. ID: " + conversionId, Toast.LENGTH_SHORT).show();
    }

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
        ArrayList<ConversionEntryModel> historyItems = dbHelper.getAllConversions();

        ArrayAdapter<ConversionEntryModel> adapter = new ArrayAdapter<ConversionEntryModel>(
                this,
                R.layout.item_layout,
                R.id.textViewFrom,
                historyItems
        )
        {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);

                TextView from = view.findViewById(R.id.textViewFrom);
                TextView number = view.findViewById(R.id.textViewNumber);
                TextView to = view.findViewById(R.id.textViewTo);
                TextView result = view.findViewById(R.id.textViewResult);

                //getFrom a getTo vrací něco jako ID konverzí => switch

                String fromStr, toStr;
                switch (historyItems.get(position).getFrom())
                {
                    case 0 -> fromStr = "Dec";
                    case 1 -> fromStr = "Bin";
                    case 2 -> fromStr = "Hex";
                    default -> fromStr = "";
                }
                switch (historyItems.get(position).getTo())
                {
                    case 0 -> toStr = "Dec";
                    case 1 -> toStr = "Bin";
                    case 2 -> toStr = "Hex";
                    default -> toStr = "";
                }
                from.setText(fromStr);
                number.setText(String.valueOf(historyItems.get(position).getNumber()));
                to.setText(toStr);
                result.setText(String.valueOf(historyItems.get(position).getResult()));

                return view;
            }
        };
        history.setAdapter(adapter);

        /*
                @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);

                text1.setText(friends.get(position).getName());
                text2.setText(Integer.toString(friends.get(position).getFriendFrom()));
                return view;
            }

         */

        //System.out.println(historyItems.toString());

        //adapter.addAll(historyItems.toString());
    }

    public void vymazatHistorii()
    {
        history.removeAllViewsInLayout();
        dbHelper.deleteAllConversions();
    }
}