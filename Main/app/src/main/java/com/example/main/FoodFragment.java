package com.example.main;

import static android.content.Context.MODE_NO_LOCALIZED_COLLATORS;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FoodFragment extends Fragment {

    public MainActivity beaconScan = new MainActivity();
    Bitmap bitmap;

    public Button get_btn_picture;
    public Button button3;
    public Button select_btn;
    public ImageView imgV;
    public File file;

    public TextView plate_White;
    public TextView plate_Red;
    public TextView plate_Black;
    public TextView plate_Blue;

    //기록 페이지 전달용 변수(텍스트)

    public String Message = "";
    public CharSequence select_text = null;

    Date currentTime = Calendar.getInstance().getTime();

    SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

    int year = Integer.parseInt(yearFormat.format(currentTime));
    int month = Integer.parseInt(monthFormat.format(currentTime));
    int day = Integer.parseInt(dayFormat.format(currentTime));

    //내장메모리에 저장할 파일 이름(형식 포함)
    String fname=""+ year +"-"+(month)+""+"-"+day;
    String fname_img=""+ year +"-"+(month)+""+"-"+day;


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
        select_btn = view.findViewById(R.id.select_btn);


        imgV = view.findViewById(R.id.imageView);


        //TODO 저장버튼
        button3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                FileOutputStream fos = null;

                fname += select_text + ".txt";
                fname_img += select_text + ".png";

                try{
                    fos= getContext().openFileOutput(fname, Context.MODE_PRIVATE);
                    fos.write((Message).getBytes());
                    fos.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

                fos = null;

                try{
                    fos= getContext().openFileOutput(fname_img, Context.MODE_PRIVATE);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100 , fos);
                    fos.flush();
                    fos.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


        //TODO 카메라 버튼 클릭 3초후 비콘 무게정보 표시
        get_btn_picture.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultPicture.launch(intent);

                Message = "";
                //3초 이후 무게정보 표시
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if(!MainActivity.plate_White.equals("")) {
                            plate_White.setVisibility(View.VISIBLE);
                            plate_White.setText("흰색 그릇 :" +  MainActivity.plate_White + "g");
                            Message += "흰색 그릇 :" +  MainActivity.plate_White + "g";
                        }
                        if(!MainActivity.plate_Black.equals("")) {
                            plate_Black.setVisibility(View.VISIBLE);
                            plate_Black.setText("검정색 그릇 :" + MainActivity.plate_Black + "g" );
                            Message += "검정색 그릇 :" + MainActivity.plate_Black + "g";
                        }
                        if(!MainActivity.plate_Red.equals("")) {
                            plate_Red.setVisibility(View.VISIBLE);
                            plate_Red.setText("빨간색 그릇 : " + MainActivity.plate_Red + "g");
                            Message += "빨간색 그릇 : " + MainActivity.plate_Red + "g";

                        }
                        if(!MainActivity.plate_Blue.equals("")) {
                            plate_Blue.setVisibility(View.VISIBLE);
                            plate_Blue.setText("파란색 그릇 :" + MainActivity.plate_Blue + "g");
                            Message += "파란색 그릇 :" + MainActivity.plate_Blue + "g";
                        }
                        Log.d("---",MainActivity.beaconMessage);

                    }
                }, 3000); //딜레이 타임 조절

            }

        });
        select_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final CharSequence[] oItems = {"아침","점심","저녁"};

                AlertDialog.Builder oDialog = new AlertDialog.Builder(getActivity(),
                        android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);

                oDialog.setTitle("식사 선택")
                        .setItems(oItems, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                select_text = oItems[which];
                                select_btn.setText(select_text);
                            }
                        })
                        .setCancelable(false)
                        .show();

            }
        });
        return view; //attachToRoot: false 안함.
    }




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

                    }
                }
            }
    );

}

