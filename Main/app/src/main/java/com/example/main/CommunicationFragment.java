package com.example.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultRegistry;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.io.File;

public class CommunicationFragment extends Fragment {

    private ValueCallback mFilePathCallback;
    private static final String TAG = "MyTag";
    private final static int FILECHOOSER_NORMAL_REQ_CODE = 0;

    @SuppressLint("JavascriptInterface")
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_communication, container, false); //attachToRoot: false 안함.

        WebView webView = view.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true); //javascript 사용 가능하게 하는 코드
        //webView.setWebViewClient(new WebViewClientClass()); //  페이지 컨트롤을 위한 기본적인 함수, 다양한 요청, 알림을 수신하는 기능을 한다.
        webView.getSettings().setDomStorageEnabled(true);
        //webView.loadUrl("javascript:");

        webView.setWebChromeClient(new WebChromeClient()); //쾌적한 환경조성 세팅
        //webView.loadUrl("localhost:3000"); //url 주소 가저오는 코드


        webView.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    if(keyCode == KeyEvent.KEYCODE_BACK){
                        if(webView!=null){
                            if(webView.canGoBack()){
                                webView.goBack();
                            }else{
                                getActivity().onBackPressed();
                            }
                        }
                    }
                }return true;
            }
        });

//        webView.setWebChromeClient(new WebChromeClient(){ //input type='file'의 버튼을 호출하는 함수)
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
//                Log.d(TAG, "***** onShowFileChooser()"); //상태확인용
//
//                //파일 업로드 하는 것
//                if (mFilePathCallback != null) {
//                    //파일을 한번 오픈했으면 mFilePathCallback 를 초기화를 해줘야함
//                    //그렇지 않으면 다시 파일 오픈 시 열리지 않는 경우 발생
//                    mFilePathCallback.onReceiveValue(null);
//                    mFilePathCallback = null;
//                }
//                mFilePathCallback = filePathCallback;
//
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                intent.setType("*/*");  //모든 contentType 파일 표시
//                startActivityForResult(intent, 0);
//                return true;
//            }
//        });

        return view;
    }

//    @Override //안드로이드에서 파일 업로드 가능하게 하는 것.
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.d(TAG, "***** onActivityResult() - requestCode : " + requestCode);
//        Log.d(TAG, "***** onActivityResult() - resultCode : " + resultCode);
//        Log.d(TAG, "***** onActivityResult() - data : " + data);
//        /* 파일 선택 완료 후 처리 */
//        switch (requestCode) {
//            case FILECHOOSER_NORMAL_REQ_CODE:
//                //fileChooser 로 파일 선택 후 onActivityResult 에서 결과를 받아 처리함
//                if (resultCode == Activity.RESULT_OK) {
//                    //파일 선택 완료 했을 경우
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        mFilePathCallback.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
//                    } else {
//                        mFilePathCallback.onReceiveValue(new Uri[]{data.getData()});
//                    }
//                    mFilePathCallback = null;
//                } else {
//                    //cancel 했을 경우
//                    if (mFilePathCallback != null) {
//                        mFilePathCallback.onReceiveValue(null);
//                        mFilePathCallback = null;
//                    }
//                }
//                break;
//            default:
//                break;
//        }
//
//    }

//    private class WebViewClientClass extends WebViewClient{
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//            return true;
//        }
//    }



}



