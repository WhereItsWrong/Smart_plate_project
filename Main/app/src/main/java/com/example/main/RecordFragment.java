package com.example.main;

import static android.content.Context.MODE_NO_LOCALIZED_COLLATORS;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class RecordFragment extends Fragment {
    public String fname=null;
    public String fname_img=null;
    public String str=null;
    public Bitmap bitmap = null;
    public CalendarView calendarView;
    public Button cha_Btn,del_Btn,save_Btn, select_btn2;
    public TextView diaryTextView,textView2,textView3;
    public EditText contextEditText;
    public ImageView record_Img;
    public String imgpath = null;

    public CharSequence select_text = null;

    public int day_tmp = 0;
    public int month_tmp = 0;
    public int year_tmp = 0;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_record, container, false);

        calendarView=view.findViewById(R.id.calendarView);
        diaryTextView=view.findViewById(R.id.diaryTextView);
        save_Btn=view.findViewById(R.id.save_Btn);
        del_Btn=view.findViewById(R.id.del_Btn);
        cha_Btn=view.findViewById(R.id.cha_Btn);
        select_btn2 = view.findViewById(R.id.select_btn2);
        textView2=view.findViewById(R.id.textView2);
        textView3=view.findViewById(R.id.textView3);
        contextEditText=view.findViewById(R.id.contextEditText);
        record_Img=view.findViewById(R.id.recordImg);
//        //로그인 및 회원가입 엑티비티에서 이름을 받아옴
//        Intent intent=getIntent();
//        String name=intent.getStringExtra("userName");
//        final String userID=intent.getStringExtra("userID");
//        textView3.setText(name+"님의 음식 기록");

// 비트맵 불러오기

        //레코드 프레그먼트 시작부터 뜨게 하기위한 구문
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        int year = Integer.parseInt(yearFormat.format(currentTime));
        int month = Integer.parseInt(monthFormat.format(currentTime));
        int day = Integer.parseInt(dayFormat.format(currentTime));
        diaryTextView.setText(String.format("%d / %d / %d",year,month,day));
//                contextEditText.setText(contextEditText.getText());
        //todo 윤서한테 물어봐야댐

        checkDay(year,month,day);
        //위 사항 종료

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                record_Img.setVisibility(View.INVISIBLE);

                day_tmp = dayOfMonth;
                month_tmp = month;
                year_tmp = year;

                diaryTextView.setText(String.format("%d / %d / %d",year,month+1,dayOfMonth));
//                contextEditText.setText(contextEditText.getText());
                //todo 윤서한테 물어봐야댐

                checkDay(year,month,dayOfMonth);
            }
        });
        save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDiary(fname);
                str=contextEditText.getText().toString();

                textView2.setText(str);
                save_Btn.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.VISIBLE);
                del_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.VISIBLE);

            }
        });
        select_btn2.setOnClickListener(new View.OnClickListener(){
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
                                select_btn2.setText(select_text);

                                diaryTextView.setVisibility(View.VISIBLE);
                                save_Btn.setVisibility(View.VISIBLE);
                                contextEditText.setVisibility(View.VISIBLE);
                                textView2.setVisibility(View.INVISIBLE);
                                cha_Btn.setVisibility(View.INVISIBLE);
                                del_Btn.setVisibility(View.INVISIBLE);
                                record_Img.setVisibility(View.INVISIBLE);
                                checkDay(year_tmp,month_tmp,day_tmp);

                            }
                        })
                        .setCancelable(false)
                        .show();

            }
        });
        return view;
    }
    public void  checkDay(int cYear,int cMonth,int cDay){
        fname=""+cYear+"-"+(cMonth+1)+""+"-"+cDay+select_btn2.getText()+".txt";//저장할 파일 이름설정
        fname_img=""+ cYear +"-"+(cMonth+1)+""+"-"+cDay+select_btn2.getText()+
                ".png";

        FileInputStream fis = null;//FileStream fis 변수

        try{
            fis = getContext().openFileInput(fname);

            byte[] fileData=new byte[fis.available()];
            fis.read(fileData);
            fis.close();

            str=new String(fileData);


            imgpath = "data/data/com.example.main/files/";
            imgpath += fname_img;

            Bitmap bm = BitmapFactory.decodeFile(imgpath);

            imgpath = null;

            contextEditText.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.VISIBLE);
            textView2.setText(str);

            record_Img.setVisibility(View.VISIBLE);
            record_Img.setImageBitmap(bm);


            save_Btn.setVisibility(View.INVISIBLE);
            cha_Btn.setVisibility(View.VISIBLE);
            del_Btn.setVisibility(View.VISIBLE);





            cha_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    contextEditText.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText(str);

                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    textView2.setText(contextEditText.getText());
                }

            });
            del_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText("");
                    contextEditText.setVisibility(View.VISIBLE);
                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    removeDiary(fname);
                }
            });
            if(textView2.getText()==null){
                textView2.setVisibility(View.INVISIBLE);
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @SuppressLint("WrongConstant")
    public void removeDiary(String readDay){
        FileOutputStream fos=null;
        record_Img.setVisibility(View.INVISIBLE);
        imgpath = "data/data/com.example.main/files/";

        try{
            File file = new File(imgpath);
            File[] flist = file.listFiles();
            for (int i = 0; i < flist.length; i++) {
                String tmp = flist[i].getName();
                if(tmp.equals(fname)){
                    flist[i].delete();
                }
            }

            // 텍스트 파일 삭제
//            fos= getContext().openFileOutput(readDay, Context.MODE_PRIVATE);
//            String content="";
//            fos.write((content).getBytes());
//            fos.close();

            //  비트맵 이미지 삭제


            try {
                File file_img = new File(imgpath);
                File[] flist_img = file_img.listFiles();
                for (int i = 0; i < flist_img.length; i++) {
                    String tmp = flist_img[i].getName();
                    if(tmp.equals(fname_img)){
                        flist_img[i].delete();
                    }
                }
                imgpath = null;
            }
            catch(Exception e){

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @SuppressLint("WrongConstant")
    public void saveDiary(String readDay){
        FileOutputStream fos=null;
        try{
            fos= getContext().openFileOutput(readDay, Context.MODE_PRIVATE);
            String content=contextEditText.getText().toString();
            fos.write((content).getBytes());
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
