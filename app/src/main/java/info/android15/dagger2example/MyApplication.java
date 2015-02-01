package info.android15.dagger2example;

import android.app.Application;

public class MyApplication extends Application {
    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        // component = Dagger_AppComponent.builder().appModule(new AppModule(this)).build();
        component = Dagger2Helper.buildComponent(AppComponent.class, new AppModule(this));
    }

    public static void inject(Object target) {
        Dagger2Helper.inject(AppComponent.class, component, target);
    }

    public static AppComponent getComponent() {
        return component;
    }
}
