package com.cr.transportmap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.wear.widget.SwipeDismissFrameLayout;
import net.daum.mf.map.api.MapView;

public class MainActivity extends Activity {

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
        mapView.canScrollHorizontally(1);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
    }
}