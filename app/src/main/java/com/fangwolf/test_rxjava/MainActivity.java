package com.fangwolf.test_rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";
    Button fire;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fire = findViewById(R.id.fire);
        findViewById(R.id.tv);

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
                        Log.e(TAG, "处理 onNext事件"+integer);
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
    }
}
