package com.example.main;

import android.app.Activity;
import android.app.MediaRouteButton;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class signup extends AppCompatActivity {
    private EditText nick, e_mail, pwd;
    private Button sign_btn;
    private ServiceApi service;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_dialog);

        nick = (EditText) findViewById(R.id.nameEdit);
        e_mail = (EditText) findViewById(R.id.emailEdit);
        pwd = (EditText) findViewById(R.id.passwordEdit);
        sign_btn = (Button) findViewById(R.id.signup);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptJoin();
            }
        });
    }

    private void attemptJoin() {
        nick.setError(null);
        e_mail.setError(null);
        pwd.setError(null);

        String name = nick.getText().toString();
        String email = e_mail.getText().toString();
        String password = pwd.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 패스워드의 유효성 검사
        if (password.isEmpty()) {
            e_mail.setError("비밀번호를 입력해주세요.");
            focusView = e_mail;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            pwd.setError("6자 이상의 비밀번호를 입력해주세요.");
            focusView = pwd;
            cancel = true;
        }

        // 이메일의 유효성 검사
        if (email.isEmpty()) {
            e_mail.setError("이메일을 입력해주세요.");
            focusView = e_mail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            e_mail.setError("@를 포함한 유효한 이메일을 입력해주세요.");
            focusView = e_mail;
            cancel = true;
        }

        // 이름의 유효성 검사
        if (name.isEmpty()) {
            nick.setError("이름을 입력해주세요.");
            focusView = nick;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            startJoin(new JoinData(name, email, password));
        }
    }

    private void startJoin(JoinData data) {
        service.userJoin(data).enqueue(new Callback<JoinResponse>() {
            @Override
            public void onResponse(Call<JoinResponse> call, Response<JoinResponse> response) {
                JoinResponse result = response.body();
                Toast.makeText(signup.this, result.getMessage(), Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

                if (result.getCode() == 200) {
                    finish();
                }
            }

            @Override
            public void onFailure(Call<JoinResponse> call, Throwable t) {
                Toast.makeText(signup.this, "회원가입 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("회원가입 에러 발생", t.getMessage());
            }
        });
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }
}