package com.example.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationBarView;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
//    public static final int REQUEST_PERMISSION = 11;
    private PermissionSupport permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      checkPermission();
        permissionCheck();

        NavigationBarView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);
        getSupportActionBar().setTitle("                             스마트 그릇");


    }
    // 권한 체크
    private void permissionCheck() {

        // PermissionSupport.java 클래스 객체 생성
        permission = new PermissionSupport(this, this);

        // 권한 체크 후 리턴이 false로 들어오면
        if (!permission.checkPermission()){
            //권한 요청
            permission.requestPermission();
        }
    }

    // Request Permission에 대한 결과 값 받아와
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //여기서도 리턴이 false로 들어온다면 (사용자가 권한 허용 거부)
        if (!permission.permissionResult(requestCode, permissions, grantResults)) {
            // 다시 permission 요청
            permission.requestPermission();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


//    //권한 확인
//    public void checkPermission() {
//        int permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
//        int permissionRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
//        int permissionWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        //권한이 없으면 권한 요청
//        if (permissionCamera != PackageManager.PERMISSION_GRANTED
//                || permissionRead != PackageManager.PERMISSION_GRANTED
//                || permissionWrite != PackageManager.PERMISSION_GRANTED) {
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
//                Toast.makeText(this, "이 앱을 실행하기 위해 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
//            }
//
//            ActivityCompat.requestPermissions(this, new String[]{
//                    Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
//
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case REQUEST_PERMISSION: {
//                // 권한이 취소되면 result 배열은 비어있다.
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    Toast.makeText(this, "권한 확인", Toast.LENGTH_LONG).show();
//
//                } else {
//                    Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();
//                    finish(); //권한이 없으면 앱 종료
//                }
//            }
//        }
//    }

    private NavigationBarView.OnItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                switch(item.getItemId()){
                    case R.id.nav_food:
                        selectedFragment = new FoodFragment();
                        break;
                    case R.id.nav_record:
                        selectedFragment = new RecordFragment();
                        break;
                    case R.id.nav_communication:
                        selectedFragment = new CommunicationFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();

                return true;
            };
}