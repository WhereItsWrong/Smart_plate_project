package com.example.main;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;


import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FoodFragment extends Fragment {

    public MainActivity beaconScan = new MainActivity();
    Bitmap bitmap;

    public Button get_btn_picture;
    public Button save_btn;

    public Button del_btn;
    public ImageView imgV;


    public TextView plate_White;
    public TextView plate_Red;
    public TextView plate_Black;
    public TextView plate_Blue;
    public TextView date;

    public Spinner spinner;
    public ResultView resultView;

    //기록 페이지 전달용 변수(텍스트)

    public static String Message = "";
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

    String date_str = year +"/"+(month)+""+"/"+day;


    private static final int REQUEST_IMAGE_CODE = 101;



    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        //TODO 스태틱을 사용해 MainActiviy의 비콘 메세지(그릇당 무게값)을 전달받음.
        //TODO 안드로이드에서 생명주기에 의해 스태틱 사용기 지양됨으로 추후 수정이 가능하다면(가능하다면...) 수정예정

        View view = inflater.inflate(R.layout.fragment_food, container, false);

        //사진 버튼 선언
        get_btn_picture = view.findViewById(R.id.btn_picture);
        resultView = view.findViewById(R.id.resultView);

        date = view.findViewById(R.id.date);

        save_btn = view.findViewById(R.id.save_btn);
        del_btn = view.findViewById(R.id.delect_btn);

        //그릇 객체 선언
        plate_Black = view.findViewById(R.id.plate_Black);
        plate_White = view.findViewById(R.id.plate_White);
        plate_Blue = view.findViewById(R.id.plate_Blue);
        plate_Red = view.findViewById(R.id.plate_Red);

        spinner = view.findViewById(R.id.spinner);

        imgV = view.findViewById(R.id.imageView);

        date.setText(date_str);

        final String[] items = {"선택","아침","점심","저녁"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String> (
                this.getActivity() , android.R.layout.simple_spinner_item,items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);





        //TODO 저장버튼
        save_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                FileOutputStream fos = null;

                fname += spinner.getSelectedItem().toString() + ".txt";
                fname_img += spinner.getSelectedItem().toString() + ".png";


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

            }
        });
        del_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                imgV.setImageBitmap(null);

                plate_Black.setText(null);
                plate_Red.setText(null);
                plate_White.setText(null);
                plate_Blue.setText(null);

                Message = "";

                resultView.setVisibility(View.INVISIBLE);
            }
        });
        //        select_btn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                final CharSequence[] oItems = {"아침","점심","저녁"};
//
//                AlertDialog.Builder oDialog = new AlertDialog.Builder(getActivity(),
//                        android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
//
//                oDialog.setTitle("식사 선택")
//                        .setItems(oItems, new DialogInterface.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which)
//                            {
//                                select_text = oItems[which];
//                                select_btn.setText(select_text);
//                            }
//                        })
//                        .setCancelable(false)
//                        .show();
//            }
//        });
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

