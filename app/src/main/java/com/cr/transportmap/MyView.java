package com.cr.transportmap;

import android.content.Context;
import android.view.View;
import net.daum.mf.map.api.MapView;

public class MyView extends MapView {

    public MyView(Context context) {
        super(context);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return true;
    }
}
