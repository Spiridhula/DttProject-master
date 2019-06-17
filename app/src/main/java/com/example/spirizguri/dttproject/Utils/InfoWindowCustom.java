package com.example.spirizguri.dttproject.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.spirizguri.dttproject.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class InfoWindowCustom implements GoogleMap.InfoWindowAdapter {



    private Context context;


    public InfoWindowCustom(Context context){
        this.context=context;
    }
    @Override
    public View getInfoWindow(Marker marker) {

        View v = ((Activity) context).getLayoutInflater()
                .inflate(R.layout.infowindow, null);

        TextView title = v.findViewById(R.id.infowindowtitle);

        title.setText(marker.getTitle());
        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return  null;
    }
}
