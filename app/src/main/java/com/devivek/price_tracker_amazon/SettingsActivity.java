package com.devivek.price_tracker_amazon;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.devivek.price_tracker_amazon.utilities.Background;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    String[] counryList = {"BR","TR","SP","GE","IN","AU","UK","CA","US"};
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    int DurationSelected;
    String countryIndex;

    private TextView duration_text;
    private TextView country_text;


    @SuppressLint({"SetTextI18n", "CommitPrefEdits"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //start service
        this.startService(new Intent(this, Background.class));

        //Const
        duration_text = findViewById(R.id.duration_text);
        country_text = findViewById(R.id.country_option);

        //SharedPref
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        DurationSelected = sharedPreferences.getInt(getString(R.string.DurationTime),30);
        countryIndex = sharedPreferences.getString(getString(R.string.DefaultCountry),"US");
        duration_text.setText(DurationSelected+" "+getString(R.string.Minutes));
        country_text.setText(countryIndex);

        //Back arrow function
        ImageView backArrow = findViewById(R.id.backarrow);
        backArrow.setOnClickListener(view -> finish());


        //Version name function
        TextView versionName = findViewById(R.id.versionName);
        try {
            PackageInfo packageInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionname = packageInfo.versionName;
            versionName.setText("Version - "+ versionname);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "onCreate: Error getting version name: "+e.getMessage());
        }

        //Duration btn
        ImageView duration_btn = findViewById(R.id.duration_btn);
        duration_btn.setOnClickListener(view -> DurationDialog());

        //Country btn
        ImageView country_btn = findViewById(R.id.country_btn);
        country_btn.setOnClickListener(view -> CountryDialog());
    }

    public void DurationDialog() {
        final AlertDialog.Builder d = new AlertDialog.Builder(SettingsActivity.this,R.style.MyDialogTheme);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_duration, null);
        d.setTitle("Duration");
        d.setMessage("Choose duration in minutes");
        d.setView(dialogView);
        final NumberPicker numberPicker = dialogView.findViewById(R.id.dialog_number_picker);
        numberPicker.setMaxValue(9999);
        numberPicker.setMinValue(1);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setValue(DurationSelected);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                Log.d(TAG, "onValueChange: ");
            }
        });
        d.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int Duration = numberPicker.getValue();
                Log.d(TAG, "onClick: " + Duration);
                editor.putInt(getString(R.string.DurationTime),Duration);
                editor.commit();

                duration_text.setText(Duration+" "+getString(R.string.Minutes));
            }
        });
        d.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = d.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    public void CountryDialog() {
        final AlertDialog.Builder d = new AlertDialog.Builder(SettingsActivity.this,R.style.MyDialogTheme);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_duration, null);
        d.setTitle("Country");
        d.setMessage("Choose default country");
        d.setView(dialogView);
        final NumberPicker numberPicker = dialogView.findViewById(R.id.dialog_number_picker);
        numberPicker.setMaxValue(8);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setDisplayedValues(counryList);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                Log.d(TAG, "onValueChange: ");
            }
        });
        d.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String countryName = counryList[numberPicker.getValue()];

                editor.putString(getString(R.string.DefaultCountry), countryName);
                editor.commit();

                country_text.setText(countryName);
            }
        });
        d.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog alertDialog = d.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.startService(new Intent(this, Background.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.startService(new Intent(this, Background.class));
    }
}
