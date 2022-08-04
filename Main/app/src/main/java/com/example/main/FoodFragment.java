package com.example.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

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
    public Button btn_record;
    public ImageView imgV;
    public File file;


    private static final int REQUEST_IMAGE_CODE = 101;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_food, container, false);

        get_btn_picture = view.findViewById(R.id.btn_picture);
        btn_record = view.findViewById(R.id.btn_record);
        imgV = view.findViewById(R.id.imageView);

        get_btn_picture.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultPicture.launch(intent);
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
