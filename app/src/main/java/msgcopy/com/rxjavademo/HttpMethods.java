package msgcopy.com.rxjavademo;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liang on 2017/3/27.
 */

public class HttpMethods {

    private static final String TAG = "HttpMethods";
    public static final int HTTP_CACHE_SIZE = 20 * 1024 * 1024;
    public static final int HTTP_CONNECT_TIMEOUT = 15 * 1000;
    public static final int HTTP_READ_TIMEOUT = 20 * 1000;
    private static final String HTTP_CACHE_DIR = "http";


    public static final String BASE_URL = "https://api.douban.com/v2/movie/";
    private static final int DEFAULT_TIMEOUT = 5;
    private Retrofit retrofit;
    private MovieService movieService;

    //私有化构造方法
    private HttpMethods() {
        //手动创建一个okhttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        movieService = retrofit.create(MovieService.class);


//        Gson gson = new GsonBuilder().create();
//        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder()
//                .cache(new Cache(getHttpCacheDir(ListenerApp.getContext()), HTTP_CACHE_SIZE))
//                .connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
//                .readTimeout(HTTP_READ_TIMEOUT, TimeUnit.MILLISECONDS)
//                .build();
//
//        retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .client(client)
//                .addConverterFactory(gsonConverterFactory)
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .build();

    }

    //在访问httpmethods时候创建单例
    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void getHttpData(Subscriber<MovieBean> subscriber, int start, int count) {
        movieService.getHttpData(start, count)
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
