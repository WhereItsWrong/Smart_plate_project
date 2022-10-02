package com.example.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.markers.KMutableList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private Button button_log_in;
    private ImageButton button_kakao;
    private static final String TAG = "사용자";
    private TextView signup_text;
    private static globalApplication instance;
    private static String profile_img, nickname_u;
    private String BASE_URL = "http://10.0.2.2:3000";

    public static final String DATABASE_NAME = "user";
    public static final String srl = "jdbc:mysql://database-1.ctf5tuczl6kc.ap-northeast-2.rds.amazonaws.com:3306/" + DATABASE_NAME;
    public static final String username = "admin", password = "park6498";

    public static final String TABLE_NAME = "user_vit";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("                             스마트 그릇");
        button_log_in = findViewById(R.id.log_in); //login 버튼
        button_kakao = findViewById(R.id.kakao_login_button); //kakao 연동 버튼
        signup_text = findViewById(R.id.log_out); //가입 버튼



        button_log_in.setOnClickListener(v -> {

        });

        button_kakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)) {
                    login();
                    System.out.println(profile_img);

                } else {
                    accountLogin();
                }

            }
        });

        signup_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserApiClient.getInstance().logout(error -> {
                    if (error != null) {
                        Log.e(TAG, "로그아웃 실패, SDK에서 토큰 삭제됨", error);
                    } else {
                        Log.e(TAG, "로그아웃 성공, SDK에서 토큰 삭제됨");
                    }
                    return null;
                });
            }
        });
    }

    public void login() {
        String TAG = "login()";
        UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, (oAuthToken, error) -> {
            //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            if (error != null) {
                Log.e(TAG, "로그인 실패", error);
            } else if (oAuthToken != null) {
                Log.i(TAG, "로그인 성공(토큰) : " + oAuthToken.getAccessToken());
                getUserInfo();
                //startActivity(intent);
            }
            return null;
        });
    }

    public void accountLogin() {
        String TAG = "accountLogin()";
        UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, (oAuthToken, error) -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            if (error != null) {
                Log.e(TAG, "로그인 실패", error);
            } else if (oAuthToken != null) {
                Log.i(TAG, "로그인 성공(토큰) : " + oAuthToken.getAccessToken());
                getUserInfo();
                startActivity(intent);
            }
            return null;
        });
    }


    //kakao 정보추출
    public void getUserInfo() {
        String TAG = "getUserInfo()";
        UserApiClient.getInstance().me((user, meError) -> {
            if (meError != null) {
                Log.e(TAG, "사용자 정보 요청 실패", meError);
            } else {
                System.out.println("로그인 완료");
                Log.i(TAG, user.toString());
                {
                    Log.i(TAG, "사용자 정보 요청 성공" +
                            "\n회원번호: " + user.getId() +
                            "\n이메일: " + user.getKakaoAccount().getEmail());
                }
                Account user1 = user.getKakaoAccount();
                System.out.println("사용자 계정" + user.getProperties());

                String account = user1.toString();
                nickname_u = user1.getProfile().getNickname();
                profile_img = user1.getProfile().getProfileImageUrl();

                Toast myToast = Toast.makeText(this.getApplicationContext(), nickname_u, Toast.LENGTH_SHORT);
                myToast.show();
            }
            return null;
        });
    }

    // 키해시 얻는 방법
    public String getKeyHash() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            if (packageInfo == null) {
                return null;
            }
            for (Signature signature : packageInfo.signatures) {
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    return android.util.Base64.encodeToString(md.digest(), Base64.NO_WRAP);
                } catch (NoSuchAlgorithmException e) {
                    Log.w("getKeyHash", "Unable to get MessageDigest. signature=" + signature, e);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("getPackageInfo", "Unable to getPackageInfo");
        }
        return null;
    }
}

