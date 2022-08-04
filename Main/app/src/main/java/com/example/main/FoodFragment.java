package com.example.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
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

    Bitmap bitmap;

    public Button get_btn_picture;
    public Button button3;
    public ImageView imgV;
    public File file;

    public TextView plate_W;
    public TextView plate_R;
    public TextView plate_B;
    public TextView plate4;

    private BeaconScan beaconScan;


    private static final int REQUEST_IMAGE_CODE = 101;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_food, container, false);


        get_btn_picture = view.findViewById(R.id.btn_picture);
        button3 = view.findViewById(R.id.button3);
        imgV = view.findViewById(R.id.imageView);

        button3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                try{beaconScan.BeaconScanStart();}
                catch(NullPointerException e){

                }


            }
        });
        get_btn_picture.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultPicture.launch(intent);
                try{beaconScan.BeaconScanStart();}
                catch(NullPointerException e){

                }
                beaconScan.BeaconScanStart();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (beaconScan.white_plate != "") {
                            plate_W.setVisibility(View.VISIBLE);
                            plate_W.setText(
                                    "흰색 그릇\n" +
                                            "무게" + beaconScan.white_plate);
                        }
                        if (beaconScan.red_plate != "") {
                            plate_R.setVisibility(View.VISIBLE);
                            plate_R.setText(
                                    "빨간색 그릇\n" +
                                            "무게" + beaconScan.white_plate);
                        }
                        if (beaconScan.black_plate != "") {
                            plate_B.setVisibility(View.VISIBLE);
                            plate_B.setText(
                                    "검정색 그릇\n" +
                                            "무게" + beaconScan.white_plate);
                        }
                    }
                }, 1000);
                beaconScan.BeaconScanStop();
                // todo 음식 무게 정보 표시
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
