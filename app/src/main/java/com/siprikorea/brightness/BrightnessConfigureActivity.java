package com.siprikorea.brightness;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.siprikorea.brightness.databinding.BrightnessConfigureBinding;

/**
 * The configuration screen for the {@link Brightness Brightness} AppWidget.
 */
public class BrightnessConfigureActivity extends Activity {

    private static final String PREFS_NAME = "com.siprikorea.brightness.Brightness";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText mMinValue;
    EditText mMaxValue;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = BrightnessConfigureActivity.this;

            // When the button is clicked, store the string locally
            String minValue = mMinValue.getText().toString();
            String maxValue = mMaxValue.getText().toString();
            savePref(context, mAppWidgetId, "MIN_VALUE", minValue);
            savePref(context, mAppWidgetId, "MAX_VALUE", maxValue);

            // It is the responsibility of the configuration activity to update the app widget
            //AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            //Brightness.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };
    private BrightnessConfigureBinding binding;

    public BrightnessConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void savePref(Context context, int appWidgetId, String key, String value) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId + key + "_", value);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadPref(Context context, int appWidgetId, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(PREF_PREFIX_KEY + appWidgetId + key + "_", null);
    }

    static void deletePref(Context context, int appWidgetId, String key) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId + key + "_");
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        binding = BrightnessConfigureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mMinValue = binding.minValue;
        mMaxValue = binding.maxValue;
        binding.addButton.setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        mMinValue.setText(loadPref(BrightnessConfigureActivity.this, mAppWidgetId, "MIN_VALUE"));
        mMaxValue.setText(loadPref(BrightnessConfigureActivity.this, mAppWidgetId, "MAX_VALUE"));
    }
}