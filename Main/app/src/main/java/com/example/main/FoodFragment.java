package com.example.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;

public class FoodFragment extends Fragment {

    public MainActivity beaconScan = new MainActivity();
    Bitmap bitmap;

    public Button get_btn_picture;
    public Button button3;
    public ImageView imgV;
    public File file;

    public TextView plate_White;
    public TextView plate_Red;
    public TextView plate_Black;
    public TextView plate_Blue;

    //기록 페이지 전달용 변수(텍스트)
    static String plate_White_Message = "";
    static String plate_Black_Message = "";
    static String plate_Red_Message = "";
    static String plate_Blue_Message = "";
    static Bitmap sendImg;

    private static final int REQUEST_IMAGE_CODE = 101;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        //TODO 스태틱을 사용해 MainActiviy의 비콘 메세지(그릇당 무게값)을 전달받음.
        //TODO 안드로이드에서 생명주기에 의해 스태틱 사용기 지양됨으로 추후 수정이 가능하다면(가능하다면...) 수정예정

        View view = inflater.inflate(R.layout.fragment_food, container, false);

        //사진 버튼 선언
        get_btn_picture = view.findViewById(R.id.btn_picture);

        //TODO 버튼 미정
        button3 = view.findViewById(R.id.button3);

        //그릇 객체 선언
        plate_Black = view.findViewById(R.id.plate_Black);
        plate_White = view.findViewById(R.id.plate_White);
        plate_Blue = view.findViewById(R.id.plate_Blue);
        plate_Red = view.findViewById(R.id.plate_Red);


        imgV = view.findViewById(R.id.imageView);


        //TODO 버튼 미정
        button3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
// 스태틱을 사용해 MainActivty의 비콘 메세지를 해당 클래스로 옮기기 위해 노력햇던 흔적들(혹시 모르니 삭제 x)
//
//                Thread1 thread1 = new Thread1();
//                thread1.start();

//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        beaconScan.BeaconScanStop();
//                        try {
//                            plate_B.setVisibility(View.VISIBLE);
//                            plate_B.setText(beaconMessage);
//                            Log.d("---",beaconMessage);
//                        }
//                        catch(NullPointerException e){
//                            e.printStackTrace();
//                        }
//                    }
//                }, 3000); //딜레이 타임 조절
            }
        });

        //TODO 카메라 버튼 클릭 3초후 비콘 무게정보 표시
        get_btn_picture.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultPicture.launch(intent);

                //3초 이후 무게정보 표시
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(!MainActivity.plate_White.equals("")) {
                            plate_White.setVisibility(View.VISIBLE);
                            plate_White.setText("흰색 그릇 :" +  MainActivity.plate_White + "g");
                            plate_White_Message += "흰색 그릇 :" +  MainActivity.plate_White + "g";
                        }
                        if(!MainActivity.plate_Black.equals("")) {
                            plate_Black.setVisibility(View.VISIBLE);
                            plate_Black.setText("검정색 그릇 :" + MainActivity.plate_Black + "g" );
                            plate_Black_Message += "검정색 그릇 :" +  MainActivity.plate_White + "g";
                        }
                        if(!MainActivity.plate_Red.equals("")) {
                            plate_Red.setVisibility(View.VISIBLE);
                            plate_Red.setText("빨간색 그릇 : " + MainActivity.plate_Red + "g");
                            plate_Red_Message += "빨간색 그릇 :" +  MainActivity.plate_White + "g";
                        }
                        if(!MainActivity.plate_Blue.equals("")) {
                            plate_Blue.setVisibility(View.VISIBLE);
                            plate_Blue.setText("파란색 그릇 :" + MainActivity.plate_Blue + "g");
                            plate_Blue_Message += "파란색 그릇 :" +  MainActivity.plate_White + "g";
                        }
                        Log.d("---",MainActivity.beaconMessage);

                    }
                }, 3000); //딜레이 타임 조절





// 비콘 관련 메소드가 해당 클래스에서 실행이 안된다는것을(객체가 다르기때문) 모르기 전에 시도햇던 흔적들, 혹시 모르니 삭제 x
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (beaconScan.white_plate != "") {
//                            plate_W.setVisibility(View.VISIBLE);
//                            plate_W.setText(
//                                    "흰색 그릇\n" +
//                                            "무게" + beaconScan.white_plate);
//                        }
//                        if (beaconScan.red_plate != "") {
//                            plate_R.setVisibility(View.VISIBLE);
//                            plate_R.setText(
//                                    "빨간색 그릇\n" +
//                                            "무게" + beaconScan.white_plate);
//                        }
//                        if (beaconScan.black_plate != "") {
//                            plate_B.setVisibility(View.VISIBLE);
//                            plate_B.setText(
//                                    "검정색 그릇\n" +
//                                            "무게" + beaconScan.white_plate);
//                        }
//
//                    }
//                }, 3000);


            }
        });

        return view; //attachToRoot: false 안함.
    }
//    public class Thread1 extends Thread{
//        public void run(){
//            if(beaconScan.getBleStateCheck() == true){ // [블루투스 및 GPS 기능이 모두 활성 상태]
//                beaconScan.BeaconScanStart(); //[비콘 스캔 시작 실시]
//            }
//
//            while(true){
//                beaconMessage = beaconScan.getBeaconMessage();
//
//            }
//        }
//    }

    ActivityResultLauncher<Intent> activityResultPicture = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //결과 ok, 데이터 null 아니라면
                    if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                        Bundle extras = result.getData().getExtras();
                        bitmap = (Bitmap) extras.get("data");

                        imgV.setImageBitmap(bitmap);
                        sendImg = bitmap;
                    }
                }
            }
    );

}

