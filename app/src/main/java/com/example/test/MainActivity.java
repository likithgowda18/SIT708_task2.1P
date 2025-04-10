package com.example.test;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Spinner sourceUnitSpinner, destinationUnitSpinner, typeSpinner;
    private EditText sourceInput;
    private Button convertButton;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        initializeUI();

        // Initialize spinner adapter for conversion types
        setupConversionTypeSpinner();

        // Set onClickListener for the convert button
        convertButton.setOnClickListener(v -> handleConversion());
    }

    // Method to initialize UI components
    private void initializeUI() {
        sourceUnitSpinner = findViewById(R.id.sourceUnit);
        destinationUnitSpinner = findViewById(R.id.destinationUnit);
        typeSpinner = findViewById(R.id.type);
        sourceInput = findViewById(R.id.sourceInput);
        convertButton = findViewById(R.id.Convert);
        resultTextView = findViewById(R.id.result);

        // Clear result text view on start
        resultTextView.setText("");

        // Set item selected listener for conversion types spinner
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = parent.getItemAtPosition(position).toString();
                handleTypeSelection(selectedType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    // Method to set up spinner for conversion types
    private void setupConversionTypeSpinner() {
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.conversion_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
    }

    // Method to handle conversion type selection
    private void handleTypeSelection(String selectedType) {
        String[] units;
        if (selectedType.equals("Length")) {
            units = getResources().getStringArray(R.array.length_units);
        } else if (selectedType.equals("Weight")) {
            units = getResources().getStringArray(R.array.weight_units);
        } else {
            units = getResources().getStringArray(R.array.temperature_units);
        }

        // Update both source and destination unit spinners
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceUnitSpinner.setAdapter(unitAdapter);
        destinationUnitSpinner.setAdapter(unitAdapter);
    }

    // Method to validate if a string is a valid positive number
    private boolean isValidDouble(String string) {
        try {
            double value = Double.parseDouble(string);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Method to handle conversion
    private void handleConversion() {
        String inputText = sourceInput.getText().toString().trim();
        if (inputText.isEmpty() || !isValidDouble(inputText)) {
            // Show error if input is invalid
            Toast.makeText(this, "Invalid Input! Please enter a valid positive number.", Toast.LENGTH_SHORT).show();
        } else {
            performConversion();
        }
    }

    // Method to perform the conversion based on selected type, source, and destination units
    private void performConversion() {
        String type = typeSpinner.getSelectedItem().toString();
        String sourceUnit = sourceUnitSpinner.getSelectedItem().toString();
        String destinationUnit = destinationUnitSpinner.getSelectedItem().toString();

        // Handle case where source and destination units are the same
        if (sourceUnit.equals(destinationUnit)) {
            Toast.makeText(this, "Source and Destination units cannot be the same!", Toast.LENGTH_SHORT).show();
            return;
        }

        double inputValue = Double.parseDouble(sourceInput.getText().toString());
        double resultValue = convertValue(inputValue, sourceUnit, destinationUnit, type);
        resultTextView.setText(formatResult(inputValue, sourceUnit, resultValue, destinationUnit));
    }

    // Method to perform specific conversion based on type (Length, Weight, or Temperature)
    private double convertValue(double inputValue, String sourceUnit, String destinationUnit, String type) {
        switch (type) {
            case "Length":
                return convertLength(inputValue, sourceUnit, destinationUnit);
            case "Weight":
                return convertWeight(inputValue, sourceUnit, destinationUnit);
            case "Temperature":
                return convertTemperature(inputValue, sourceUnit, destinationUnit);
            default:
                return 0;
        }
    }

    // Method to convert length
    private double convertLength(double value, String sourceUnit, String destinationUnit) {
        double resultValue = 0;

        switch (sourceUnit) {
            case "inch":
                resultValue = convertLengthFromInch(value, destinationUnit);
                break;
            case "foot":
                resultValue = convertLengthFromFoot(value, destinationUnit);
                break;
            case "yard":
                resultValue = convertLengthFromYard(value, destinationUnit);
                break;
            case "mile":
                resultValue = convertLengthFromMile(value, destinationUnit);
                break;
            case "cm":
                resultValue = convertLengthFromCm(value, destinationUnit);
                break;
            case "km":
                resultValue = convertLengthFromKm(value, destinationUnit);
                break;
        }

        return resultValue;
    }

    private double convertLengthFromInch(double value, String destinationUnit) {
        switch (destinationUnit) {
            case "inch": return value;
            case "foot": return value / 12;
            case "yard": return value / 36;
            case "mile": return value / 63360;
            case "cm": return value * 2.54;
            case "km": return value * 0.0000254;
            default: return 0;
        }
    }

    private double convertLengthFromFoot(double value, String destinationUnit) {
        switch (destinationUnit) {
            case "inch": return value * 12;
            case "foot": return value;
            case "yard": return value / 3;
            case "mile": return value / 5280;
            case "cm": return value * 30.48;
            case "km": return value * 0.0003048;
            default: return 0;
        }
    }

    private double convertLengthFromYard(double value, String destinationUnit) {
        switch (destinationUnit) {
            case "inch": return value * 36;
            case "foot": return value * 3;
            case "yard": return value;
            case "mile": return value / 1760;
            case "cm": return value * 91.44;
            case "km": return value * 0.0009144;
            default: return 0;
        }
    }

    private double convertLengthFromMile(double value, String destinationUnit) {
        switch (destinationUnit) {
            case "inch": return value * 63360;
            case "foot": return value * 5280;
            case "yard": return value * 1760;
            case "mile": return value;
            case "cm": return value * 160934.4;
            case "km": return value * 1.60934;
            default: return 0;
        }
    }

    private double convertLengthFromCm(double value, String destinationUnit) {
        switch (destinationUnit) {
            case "inch": return value * 0.393701;
            case "foot": return value * 0.0328084;
            case "yard": return value * 0.0109361;
            case "mile": return value * 0.0000062137;
            case "cm": return value;
            case "km": return value * 0.00001;
            default: return 0;
        }
    }

    private double convertLengthFromKm(double value, String destinationUnit) {
        switch (destinationUnit) {
            case "inch": return value * 39370.1;
            case "foot": return value * 3280.84;
            case "yard": return value * 1093.61;
            case "mile": return value * 0.621371;
            case "cm": return value * 100000;
            case "km": return value;
            default: return 0;
        }
    }

    // Method to convert weight
    private double convertWeight(double value, String sourceUnit, String destinationUnit) {
        double resultValue = 0;

        switch (sourceUnit) {
            case "pound":
                resultValue = convertWeightFromPound(value, destinationUnit);
                break;
            case "ounce":
                resultValue = convertWeightFromOunce(value, destinationUnit);
                break;
            case "ton":
                resultValue = convertWeightFromTon(value, destinationUnit);
                break;
            case "kg":
                resultValue = convertWeightFromKg(value, destinationUnit);
                break;
            case "g":
                resultValue = convertWeightFromGram(value, destinationUnit);
                break;
        }

        return resultValue;
    }

    // Weight conversion logic
    private double convertWeightFromPound(double value, String destinationUnit) {
        switch (destinationUnit) {
            case "pound": return value;
            case "ounce": return value * 16;
            case "ton": return value / 2000;
            case "kg": return value * 0.453592;
            case "g": return value * 453.592;
            default: return 0;
        }
    }

    private double convertWeightFromOunce(double value, String destinationUnit) {
        switch (destinationUnit) {
            case "pound": return value / 16;
            case "ounce": return value;
            case "ton": return value / 32000;
            case "kg": return value * 0.0283495;
            case "g": return value * 28.3495;
            default: return 0;
        }
    }

    private double convertWeightFromTon(double value, String destinationUnit) {
        switch (destinationUnit) {
            case "pound": return value * 2000;
            case "ounce": return value * 32000;
            case "ton": return value;
            case "kg": return value * 907.185;
            case "g": return value * 907185;
            default: return 0;
        }
    }

    private double convertWeightFromKg(double value, String destinationUnit) {
        switch (destinationUnit) {
            case "pound": return value / 0.453592;
            case "ounce": return value / 0.0283495;
            case "ton": return value / 907.185;
            case "kg": return value;
            case "g": return value * 1000;
            default: return 0;
        }
    }

    private double convertWeightFromGram(double value, String destinationUnit) {
        switch (destinationUnit) {
            case "pound": return value / 453.592;
            case "ounce": return value / 28.3495;
            case "ton": return value / 907185;
            case "kg": return value / 1000;
            case "g": return value;
            default: return 0;
        }
    }

    // Method to convert temperature
    private double convertTemperature(double value, String sourceUnit, String destinationUnit) {
        double resultValue = 0;

        switch (sourceUnit) {
            case "Celsius":
                resultValue = convertTemperatureFromCelsius(value, destinationUnit);
                break;
            case "Fahrenheit":
                resultValue = convertTemperatureFromFahrenheit(value, destinationUnit);
                break;
            case "Kelvin":
                resultValue = convertTemperatureFromKelvin(value, destinationUnit);
                break;
        }

        return resultValue;
    }

    // Temperature conversion logic
    private double convertTemperatureFromCelsius(double value, String destinationUnit) {
        switch (destinationUnit) {
            case "Celsius": return value;
            case "Fahrenheit": return (value * 9 / 5) + 32;
            case "Kelvin": return value + 273.15;
            default: return 0;
        }
    }

    private double convertTemperatureFromFahrenheit(double value, String destinationUnit) {
        switch (destinationUnit) {
            case "Celsius": return (value - 32) * 5 / 9;
            case "Fahrenheit": return value;
            case "Kelvin": return (value - 32) * 5 / 9 + 273.15;
            default: return 0;
        }
    }

    private double convertTemperatureFromKelvin(double value, String destinationUnit) {
        switch (destinationUnit) {
            case "Celsius": return value - 273.15;
            case "Fahrenheit": return (value - 273.15) * 9 / 5 + 32;
            case "Kelvin": return value;
            default: return 0;
        }
    }

    // Method to format the conversion result
    private String formatResult(double inputValue, String sourceUnit, double resultValue, String destinationUnit) {
        DecimalFormat df = new DecimalFormat("#.####");
        return "Result: " + inputValue + " " + sourceUnit + " = " + df.format(resultValue) + " " + destinationUnit;
    }
}
