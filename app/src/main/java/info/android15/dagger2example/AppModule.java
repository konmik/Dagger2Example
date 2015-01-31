package info.android15.dagger2example;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private static final String PREFERENCES_FILE_NAME = "preferences";

    private MyApplication app;

    AppModule(MyApplication app) {
        this.app = app;
    }

    @Singleton
    @Provides
    SharedPreferences provideSharedPreferences() {
        return app.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
    }

    @Provides
    MyApplication provideApp() {
        return app;
    }
}
