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



    Context context;
    LayoutInflater inflater;



    public InfoWindowCustom(Context context){
        this.context=context;
    }
    @Override
    public View getInfoWindow(Marker marker) {
       return null;
    }

    @Override
    public View getInfoContents(Marker marker) {


        //add the info


        View v;
        v = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.infowindow, null);


        TextView title = (TextView) v.findViewById(R.id.infowindowtitle);

        title.setText(marker.getTitle());



        return v;
    }
}
