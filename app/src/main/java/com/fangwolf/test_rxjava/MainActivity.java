package com.fangwolf.test_rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";
    TextView tv;
    Button fire;
    Button fire2;
    Button fire3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv);
        fire = findViewById(R.id.fire);
        fire2 = findViewById(R.id.fire2);
        fire3 = findViewById(R.id.fire3);

        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Rxjava基于时间流的链式调用
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    //创建 被观察者 并发射 事件
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        emitter.onNext(1);
                        Thread.sleep(700);
                        emitter.onNext(2);
                        Thread.sleep(700);
                        emitter.onNext(3);
                        Thread.sleep(700);
                        emitter.onComplete();
                    }
                }).subscribe(new Observer<Integer>() {
                    //通过订阅连接 被观察者和观察者
                    //创建观察者并处理事件
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "开始Subscribe连接");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e(TAG, "处理 onNext事件" + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "处理 Error事件");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "处理 Complete事件");
                    }
                });
            }
        });

        fire2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    // 1. 创建被观察者 & 生产事件
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

                        for (int i = 0; ; i++) {
                            Log.d(TAG, "发送了事件" + i);
                            Thread.sleep(1);
                            // 发送事件速度：1ms / 个
                            emitter.onNext(i);

                        }

                    }
                }).subscribeOn(Schedulers.io()) // 设置被观察者在io线程中进行
                        .observeOn(AndroidSchedulers.mainThread()) // 设置观察者在主线程中进行
                        .subscribe(new Observer<Integer>() {
                            // 2. 通过通过订阅（subscribe）连接观察者和被观察者

                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d(TAG, "开始采用subscribe连接");
                            }

                            @Override
                            public void onNext(Integer value) {

                                try {
                                    // 接收事件速度：5s / 个
                                    Thread.sleep(5000);
                                    Log.d(TAG, "接收到了事件" + value);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

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
            }
        });

        fire3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 步骤1：创建被观察者 =  Flowable
                Flowable.create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                        Log.e(TAG, "发送事件 1");
                        emitter.onNext(1);
                        Log.e(TAG, "发送事件 2");
                        emitter.onNext(2);
                        Log.e(TAG, "发送事件 3");
                        emitter.onNext(3);
                        Log.e(TAG, "发送完成");
                        emitter.onComplete();
                    }
                }, BackpressureStrategy.ERROR)
                        .subscribe(new Subscriber<Integer>() {
                            // 步骤2：创建观察者 =  Subscriber & 建立订阅关系

                            @Override
                            public void onSubscribe(Subscription s) {
                                Log.e(TAG, "onSubscribe");
                                s.request(3);
                            }

                            @Override
                            public void onNext(Integer integer) {
                                Log.e(TAG, "接收到了事件" + integer);
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.e(TAG, "onError: ", t);
                            }

                            @Override
                            public void onComplete() {
                                Log.e(TAG, "onComplete");
                            }
                        });
                /**
                 * 步骤1：创建被观察者 =  Flowable
                 *//*
                Flowable<Integer> upstream = Flowable.create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                        emitter.onNext(1);
                        emitter.onNext(2);
                        emitter.onNext(3);
                        emitter.onComplete();
                    }
                }, BackpressureStrategy.ERROR);
                // 需要传入背压参数BackpressureStrategy，下面会详细讲解

                *//**
                 * 步骤2：创建观察者 =  Subscriber
                 *//*
                Subscriber<Integer> downstream = new Subscriber<Integer>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        // 对比Observer传入的Disposable参数，Subscriber此处传入的参数 = Subscription
                        // 相同点：Subscription具备Disposable参数的作用，即Disposable.dispose()切断连接, 同样的调用Subscription.cancel()切断连接
                        // 不同点：Subscription增加了void request(long n)
                        Log.d(TAG, "onSubscribe");
                        s.request(Long.MAX_VALUE);
                        // 关于request()下面会继续详细说明
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.w(TAG, "onError: ", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                };

                *//**
                 * 步骤3：建立订阅关系
                 *//*
                upstream.subscribe(downstream);*/
            }
        });
    }
}
