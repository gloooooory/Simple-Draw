package com.simplemobiletools.draw;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

public class Config {
    private SharedPreferences mPrefs;

    public static Config newInstance(Context context) {
        return new Config(context);
    }

    public Config(Context context) {
        mPrefs = context.getSharedPreferences(Constants.PREFS_KEY, Context.MODE_PRIVATE);
    }

    public boolean getIsFirstRun() {
        return mPrefs.getBoolean(Constants.IS_FIRST_RUN, true);
    }

    public void setIsFirstRun(boolean firstRun) {
        mPrefs.edit().putBoolean(Constants.IS_FIRST_RUN, firstRun).apply();
    }

    public boolean getIsDarkTheme() {
        return mPrefs.getBoolean(Constants.IS_DARK_THEME, false);
    }

    public void setIsDarkTheme(boolean isDarkTheme) {
        mPrefs.edit().putBoolean(Constants.IS_DARK_THEME, isDarkTheme).apply();
    }

    public boolean getIsStrokeWidthBarEnabled() {
        return mPrefs.getBoolean(Constants.IS_STROKE_WIDTH_BAR_ENABLED, false);
    }

    public void setIsStrokeWidthBarEnabled(boolean isStrokeWidthBarEnabled) {
        mPrefs.edit().putBoolean(Constants.IS_STROKE_WIDTH_BAR_ENABLED, isStrokeWidthBarEnabled).apply();
    }

    public int getBrushColor() {
        return mPrefs.getInt(Constants.BRUSH_COLOR_KEY, Color.BLACK);
    }

    public void setBrushColor(int color) {
        mPrefs.edit().putInt(Constants.BRUSH_COLOR_KEY, color).apply();
    }

    public float getStrokeWidth() {
        return mPrefs.getFloat(Constants.STROKE_WIDTH_KEY, 5.0f);
    }

    public void setStrokeWidth(float strokeWidth) {
        mPrefs.edit().putFloat(Constants.STROKE_WIDTH_KEY, strokeWidth).apply();
    }

    public int getBackgroundColor() {
        return mPrefs.getInt(Constants.BACKGROUND_COLOR_KEY, Color.WHITE);
    }

    public void setBackgroundColor(int color) {
        mPrefs.edit().putInt(Constants.BACKGROUND_COLOR_KEY, color).apply();
    }
}
