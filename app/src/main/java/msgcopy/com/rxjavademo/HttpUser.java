package msgcopy.com.rxjavademo;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liang on 2017/4/6.
 */

public class HttpUser {
    private static final String TAG = "HttpMethods";
    public static final int HTTP_CACHE_SIZE = 20 * 1024 * 1024;
    public static final int HTTP_CONNECT_TIMEOUT = 15 * 1000;
    public static final int HTTP_READ_TIMEOUT = 20 * 1000;
    private static final String HTTP_CACHE_DIR = "http";


    public static final String BASE_URL = "http://cloud1.kaoke.me/iapi/";
    private static final int DEFAULT_TIMEOUT = 5;
    private Retrofit retrofit;
    private UserService userService;

    //私有化构造方法
    private HttpUser() {
        //手动创建一个okhttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(HTTP_READ_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.cache(new Cache(getHttpCacheDir(ListenerApp.getContext()), HTTP_CACHE_SIZE));

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        userService = retrofit.create(UserService.class);

    }

    //在访问httpmethods时候创建单例
    private static class SingletonHolder {
        private static final HttpUser INSTANCE = new HttpUser();
    }

    //获取单例
    public static HttpUser getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void getHttpData(Subscriber<UserEntity> subscriber, String type, String reg_ver,String device) {
        userService.getUserDtat(type, reg_ver,device)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    //缓存
    public static File getHttpCacheDir(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return new File(context.getExternalCacheDir(), HTTP_CACHE_DIR);
        }
        return new File(context.getCacheDir(), HTTP_CACHE_DIR);
    }

}
