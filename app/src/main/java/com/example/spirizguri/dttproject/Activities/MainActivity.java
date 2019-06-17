package com.example.spirizguri.dttproject.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.spirizguri.dttproject.R;
import com.example.spirizguri.dttproject.Utils.CheckGpsAndInternet;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);



        //The privacy info Dialogue
        ImageView privacyinfo = findViewById(R.id.infobtn);
        privacyinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String link1 = "<a href=\"http://www.google.com\">https://www.rsr.nl/</a>";
                String message = "Om gebruik te maken van deze app,dient u het privacybeleid te accepteren "+link1+"";
                Spanned myMessage = Html.fromHtml(message);


                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setMessage(myMessage);
                builder.setNegativeButton("Bevestig", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }
                );


                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                TextView msgTxt = alertDialog.findViewById(android.R.id.message);
                msgTxt.setMovementMethod(LinkMovementMethod.getInstance());

                }
        });

        Button RSRPECHHULP = findViewById(R.id.rsrbtn);
        RSRPECHHULP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MapsActivity.class);
                view.getContext().startActivity(intent);
            }
        });










    }

    @Override
    public void onResume() {
        super.onResume();

        CheckGpsAndInternetConnectivity();
    }

    //method that uses the CheckGps And Internet class to show the alert dialogues if there is missing internet or gps enabled.

    private void CheckGpsAndInternetConnectivity(){


        if(!CheckGpsAndInternet.checkGPSEnabled(this))
            (CheckGpsAndInternet.AlertDialogueGpsDisabled(this)).show();
        else if (!CheckGpsAndInternet.checkInternetConnectivity(this))
            ( CheckGpsAndInternet.AlertDialogueNoInternet(this)).show();
    }




}
