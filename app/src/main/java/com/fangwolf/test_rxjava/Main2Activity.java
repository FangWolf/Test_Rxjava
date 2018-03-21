package com.fangwolf.test_rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "Main2Activity";
    Button btn0;
    Button btn0_0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        findViewById(R.id.btn0).setOnClickListener(this);
        findViewById(R.id.btn0_0).setOnClickListener(this);
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
            default:
                break;
        }

    }
}
