package info.android15.dagger2example;

import android.content.SharedPreferences;
import android.util.Log;

import javax.inject.Inject;

public class RealTarget extends BaseTarget {
    @Inject SharedPreferences pref;

    public RealTarget() {
    }

    public void check() {
        Log.v(getClass().getSimpleName(), "Base injection app: " + app);
        Log.v(getClass().getSimpleName(), "Real injection pref: " + pref);
    }
}
