package info.android15.dagger2example;

import android.util.Log;

import javax.inject.Inject;

public class BaseTarget {
    @Inject protected MyApplication app;

    public BaseTarget() {
        Log.v(getClass().getSimpleName(), "app before injection: " + app);
        MyApplication.inject(this);
//        MyApplication.getComponent().inject(this);
        Log.v(getClass().getSimpleName(), "app after injection: " + app);
    }
}
