package com.fangwolf.test_rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

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
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";
    private Subscription mSubscription; // 用于保存Subscription对象
    TextView tv;
    Button fire;
    Button fire2;
    Button fire3;
    Button fire4;
    Button fire5;// 该按钮用于调用Subscription.request（long n ）
    Button fire5_1;
    Button fire6;
    Button fire7;
    Button fire7_1;
    Button fire8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv);
        fire = findViewById(R.id.fire);
        fire2 = findViewById(R.id.fire2);
        fire3 = findViewById(R.id.fire3);
        fire4 = findViewById(R.id.fire4);
        fire5 = findViewById(R.id.fire5);
        fire5_1 = findViewById(R.id.fire5_1);
        fire6 = findViewById(R.id.fire6);
        fire7 = findViewById(R.id.fire7);
        fire7_1 = findViewById(R.id.fire7_1);
        fire8 = findViewById(R.id.fire8);

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

        fire4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. 创建被观察者Flowable
                Flowable.create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                        // 一共发送4个事件
                        Log.e(TAG, "发送事件 1");
                        emitter.onNext(1);
                        Log.e(TAG, "发送事件 2");
                        emitter.onNext(2);
                        Log.e(TAG, "发送事件 3");
                        emitter.onNext(3);
                        Log.e(TAG, "发送事件 4");
                        emitter.onNext(4);
                        Log.e(TAG, "发送完成");
                        emitter.onComplete();
                    }
                }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io()) // 设置被观察者在io线程中进行
                        .observeOn(AndroidSchedulers.mainThread()) // 设置观察者在主线程中进行
                        .subscribe(new Subscriber<Integer>() {
                            @Override
                            public void onSubscribe(Subscription s) {
                                // 对比Observer传入的Disposable参数，Subscriber此处传入的参数 = Subscription
                                // 相同点：Subscription参数具备Disposable参数的作用，即Disposable.dispose()切断连接, 同样的调用Subscription.cancel()切断连接
                                // 不同点：Subscription增加了void request(long n)

                                s.request(3);
                                // 作用：决定观察者能够接收多少个事件
                                // 如设置了s.request(3)，这就说明观察者能够接收3个事件（多出的事件存放在缓存区）
                                // 官方默认推荐使用Long.MAX_VALUE，即s.request(Long.MAX_VALUE);
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
            }
        });

        fire5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Flowable.create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                        Log.e(TAG, "发送事件 1");
                        emitter.onNext(1);
                        Log.e(TAG, "发送事件 2");
                        emitter.onNext(2);
                        Log.e(TAG, "发送事件 3");
                        emitter.onNext(3);
                        Log.e(TAG, "发送事件 4");
                        emitter.onNext(4);
                        Log.e(TAG, "发送完成");
                        emitter.onComplete();
                    }
                }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io()) // 设置被观察者在io线程中进行
                        .observeOn(AndroidSchedulers.mainThread()) // 设置观察者在主线程中进行
                        .subscribe(new Subscriber<Integer>() {
                            @Override
                            public void onSubscribe(Subscription s) {
                                Log.e(TAG, "onSubscribe");
                                mSubscription = s;
                                // 保存Subscription对象，等待点击按钮时（调用request(2)）观察者再接收事件
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
            }
        });

        fire5_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSubscription.request(2);
            }
        });
        fire6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Flowable.create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {

                        // 调用emitter.requested()获取当前观察者需要接收的事件数量
                        long n = emitter.requested();

                        Log.d(TAG, "观察者可接收事件" + n);

                        // 根据emitter.requested()的值，即当前观察者需要接收的事件数量来发送事件
                        for (int i = 0; i < n; i++) {
                            Log.d(TAG, "发送了事件" + i);
                            emitter.onNext(i);
                        }
                    }
                }, BackpressureStrategy.ERROR)
                        .subscribe(new Subscriber<Integer>() {
                            @Override
                            public void onSubscribe(Subscription s) {
                                Log.d(TAG, "onSubscribe");

                                // 设置观察者每次能接受10个事件
                                s.request(10);

                            }

                            @Override
                            public void onNext(Integer integer) {
                                Log.d(TAG, "接收到了事件" + integer);
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.w(TAG, "onError: ", t);
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete");
                            }
                        });
            }
        });
        fire7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 被观察者：一共需要发送500个事件，但真正开始发送事件的前提 = FlowableEmitter.requested()返回值 ≠ 0
                // 观察者：每次接收事件数量 = 48（点击按钮）
                Flowable.create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {

                        Log.d(TAG, "观察者可接收事件数量 = " + emitter.requested());
                        boolean flag; //设置标记位控制

                        // 被观察者一共需要发送500个事件
                        for (int i = 0; i < 500; i++) {
                            flag = false;

                            // 若requested() == 0则不发送
                            while (emitter.requested() == 0) {
                                if (!flag) {
                                    Log.d(TAG, "不再发送");
                                    flag = true;
                                }
                            }
                            // requested() ≠ 0 才发送
                            Log.d(TAG, "发送了事件" + i + "，观察者可接收事件数量 = " + emitter.requested());
                            emitter.onNext(i);


                        }
                    }
                }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io()) // 设置被观察者在io线程中进行
                        .observeOn(AndroidSchedulers.mainThread()) // 设置观察者在主线程中进行
                        .subscribe(new Subscriber<Integer>() {
                            @Override
                            public void onSubscribe(Subscription s) {
                                Log.d(TAG, "onSubscribe");
                                mSubscription = s;
                                // 初始状态 = 不接收事件；通过点击按钮接收事件
                            }

                            @Override
                            public void onNext(Integer integer) {
                                Log.d(TAG, "接收到了事件" + integer);
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.w(TAG, "onError: ", t);
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete");
                            }
                        });
            }
        });
        fire7_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSubscription.request(48);
                // 点击按钮 则 接收48个事件
            }
        });
        fire8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 通过interval自动创建被观察者Flowable
                // 每隔1ms将当前数字（从0开始）加1，并发送出去
                // interval操作符会默认新开1个新的工作线程
                Flowable.interval(1, TimeUnit.MILLISECONDS)
                        .onBackpressureBuffer() //背压策略
                        .observeOn(Schedulers.newThread()) // 观察者同样工作在一个新开线程中
                        .subscribe(new Subscriber<Long>() {
                            @Override
                            public void onSubscribe(Subscription s) {
                                Log.d(TAG, "onSubscribe");
                                mSubscription = s;
                                s.request(Long.MAX_VALUE); //默认可以接收Long.MAX_VALUE个事件
                            }

                            @Override
                            public void onNext(Long aLong) {
                                Log.d(TAG, "onNext: " + aLong);
                                try {
                                    Thread.sleep(1000);
                                    // 每次延时1秒再接收事件
                                    // 因为发送事件 = 延时1ms，接收事件 = 延时1s，出现了发送速度 & 接收速度不匹配的问题
                                    // 缓存区很快就存满了128个事件，从而抛出MissingBackpressureException异常，请看下图结果
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onError(Throwable t) {
                                Log.w(TAG, "onError: ", t);
                            }
                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete");
                            }
                        });
            }
        });

        // 采用RxJava基于事件流的链式操作
        Observable.create(new ObservableOnSubscribe<Integer>() {

            // 1. 被观察者发送事件 = 参数为整型 = 1、2、3
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);

            }
            // 2. 使用Map变换操作符中的Function函数对被观察者发送的事件进行统一变换：整型变换成字符串类型
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return "使用 Map变换操作符 将事件" + integer +"的参数从 整型"+integer + " 变换成 字符串类型" + integer ;
            }
        }).subscribe(new Consumer<String>() {

            // 3. 观察者接收事件时，是接收到变换后的事件 = 字符串类型
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });
    }
}
