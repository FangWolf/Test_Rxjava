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
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "Main2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        findViewById(R.id.btn0).setOnClickListener(this);
        findViewById(R.id.btn0_0).setOnClickListener(this);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn1_0).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn2_0).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn0:
                // concat（）：组合多个被观察者（≤4个）一起发送数据
                Observable.concat(Observable.just(1, 2, 3),
                        Observable.just(4, 5, 6),
                        Observable.just(7, 8, 9),
                        Observable.just(10, 11, 12))
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
            case R.id.btn0_0:
                // concatArray（）：组合多个被观察者一起发送数据（可＞4个）
                // 注：串行执行
                Observable.concatArray(Observable.just(1, 2, 3),
                        Observable.just(4, 5, 6),
                        Observable.just(7, 8, 9),
                        Observable.just(10, 11, 12),
                        Observable.just(13, 14, 15))
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
            case R.id.btn1:
                // merge（）：组合多个被观察者（＜4个）一起发送数据
                // 注：合并后按照时间线并行执行
                Observable.merge(
                        Observable.intervalRange(0, 3, 1, 1, TimeUnit.SECONDS), // 从0开始发送、共发送3个数据、第1次事件延迟发送时间 = 1s、间隔时间 = 1s
                        Observable.intervalRange(2, 3, 1, 1, TimeUnit.SECONDS)) // 从2开始发送、共发送3个数据、第1次事件延迟发送时间 = 1s、间隔时间 = 1s
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Long value) {
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
            // mergeArray（） = 组合4个以上的被观察者一起发送数据
            case R.id.btn2:
                Observable.concat(
                        Observable.create(new ObservableOnSubscribe<Integer>() {
                            @Override
                            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                                emitter.onNext(1);
                                emitter.onNext(2);
                                emitter.onNext(3);
                                emitter.onError(new NullPointerException()); // 发送Error事件，因为无使用concatDelayError，所以第2个Observable将不会发送事件
                                emitter.onComplete();
                            }
                        }),
                        Observable.just(4, 5, 6))
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
            case R.id.btn2_0:
                Observable.concatArrayDelayError(
                        Observable.create(new ObservableOnSubscribe<Integer>() {
                            @Override
                            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

                                emitter.onNext(1);
                                emitter.onNext(2);
                                emitter.onNext(3);
                                emitter.onError(new NullPointerException()); // 发送Error事件，因为使用了concatDelayError，所以第2个Observable将会发送事件，等发送完毕后，再发送错误事件
                                emitter.onComplete();
                            }
                        }),
                        Observable.just(4, 5, 6))
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
            case R.id.btn3:
                //创建第1个被观察者
                Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        Log.d(TAG, "被观察者1发送了事件1");
                        emitter.onNext(1);
                        // 为了方便展示效果，所以在发送事件后加入延迟
                        Thread.sleep(500);

                        Log.d(TAG, "被观察者1发送了事件2");
                        emitter.onNext(2);
                        Thread.sleep(500);

                        Log.d(TAG, "被观察者1发送了事件3");
                        emitter.onNext(3);
                        Thread.sleep(500);

                        emitter.onComplete();
                    }
                }).subscribeOn(Schedulers.io()); // 设置被观察者1在工作线程1中工作

                //创建第2个被观察者
                Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        Log.d(TAG, "被观察者2发送了事件A");
                        emitter.onNext("A");
                        Thread.sleep(500);

                        Log.d(TAG, "被观察者2发送了事件B");
                        emitter.onNext("B");
                        Thread.sleep(500);

                        Log.d(TAG, "被观察者2发送了事件C");
                        emitter.onNext("C");
                        Thread.sleep(500);

                        Log.d(TAG, "被观察者2发送了事件D");
                        emitter.onNext("D");
                        Thread.sleep(500);

                        emitter.onComplete();
                    }
                }).subscribeOn(Schedulers.newThread());// 设置被观察者2在工作线程2中工作
                // 假设不作线程控制，则该两个被观察者会在同一个线程中工作，即发送事件存在先后顺序，而不是同时发送

                //使用zip变换操作符进行事件合并
                // 注：创建BiFunction对象传入的第3个参数 = 合并后数据的数据类型
                Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
                    @Override
                    public String apply(Integer integer, String string) throws Exception {
                        return integer + string;
                    }
                }).subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(String value) {
                        Log.d(TAG, "最终接收到的事件 =  " + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
                break;
            case R.id.btn4:
                Observable.combineLatest(
                        Observable.just(1L, 2L, 3L), // 第1个发送数据事件的Observable
                        Observable.intervalRange(0, 3, 1, 1, TimeUnit.SECONDS), // 第2个发送数据事件的Observable：从0开始发送、共发送3个数据、第1次事件延迟发送时间 = 1s、间隔时间 = 1s
                        new BiFunction<Long, Long, Long>() {
                            @Override
                            public Long apply(Long o1, Long o2) throws Exception {
                                // o1 = 第1个Observable发送的最新（最后）1个数据
                                // o2 = 第2个Observable发送的每1个数据
                                Log.e(TAG, "合并的数据是： " + o1 + " " + o2);
                                return o1 + o2;
                                // 合并的逻辑 = 相加
                                // 即第1个Observable发送的最后1个数据 与 第2个Observable发送的每1个数据进行相加
                            }
                        }).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long s) throws Exception {
                        Log.e(TAG, "合并的结果是： " + s);
                    }
                });
                break;
            case R.id.btn5:
                Observable.just(1, 2, 3, 4)
                        .reduce(new BiFunction<Integer, Integer, Integer>() {
                            // 在该复写方法中复写聚合的逻辑,要啥写啥
                            @Override
                            public Integer apply(@NonNull Integer s1, @NonNull Integer s2) throws Exception {
                                Log.e(TAG, "本次计算的数据是： " + s1 + " 乘 " + s2);
                                return s1 * s2;
                                // 本次聚合的逻辑是：全部数据相乘起来
                                // 原理：第1次取前2个数据相乘，之后每次获取到的数据 = 返回的数据x原始下1个数据每
                            }
                        }).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer s) throws Exception {
                        Log.e(TAG, "最终计算的结果是： " + s);

                    }
                });
                break;
            default:
                break;
        }

    }
}
