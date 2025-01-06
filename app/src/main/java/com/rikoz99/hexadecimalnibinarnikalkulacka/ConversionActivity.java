package com.rikoz99.hexadecimalnibinarnikalkulacka;

import static com.rikoz99.hexadecimalnibinarnikalkulacka.R.*;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ConversionActivity extends NavigationActivity {

    Spinner convModeFrom_SPIN, convModeTo_SPIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_conversion);
        this.setBottomMenu(R.id.bottomNavItemConversion);
        this.setToolbar(getString(R.string.toolbarConv), true);

        convModeFrom_SPIN = findViewById(R.id.convertSpinnerModeFrom);
        // convModeTo_SPIN = findViewById(R.id.convertSpinnerModeTo);

        //TODO nefunguje!

        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter<CharSequence> adapterFrom = ArrayAdapter.createFromResource(
                this,
                array.convertSpinner_mode,
                android.R.layout.simple_spinner_item
        );
        /*
        // Specify the layout to use when the list of choices appears.
        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        convModeFrom_SPIN.setAdapter(adapterFrom);

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

         */
    }
}