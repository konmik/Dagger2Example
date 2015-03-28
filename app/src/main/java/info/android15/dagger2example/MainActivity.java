package info.android15.dagger2example;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import javax.inject.Inject;

import info.android15.dagger2example.dummy.Dummy01;
import info.android15.dagger2example.dummy.Dummy11;
import info.android15.dagger2example.dummy.Dummy21;
import info.android15.dagger2example.dummy.Dummy31;
import info.android15.dagger2example.dummy.Dummy41;

public class MainActivity extends Activity {

    public static final int DUMMY_COUNT = 50;
    public static final int TESTS_COUNT = 1000;
    @Inject SharedPreferences pref;
    @Inject PreferencesLogger logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.inject(this);
        setContentView(R.layout.activity_main);

        logger.log();
        new PreferencesCounter().count();
        logger.log();

        RealTarget target = new RealTarget();
        target.check();

        ArrayList<Object> dummy = new ArrayList<>();
        try {
            for (int i = 1; i <= DUMMY_COUNT; i++)
                dummy.add(Class.forName("info.android15.dagger2example.dummy.Dummy" + String.format("%02d", i)).getConstructor().newInstance((Object[])null));
        }
        catch (Exception ignored) {
        }

        long time1 = System.nanoTime();
        for (int i = 0; i < TESTS_COUNT; i++) {
            MyApplication.inject(dummy.get(i % DUMMY_COUNT));
        }
        Log.v(getClass().getSimpleName(), String.format("inject1 %d ms", (System.nanoTime() - time1) / 1000000));

        long time2 = System.nanoTime();
        for (int i = 0; i < TESTS_COUNT / 5; i++) {
            MyApplication.getComponent().inject((Dummy01)dummy.get(0));
            MyApplication.getComponent().inject((Dummy11)dummy.get(10));
            MyApplication.getComponent().inject((Dummy21)dummy.get(20));
            MyApplication.getComponent().inject((Dummy31)dummy.get(30));
            MyApplication.getComponent().inject((Dummy41)dummy.get(40));
        }
        Log.v(getClass().getSimpleName(), String.format("inject2 %d ms", (System.nanoTime() - time2) / 1000000));
    }
}
