package com.example.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationBarView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MainActivity extends AppCompatActivity implements BeaconConsumer {
    // 홍승표 권한 허용의 잔재
//    public static final int REQUEST_PERMISSION = 11;
    // TODO 처음에는 비콘 클래스를(Activity가 아닌) 따로 생성하여 FoodFragment에서 객체 선언 후 처리 시도,
    // TODO 비콘을 처리하는 클래스는 Activity 클래스가 아니면 실행되지 않음 - BeaconScan.java 삭제후 MainActivy.java에 통합
    // TODO 비콘 메세지를 FoodFragent로 보낼 방법이 없음으로 스태틱 선언- 추후 방법 찾을 시 수정할 것 (스태틱 사용이 안스에서 생명주기로 인해 추천되지 않음)

    // TODO log를 위한 비콘 메세지
    static String beaconMessage = "";

    //TODO [그릇색별 변수]
    static String plate_Black = "";
    static String plate_White = "";
    static String plate_Red = "";
    static String plate_Blue = "";

    //TODO 권한객체 선언
    private PermissionSupport permission;

    //TODO [실시간 비콘 스캐닝을 하기 위한 변수 및 객체 선언 실시]
    private BeaconManager beaconManager; // [비콘 매니저 객체]
    private List<Beacon> beaconList = new ArrayList<>(); // [실시간 비콘 감지 배열]

    //TODO 비콘을 위한 변수 선언
    int beaconScanCount = 1; // [비콘 스캔 횟수를 카운트하기 위함]
    ArrayList beaconFormatList = new ArrayList<>(); // [스캔한 비콘 리스트를 포맷해서 저장하기 위함]

    //TODO [비콘 스캐닝을 위한 초기 설정]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO 홍승표 버전 권한 설정 - 논의 필요
//      checkPermission();

        permissionCheck();

        Log.d("---","---");
        Log.d("//===========//","================================================");
        Log.d("","\n"+"[BeaconScan > onCreate() 메소드 : 액티비티 시작 실시]");
        Log.d("//===========//","================================================");
        Log.d("---","---");

        //TODO [비콘 매니저 초기 설정 및 레이아웃 지정 실시]
        BeaconSettiong();
        if(getBleStateCheck() == true){ // [블루투스 및 GPS 기능이 모두 활성 상태]
            BeaconScanStart(); //[비콘 스캔 시작 실시]
        }


        NavigationBarView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);
        getSupportActionBar().setTitle("                             스마트 그릇");


    }
    public void BeaconSettiong(){
        Log.d("---","---");
        Log.d("//===========//","================================================");
        Log.d("","\n"+"[BeaconScan > BeaconSettiong() 메소드 : 비콘 매니저 초기 설정 수행]");
        Log.d("","\n"+"[레이아웃 : m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25]");
        Log.d("//===========//","================================================");
        Log.d("---","---");
        try {
            //TODO [비콘 매니저 생성]
            beaconManager = BeaconManager.getInstanceForApplication(MainActivity.this);

            //TODO [블루투스가 스캔을 중지하지 않도록 설정]
            beaconManager.setEnableScheduledScanJobs(false);

            //TODO [레이아웃 지정 - IOS , Android 모두 스캔 가능]
            beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //TODO [실시간 비콘 스캐닝 시작]
    public void BeaconScanStart(){
        Log.d("---","---");
        Log.w("//===========//","================================================");
        Log.d("","\n"+"[BeaconScan > BeaconScanStart() 메소드 : 실시간 비콘 스캐닝 시작]");
        Log.w("//===========//","================================================");
        Log.d("---","---");


        try {
            //TODO [변수값 초기화 실시]
            beaconScanCount = 1;

            //TODO [beaconManager Bind 설정]
            beaconManager.bind(MainActivity.this);

            //TODO [실시간 비콘 스캔 수행 핸들러 호출]
            BeaconHandler.sendEmptyMessage(0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //TODO [실시간 비콘 스캐닝 종료]
    public void BeaconScanStop(){
        Log.d("---","---");
        Log.e("//===========//","================================================");
        Log.d("","\n"+"[BeaconScan > BeaconScanStop() 메소드 : 실시간 비콘 스캐닝 종료]");
        Log.e("//===========//","================================================");
        Log.d("---","---");
        try {
            //TODO [변수값 초기화 실시]
            beaconScanCount = 1;

            //TODO [핸들러 사용 종료]
            BeaconHandler.removeMessages(0);
            BeaconHandler.removeCallbacks(null);

            //TODO [beaconManager Bind 해제]
            beaconManager.unbind(MainActivity.this);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //TODO [실시간 비콘 스캐닝 감지 부분]
    @Override
    public void onBeaconServiceConnect() {
        RangeNotifier rangeNotifier = new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                //TODO [비콘이 감지되면 해당 함수가 호출]
                //TODO [비콘들에 대응하는 Region 객체가 들어들어옴]
                if (beacons.size() > 0) {
                    beaconList.clear();
                    for (Beacon beacon : beacons) {
                        beaconList.add(beacon);
                    }
                }
            }
        };
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null , null, null));
            beaconManager.addRangeNotifier(rangeNotifier);
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            beaconManager.addRangeNotifier(rangeNotifier);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    Handler BeaconHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            String tmp = "";
            int beaconCount = 0;
            beaconMessage = "";
            try {
                //TODO [기존에 저장된 배열 데이터 초기화 실시]
                if(beaconFormatList.size() > 0){
                    beaconFormatList.clear();
                }

                //TODO [for 문 사용해 실시간 스캔된 비콘 개별 정보 확인]
                for(Beacon beacon : beaconList){
                    /** TODO [비콘 스캔 정보 추출 참고]
                     Log.d("//===========//","================================================");
                     Log.d("","\n"+"[비콘 스캔 Name] "+" ["+String.valueOf(beacon.getBluetoothName())+"]");
                     Log.d("","\n"+"[비콘 스캔 MAC] "+" ["+String.valueOf(beacon.getBluetoothAddress())+"]");
                     Log.d("","\n"+"[비콘 스캔 UUID] "+" ["+String.valueOf(beacon.getId1().toString())+"]");
                     Log.d("","\n"+"[비콘 스캔 Major] "+" ["+String.valueOf(beacon.getId2().toString())+"]");
                     Log.d("","\n"+"[비콘 스캔 Minor] "+" ["+String.valueOf(beacon.getId3().toString())+"]");
                     Log.d("","\n"+"[비콘 스캔 MPower] "+" ["+String.valueOf(beacon.getTxPower())+"]");
                     Log.d("","\n"+"[비콘 스캔 RSSI] "+" ["+String.valueOf(beacon.getRssi())+"]");
                     Log.d("","\n"+"[비콘 스캔 ServiceUuid] "+" ["+String.valueOf(beacon.getServiceUuid())+"]");
                     Log.d("","\n"+"[비콘 스캔 beacon] "+" ["+String.valueOf(beacon.toString())+"]");
                     Log.d("//===========//","================================================");
                     */


                    //스캔한 비콘 정보 스트링
                    beaconFormatList.add("\n----------------------------------------------------------------------\n");
                    beaconFormatList.add("UUID "+String.valueOf(beacon.getBluetoothName())+"\n");
                    beaconFormatList.add("MAJOR "+String.valueOf(beacon.getId2())+"\n");
                    beaconFormatList.add("MINOR "+String.valueOf(beacon.getId3())+"\n");
                    beaconFormatList.add("Service "+String.valueOf(beacon.getServiceUuid())+"\n");
                    beaconFormatList.add("MAC "+String.valueOf(beacon.getBluetoothAddress())+"\n");
                    beaconFormatList.add("Service "+String.valueOf(beacon.getId1())+"\n");
                    beaconFormatList.add("----------------------------------------------------------------------\n");
                    beaconCount += 1;

                    tmp = String.valueOf(beacon.getBluetoothName());
                    //TODO 비콘이름을 가지고 판별, 다만 현재 AT커맨드로 비콘 이름을 바꾸는것이 불안정하기 때문에 수정될 수 있음
                    //TODO 이름 포함을 통해 무게 추출(uuid값을 사용)
                    if(tmp.contains("White_Plate")){
                        plate_White = String.valueOf(beacon.getId1()).substring(0,8);
                    }
                    else if (tmp.contains("Red_Plate")){
                        plate_Red = String.valueOf(beacon.getId1()).substring(0,8);
                    }
                    else if (tmp.contains("Blue_Plate")){
                        plate_Blue = String.valueOf(beacon.getId1()).substring(0,8);
                    }
                    else if (tmp.contains("Black_Plate")){
                        plate_Black = String.valueOf(beacon.getId1()).substring(0,8);
                    }


                    //TODO [스캔한 비콘 정보 포맷 실시]
                    /*
                    JSONObject jsonBeacon = new JSONObject();
                    jsonBeacon.put("UUID", String.valueOf(beacon.getBluetoothName())+"\n");
                    jsonBeacon.put("MAJOR", String.valueOf(beacon.getId2().toString())+"\n");
                    jsonBeacon.put("MINOR", String.valueOf(beacon.getId3().toString())+"\n");
                    jsonBeacon.put("ServiceUuid", String.valueOf(beacon.getServiceUuid())+"\n");
                    jsonBeacon.put("MAC", String.valueOf(beacon.getBluetoothAddress())+"\n");
                    jsonBeacon.put("UUID", String.valueOf(beacon.getId1().toString())+"\n");
                    */

                    //TODO [배열에 데이터 저장 실시]
                    //beaconFormatList.add(jsonBeacon.toString());


                }//TODO [for 문 종료]

                //TODO [실시간 스캔된 비콘 정보 확인 실시]
                Log.d("---","---");
                Log.w("//===========//","================================================");
                Log.d("","\n"+"[비콘 스캔 실행 횟수] "+" ["+String.valueOf(beaconScanCount)+"]");
                //Log.d("","\n"+"[비콘 스캔 개수 확인] "+" ["+String.valueOf(beaconFormatList.size())+"]");
                Log.d("","\n"+"[비콘 스캔 개수 확인] "+" ["+beaconCount+"개"+"]");
                Log.d("","\n"+"[비콘 스캔 정보 확인] "+" ["+String.valueOf(beaconFormatList)+"]");
                Log.w("//===========//","================================================");
                Log.d("---","---");

                //TODO [텍스트뷰 처리를 위한 비콘 메세지 입력]

                //비콘 메세지 입력
                if(Integer.parseInt(plate_White) != 0) {
                    beaconMessage += "흰색그릇 : " + Integer.parseInt(plate_White)+ "g";
                }
                else if(Integer.parseInt(plate_Red) != 0){
                    beaconMessage += "\n 빨간색그릇  :" + Integer.parseInt(plate_Red) + "g";
                }
                else if(Integer.parseInt(plate_Black) != 0) {
                    beaconMessage += "\n 검은색그릇  :" + Integer.parseInt(plate_Black)+ "g";
                }
                else if(Integer.parseInt(plate_Blue) != 0) {
                    beaconMessage += "\n 검은색그릇  :" + Integer.parseInt(plate_Blue) + "g";
                }
                // else if가 아니고 if 이면 beaconscanCount가 안오르는 현상이 존재함(확인필요)

                //TODO [중간 필요한 로직 처리 실시]

                //TODO [비콘 스캔 카운트 증가]
                beaconScanCount ++;
            }
            catch (Exception e){
                e.printStackTrace();
            }

            //TODO [자기 자신을 1초마다 호출]

            // 기존 beacontry 프로젝트때 활용한 메소드(신경x)
//            beaconText.setText(beaconMessage);


            BeaconHandler.sendEmptyMessageDelayed(0, 1000);

        }
    };

    //TODO [블루투스 기능 활성 여부 및 GPS 기능 활성 여부 확인]
    public Boolean getBleStateCheck(){
        boolean state_result = false;
        try {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(mBluetoothAdapter == null){ //TODO [블루투스를 지원하는 기기인지 확인]
                Log.d("---","---");
                Log.e("//===========//","================================================");
                Log.d("","\n"+"[BeaconScan > getBleStateCheck() 메소드 : 블루투스 지원 기기 확인]");
                Log.d("","\n"+"[디바이스 : 블루투스를 지원하지 않는 기기]");
                Log.e("//===========//","================================================");
                Log.d("---","---");
                //TODO [Alert 팝업창 알림 실시]
                String alertTitle = "[블루투스 기능 지원 여부 확인]";
                String alertMessage = "사용자 디바이스는 블루투스 기능을 지원하지 않는 단말기입니다.";
                String buttonYes = "확인";
                String buttonNo = "취소";
                new android.app.AlertDialog.Builder(MainActivity.this)
                        .setTitle(alertTitle)
                        .setMessage(alertMessage)
                        .setCancelable(false)
                        .setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        })
                        .setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        })
                        .show();
            }
            else { //TODO [블루투스가 켜져있는지 확인]
                Log.d("---","---");
                Log.w("//===========//","================================================");
                Log.d("","\n"+"[BeaconScan > getBleStateCheck() 메소드 : 블루투스 지원 기기 확인]");
                Log.d("","\n"+"[디바이스 : 블루투스를 지원하는 기기]");
                Log.w("//===========//","================================================");
                Log.d("---","---");
                if(mBluetoothAdapter.isEnabled() == true){
                    Log.d("---","---");
                    Log.w("//===========//","================================================");
                    Log.d("","\n"+"[BeaconScan > getBleStateCheck() 메소드 : 블루투스 기능 활성 확인]");
                    Log.d("","\n"+"[상태 : 블루투스 기능 활성]");
                    Log.w("//===========//","================================================");
                    Log.d("---","---");

                    //TODO [GPS 활성 상태 확인 실시]
                    try {
                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){ //TODO 위치 권한 비활성인 경우
                            Log.d("---","---");
                            Log.e("//===========//","================================================");
                            Log.d("","\n"+"[BeaconScan > getBleStateCheck() 메소드 : GPS 위치 권한 활성 여부 확인]");
                            Log.d("","\n"+"[상태 : 비활성]");
                            Log.e("//===========//","================================================");
                            Log.d("---","---");
                            //TODO [Alert 팝업창 알림 실시]
                            String alertTitle = "[GPS 기능 활성 여부 확인]";
                            String alertMessage = "GPS 기능이 비활성화 상태입니다.\nGPS 기능을 활성화해야 정상 기능 사용이 가능합니다.";
                            String buttonYes = "확인";
                            String buttonNo = "취소";
                            new android.app.AlertDialog.Builder(MainActivity.this)
                                    .setTitle(alertTitle)
                                    .setMessage(alertMessage)
                                    .setCancelable(false)
                                    .setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO Auto-generated method stub
                                            goGpsSettingsIntent(); //TODO [GPS 기능 설정창 이동 실시]
                                        }
                                    })
                                    .setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO Auto-generated method stub
                                        }
                                    })
                                    .show();
                        }
                        else { //TODO 위치 권한 활성인 경우
                            Log.d("---","---");
                            Log.w("//===========//","================================================");
                            Log.d("","\n"+"[BeaconScan > getBleStateCheck() 메소드 : GPS 위치 권한 활성 여부 확인]");
                            Log.d("","\n"+"[상태 : 활성]");
                            Log.w("//===========//","================================================");
                            Log.d("---","---");
                            state_result = true;
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else {
                    Log.d("---","---");
                    Log.e("//===========//","================================================");
                    Log.d("","\n"+"[BeaconScan > getBleStateCheck() 메소드 : 블루투스 기능 활성 확인]");
                    Log.d("","\n"+"[상태 : 블루투스 기능 비활성]");
                    Log.e("//===========//","================================================");
                    Log.d("---","---");

                    //TODO [Alert 팝업창 알림 실시]
                    String alertTitle = "[블루투스 기능 활성 여부 확인]";
                    String alertMessage = "블루투스 기능이 비활성화 상태입니다.\n블루투스 기능을 활성화해야 정상 기능 사용이 가능합니다.";
                    String buttonYes = "확인";
                    String buttonNo = "취소";
                    new android.app.AlertDialog.Builder(MainActivity.this)
                            .setTitle(alertTitle)
                            .setMessage(alertMessage)
                            .setCancelable(false)
                            .setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    goBleSettingsIntent(); //TODO [블루투스 설정창 이동 실시]
                                }
                            })
                            .setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                }
                            })
                            .show();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Log.d("---","---");
        Log.d("//===========//","================================================");
        Log.d("","\n"+"[BeaconScan > getBleStateCheck() 메소드 : 블루투스 및 GPS 활성 상태 리턴 값 확인]");
        Log.d("","\n"+"[리턴 값 : "+String.valueOf(state_result)+"]");
        Log.d("//===========//","================================================");
        Log.d("---","---");
        return state_result;
    }

    //TODO [안드로이드 시스템 블루투스 설정창 이동 메소드]
    public void goBleSettingsIntent(){
        try {
            Log.d("---","---");
            Log.w("//===========//","================================================");
            Log.d("","\n"+"[BeaconScan > goBleSettingsIntent() 메소드 : 블루투스 설정창 인텐트 이동 실시]");
            Log.w("//===========//","================================================");
            Log.d("---","---");
            Intent go_ble = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
            go_ble.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(go_ble);
            overridePendingTransition(0, 0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //TODO [안드로이드 시스템 GPS 설정창 이동 메소드]
    public void goGpsSettingsIntent(){
        try {
            Log.d("---","---");
            Log.w("//===========//","================================================");
            Log.d("","\n"+"[BeaconScan > goGpsSettingsIntent() 메소드 : 위치 권한 설정창 인텐트 이동 실시]");
            Log.w("//===========//","================================================");
            Log.d("---","---");
            Intent go_gps = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            go_gps.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(go_gps);
            overridePendingTransition(0, 0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    // [백버튼 터치시 뒤로 가기]
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 디바이스의 키 이벤트가 발생했는데, 뒤로가기 이벤트일때
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d("---","---");
            Log.d("//===========//","================================================");
            Log.d("","\n"+"[BeaconScan > onKeyDown() 메소드 : 백버튼 터치 및 뒤로 가기 이벤트 실시]");
            Log.d("//===========//","================================================");
            Log.d("---","---");
            //TODO [액티비티 종료 실시]
            try {
                finish();
                overridePendingTransition(0,0);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return true;
    }

    // [액티비티 종료 메소드]
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("---","---");
        Log.d("//===========//","================================================");
        Log.d("","\n"+"[BeaconScan > onDestroy() 메소드 : 액티비티 종료 실시]");
        Log.d("//===========//","================================================");
        Log.d("---","---");
        //TODO [비콘 스캔 종료 실시]
        try {
            BeaconScanStop();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    // [액티비티 실행 준비 메소드]
    @Override
    public void onResume(){
        super.onResume();
        Log.d("---","---");
        Log.d("//===========//","================================================");
        Log.d("","\n"+"[BeaconScan > onResume() 메소드 : 액티비티 실행 준비 실시]");
        Log.d("//===========//","================================================");
        Log.d("---","---");
        try {
            //TODO [외부 브라우저 복귀 시 화면 전환 애니메이션 없애기 위함]
            overridePendingTransition(0,0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    // todo 권한설정 메세지 박스(안드 구버전에서는 정상 작동, 최신버전에서는 권한 거부가 2회 이상시 "다시묻지않음"이 자동으로 설정되 논리 오류)
    void showDialog() {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("경고")
                .setMessage("권한 거부 시 앱을 실행하실 수 없습니다. 앱을 종료하시겠습니까?")
                .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        permissionCheck();
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }
    public String getBeaconMessage(){
        return beaconMessage;
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
            // alertdialog 실행
            showDialog();
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
