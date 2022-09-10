package com.cr.transportmap;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.ViewGroup;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.wear.widget.SwipeDismissFrameLayout;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class MainActivity extends Activity {

    private final int ACCESS_FINE_LOCATION = 1000;
    private final String PACKAGE = "com.cr.transportmap";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      String hashKey = HashKey.getHashKey(getApplicationContext());
        SwipeDismissFrameLayout testLayout =
                (SwipeDismissFrameLayout) findViewById(R.id.map_container);
        testLayout.addCallback(new SwipeDismissFrameLayout.Callback() {
            @Override
            public void onDismissed(SwipeDismissFrameLayout layout) {
                onBackPressed();
            }

        });

        MyView mapView = new MyView(this);

        // 스와이프 왼쪽 끝 10%에서만 가능하도록 변경
        mapView.canScrollHorizontally(1);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        // 트랙킹 모드 On, 단말의 위치에 따라 지도 중심이 이동
        if(permissionCheck()) {
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        }
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633), true);

    }

    private boolean permissionCheck() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean isFirstPermissionCheck = preferences.getBoolean("isFirstPermissionCheck", true);
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //권한이 없는 상태
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //권한 거절 다시 요청
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("현재 위치를 확인하시려면 위치 권한을 허용해주세요");
                builder.setPositiveButton("확인", (dialog, which) -> {
                    ActivityCompat.requestPermissions(this, permissions, ACCESS_FINE_LOCATION);
                });
                builder.setNegativeButton("취소", (dialog, which) -> {
                    //취소시
                });
                builder.show();
            } else {
                if(isFirstPermissionCheck) {
                    preferences.edit().putBoolean("isFirstPermissionCheck", false).apply();
                    ActivityCompat.requestPermissions(this, permissions, ACCESS_FINE_LOCATION);
                } else {
                    // 거부 및 다시 묻지 않음 클릭시 알럿(앱 정보 화면으로 보내기)
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("현재 위치를 확인하시려면 설정에서 위치 권한을 허용해주세요");
                    builder.setPositiveButton("설정으로 이동", (dialog, which) -> {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + PACKAGE));
                    });
                    builder.setNegativeButton("취소", (dialog, which) -> {
                        //취소시 행동
                    });
                    builder.show();
                }
            }
            return false;
        } else {
            // 권한이 있는 상태
            return true;
        }
    }
}