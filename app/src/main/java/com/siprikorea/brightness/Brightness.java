package com.siprikorea.brightness;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link BrightnessConfigureActivity BrightnessConfigureActivity}
 */
public class Brightness extends AppWidgetProvider {
    private static final String ON_MIN_BUTTON_CLICK = "onMinButtonClick";
    private static final String ON_AUTO_BUTTON_CLICK = "onAutoButtonClick";
    private static final String ON_MAX_BUTTON_CLICK = "onMaxButtonClick";

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.brightness);
            views.setOnClickPendingIntent(R.id.min, getPendingSelfIntent(context, ON_MIN_BUTTON_CLICK));
            views.setOnClickPendingIntent(R.id.auto, getPendingSelfIntent(context, ON_AUTO_BUTTON_CLICK));
            views.setOnClickPendingIntent(R.id.max, getPendingSelfIntent(context, ON_MAX_BUTTON_CLICK));
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Enter relevant functionality for when the widget is deleted
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (!Settings.System.canWrite(context)) {
            Intent intentSettings = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intentSettings.setData(Uri.parse("package:" + context.getPackageName()));
            intentSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentSettings);
            return;
        }

        if (ON_MIN_BUTTON_CLICK.equals(intent.getAction())) {
            setBrightnessToMin(context);
            Toast.makeText(context, "MIN button is clicked", Toast.LENGTH_LONG).show();
        } else if (ON_AUTO_BUTTON_CLICK.equals(intent.getAction())) {
            setBrightnessToAuto(context);
            Toast.makeText(context, "AUTO button is clicked", Toast.LENGTH_LONG).show();
        } else if (ON_MAX_BUTTON_CLICK.equals(intent.getAction())) {
            setBrightnessToMax(context);
            Toast.makeText(context, "MAX button is clicked", Toast.LENGTH_LONG).show();
        }
    }

    private void setBrightnessToAuto(Context context) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }

    private void setBrightnessToMin(Context context) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);
    }

    private void setBrightnessToMax(Context context) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 255);
    }
}