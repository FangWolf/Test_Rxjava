package com.fangwolf.test_rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

//合并操作符
public class Main3Activity extends AppCompatActivity implements View.OnClickListener {
    String TAG = "Main3Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        findViewById(R.id.btnn1).setOnClickListener(this);
        findViewById(R.id.btnn2).setOnClickListener(this);
        findViewById(R.id.btnn3).setOnClickListener(this);
        findViewById(R.id.btnn4).setOnClickListener(this);
        findViewById(R.id.btnn5).setOnClickListener(this);
        findViewById(R.id.btnn6).setOnClickListener(this);
        findViewById(R.id.btnn7).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnn1:
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        // 1. 发送5个事件
                        emitter.onNext(1);
                        emitter.onNext(2);
                        emitter.onNext(3);
                        emitter.onNext(4);
                        emitter.onNext(5);
                    }

                    // 2. 采用filter（）变换操作符
                }).filter(new Predicate<Integer>() {
                    // 根据test()的返回值 对被观察者发送的事件进行过滤 & 筛选
                    // a. 返回true，则继续发送
                    // b. 返回false，则不发送（即过滤）
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer > 3;
                        // 本例子 = 过滤了整数≤3的事件
                    }
                }).subscribe(new Observer<Integer>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.d(TAG, "过滤后得到的事件是：" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }
                });
                break;
            case R.id.btnn2:
                Observable.just(1, "Carson", 3, "Ho", 5)
                        .ofType(Integer.class) // 筛选出 整型数据
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                Log.d(TAG, "获取到的整型事件元素是： " + integer);
                            }
                        });
                break;
            case R.id.btnn3:
                // 使用1：根据顺序跳过数据项
                Observable.just(1, 2, 3, 4, 5)
                        .skip(1) // 跳过正序的前1项
                        .skipLast(2) // 跳过正序的后2项
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                Log.d(TAG, "获取到的整型事件元素是： " + integer);
                            }
                        });

                // 使用2：根据时间跳过数据项
                // 发送事件特点：发送数据0-5，每隔1s发送一次，每次递增1；第1次发送延迟0s
                Observable.intervalRange(0, 5, 0, 1, TimeUnit.SECONDS)
                        .skip(1, TimeUnit.SECONDS) // 跳过第1s发送的数据
                        .skipLast(1, TimeUnit.SECONDS) // 跳过最后1s发送的数据
                        .subscribe(new Consumer<Long>() {

                            @Override
                            public void accept(Long along) throws Exception {
                                Log.d(TAG, "获取到的整型事件元素是： " + along);
                            }
                        });
                break;
            case R.id.btnn4:
                // 使用1：过滤事件序列中重复的事件
                Observable.just(1, 2, 3, 1, 2)
                        .distinct()
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                Log.d(TAG, "不重复的整型事件元素是： " + integer);
                            }
                        });

                // 使用2：过滤事件序列中 连续重复的事件
                // 下面序列中，连续重复的事件 = 3、4
                Disposable subscribe = Observable.just(1, 2, 3, 1, 2, 3, 3, 4, 4)
                        .distinctUntilChanged()
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                Log.d(TAG, "不连续重复的整型事件元素是： " + integer);
                            }
                        });
                break;
            case R.id.btnn5:
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        // 1. 发送5个事件
                        emitter.onNext(1);
                        emitter.onNext(2);
                        emitter.onNext(3);
                        emitter.onNext(4);
                        emitter.onNext(5);
                    }

                    // 采用take（）变换操作符
                    // 指定了观察者只能接收2个事件
                }).take(2)
                        .subscribe(new Observer<Integer>() {

                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d(TAG, "开始采用subscribe连接");
                            }

                            @Override
                            public void onNext(Integer value) {
                                Log.d(TAG, "过滤后得到的事件是：" + value);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "对Error事件作出响应");
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "对Complete事件作出响应");
                            }
                        });

                // 实际上，可理解为：被观察者还是发送了5个事件，只是因为操作符的存在拦截了3个事件，最终观察者接收到的是2个事件
                break;
            case R.id.btnn6:
                //在某段时间内，只发送该段时间内第1次事件
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        // 隔段事件发送时间
                        e.onNext(1);
                        Thread.sleep(500);

                        e.onNext(2);
                        Thread.sleep(400);

                        e.onNext(3);
                        Thread.sleep(300);

                        e.onNext(4);
                        Thread.sleep(300);

                        e.onNext(5);
                        Thread.sleep(300);

                        e.onNext(6);
                        Thread.sleep(400);

                        e.onNext(7);
                        Thread.sleep(300);
                        e.onNext(8);

                        Thread.sleep(300);
                        e.onNext(9);

                        Thread.sleep(300);
                        e.onComplete();
                    }
                }).throttleFirst(1, TimeUnit.SECONDS)//每1秒中采用数据
                        .subscribe(new Observer<Integer>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d(TAG, "开始采用subscribe连接");
                            }

                            @Override
                            public void onNext(Integer value) {
                                Log.d(TAG, "接收到了事件" + value);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "对Error事件作出响应");
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "对Complete事件作出响应");
                            }
                        });


                //在某段时间内，只发送该段时间内最后1次事件
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        // 隔段事件发送时间
                        e.onNext(1);
                        Thread.sleep(500);

                        e.onNext(2);
                        Thread.sleep(400);

                        e.onNext(3);
                        Thread.sleep(300);

                        e.onNext(4);
                        Thread.sleep(300);

                        e.onNext(5);
                        Thread.sleep(300);

                        e.onNext(6);
                        Thread.sleep(400);

                        e.onNext(7);
                        Thread.sleep(300);
                        e.onNext(8);

                        Thread.sleep(300);
                        e.onNext(9);

                        Thread.sleep(300);
                        e.onComplete();
                    }
                }).throttleLast(1, TimeUnit.SECONDS)//每1秒中采用数据
                        .subscribe(new Observer<Integer>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d(TAG, "开始采用subscribe连接");
                            }

                            @Override
                            public void onNext(Integer value) {
                                Log.d(TAG, "接收到了事件" + value);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "对Error事件作出响应");
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "对Complete事件作出响应");
                            }
                        });
                break;
            case R.id.btnn7:
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        // 隔段事件发送时间
                        e.onNext(1);
                        Thread.sleep(500);
                        e.onNext(2); // 1和2之间的间隔小于指定时间1s，所以前1次数据（1）会被抛弃，2会被保留
                        Thread.sleep(1500);  // 因为2和3之间的间隔大于指定时间1s，所以之前被保留的2事件将发出
                        e.onNext(3);
                        Thread.sleep(1500);  // 因为3和4之间的间隔大于指定时间1s，所以3事件将发出
                        e.onNext(4);
                        Thread.sleep(500); // 因为4和5之间的间隔小于指定时间1s，所以前1次数据（4）会被抛弃，5会被保留
                        e.onNext(5);
                        Thread.sleep(500); // 因为5和6之间的间隔小于指定时间1s，所以前1次数据（5）会被抛弃，6会被保留
                        e.onNext(6);
                        Thread.sleep(1500); // 因为6和Complete实践之间的间隔大于指定时间1s，所以之前被保留的6事件将发出

                        e.onComplete();
                    }
                }).throttleWithTimeout(1, TimeUnit.SECONDS)//每1秒中采用数据
                        .subscribe(new Observer<Integer>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Integer value) {
                                Log.d(TAG, "接收到了事件" + value);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "对Error事件作出响应");
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "对Complete事件作出响应");
                            }
                        });
                break;
        }

    }
}
