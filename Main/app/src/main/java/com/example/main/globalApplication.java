package com.example.main;

import com.kakao.sdk.common.KakaoSdk;;
import android.app.Application;

public class globalApplication extends Application {
    private static globalApplication instance;

    public void onCreate(){
        super.onCreate();
        instance = this;

        //네이티브 앱 키로 초기화
        KakaoSdk.init(this, "14b7d4358d5ab0adead5989689abed59");
    }
}
