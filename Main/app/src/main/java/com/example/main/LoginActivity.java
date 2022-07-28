package com.example.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private String str;
    private EditText editText_email, editText_password;
    private RelativeLayout relativelayout_login;
    private Button button_log_in;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("                             스마트 그릇");

        editText_email = findViewById(R.id.editText_email);
        editText_password = findViewById(R.id.editText_password);
        button_log_in = findViewById(R.id.log_in);

        button_log_in.setClickable(true);
        button_log_in.setOnClickListener(v -> {
            String email = editText_email.getText().toString();
            String password = editText_password.getText().toString();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            startActivity(intent);
        });
    }
}

