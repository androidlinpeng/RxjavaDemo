package msgcopy.com.rxjavademo;

import java.util.HashMap;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by liang on 2017/3/27.
 */

public class RxBus {

    private static  volatile RxBus mInstance;
    private SerializedSubject<Object,Object> mSubject;
    private HashMap<String,CompositeSubscription> mSubscriptionMap;

    public RxBus() {
        this.mSubject = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus getInstance(){
        if (mInstance==null){
            synchronized (RxBus.class){
                if (mInstance == null){
                    mInstance = new RxBus();
                }
            }
        }
        return mInstance;
    }

    /**
     * 发送事件
     */
    public void post(Object o){
        mSubject.onNext(o);
    }

    /**
     * 返回指定类型的Observable实例
     */
    public <T> Observable<T> toObservable(final Class<T> type){
        return mSubject.ofType(type);
    }

    /**
     * 是否已有观察者订阅
     */
    public boolean hasObservable(){
        return mSubject.hasObservers();
    }

    /**
     * 一个默认的订阅方法
     */
    public <T> Subscription doSubscribe(Class<T> type, Action1<T> next, Action1<Throwable> error) {
        return toObservable(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(next, error);
    }

    /**
     * 保存订阅后的subscription
     */
    public void addSubscription(Object o, Subscription subscription) {
        if (mSubscriptionMap == null) {
            mSubscriptionMap = new HashMap<>();
        }
        String key = o.getClass().getName();
        if (mSubscriptionMap.get(key) != null) {
            mSubscriptionMap.get(key).add(subscription);
        } else {
            CompositeSubscription compositeSubscription = new CompositeSubscription();
            compositeSubscription.add(subscription);
            mSubscriptionMap.put(key, compositeSubscription);
        }
    }

    /**
     * 取消订阅
     */
    public void unSubscribe(Object o) {
        if (mSubscriptionMap == null) {
            return;
        }

        String key = o.getClass().getName();
        if (!mSubscriptionMap.containsKey(key)){
            return;
        }
        if (mSubscriptionMap.get(key) != null) {
            mSubscriptionMap.get(key).unsubscribe();
        }

        mSubscriptionMap.remove(key);
    }

}











