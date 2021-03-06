package msgcopy.com.rxjavademo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Rxjava和retrofit2
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    TextView textView,sendTv,mTv_git,mTv_post;
    private Subscriber<MovieBean> subscriber;

    private Subscriber<UserEntity> subscriberUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        sendTv = (TextView) findViewById(R.id.sendTv);
        mTv_git = (TextView) findViewById(R.id.mTv_git);
        mTv_post = (TextView) findViewById(R.id.mTv_post);

        //订阅时间
        subscribeMetaChangedEvent();
        //处理数据
        subscribeRequest();

        //使用Retrofit和Rxjava网络请求
        //git请求
        subscribeHttp();
        //post请求
        subscribeHttpUser();

    }

    private void subscribeHttpUser() {

        subscriberUser = new Subscriber<UserEntity>() {
            @Override
            public void onCompleted() {
                Toast.makeText(MainActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
                Log.i(TAG,"onNext:");
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainActivity.this, "Throwable：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i(TAG,"onError" + e.getMessage());
            }

            @Override
            public void onNext(UserEntity userEntity) {
                Toast.makeText(MainActivity.this, "onNext："+userEntity.getFirst_name(), Toast.LENGTH_SHORT).show();
                Log.i(TAG,"onNext:");
                mTv_post.setText("Http: post:"+userEntity.getFirst_name());
            }
        };
        HttpUser.getInstance().getHttpData(subscriberUser, "auto", "1","00000000");
    }

    //进行网络请求
    private void subscribeHttp() {
        subscriber = new Subscriber<MovieBean>() {
            @Override
            public void onCompleted() {
//                Toast.makeText(MainActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
                Log.i(TAG,"onNext:");
            }

            @Override
            public void onError(Throwable e) {
//                Toast.makeText(MainActivity.this, "Throwable：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i(TAG,"" + e.getMessage());
            }

            @Override
            public void onNext(MovieBean movieBean) {
//                Toast.makeText(MainActivity.this, "onNext："+movieBean.getTitle(), Toast.LENGTH_SHORT).show();
                Log.i(TAG,"onNext:");
                mTv_git.setText("Http: git:"+movieBean.getTitle());
            }
        };
        HttpMethods.getInstance().getHttpData(subscriber, 0, 10);

    }

    private void subscribeRequest() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("标题");
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String string) {
                        textView.setText("加载数据："+string);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.i("throwable", "" + throwable.getMessage());
                    }
                });
    }

    private void subscribeMetaChangedEvent() {
        Subscription subscription = RxBus.getInstance()
                .toObservable(MetaChangedEvent.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .distinctUntilChanged()
                .subscribe(new Action1<MetaChangedEvent>() {
                    @Override
                    public void call(MetaChangedEvent metaChangedEvent) {
//                        Toast.makeText(MainActivity.this, "发来的信息：" + metaChangedEvent.getArtistName(), Toast.LENGTH_SHORT).show();
                        sendTv.setText("Secon发来的信息:"+metaChangedEvent.getArtistName());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(MainActivity.this, "throwable："+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RxBus.getInstance().addSubscription(this, subscription);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                startActivity(new Intent(MainActivity.this, SecondAvtivity.class));
                break;
        }
    }

}







