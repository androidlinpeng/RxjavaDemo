package msgcopy.com.rxjavademo;

import android.app.Application;
import android.content.Context;

/**
 * Created by liang on 2017/3/27.
 */

public class ListenerApp extends Application{

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }
    public static Context getContext() {
        return sContext;
    }
}













