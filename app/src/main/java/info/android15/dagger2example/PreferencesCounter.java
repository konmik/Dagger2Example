package info.android15.dagger2example;

import android.content.SharedPreferences;

import javax.inject.Inject;

public class PreferencesCounter {
    private static final String COUNTER_KEY = "counter";

    @Inject SharedPreferences pref;

    public PreferencesCounter() {
        MyApplication.inject(this);
    }

    public void count() {
        pref.edit().putInt(COUNTER_KEY, pref.getInt(COUNTER_KEY, 0) + 1).apply();
    }
}
