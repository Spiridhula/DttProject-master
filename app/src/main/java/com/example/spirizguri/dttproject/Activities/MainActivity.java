package com.example.spirizguri.dttproject.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spirizguri.dttproject.R;
import com.example.spirizguri.dttproject.Utils.CheckGpsAndInternet;
import com.example.spirizguri.dttproject.Utils.RetryClickListener;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
       if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        //The privacy info Dialogue

      ImageButton info = (ImageButton) findViewById(R.id.imageButton);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String link1 = "<a href=\"http://www.google.com\">https://www.rsr.nl/</a>";
                String message = "Some links: "+link1+"";
                Spanned myMessage = Html.fromHtml(message);


                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setMessage(myMessage);
                builder.setNegativeButton("Beverdig", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }
                );


                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                TextView msgTxt = (TextView) alertDialog.findViewById(android.R.id.message);
                msgTxt.setMovementMethod(LinkMovementMethod.getInstance());

                }
        });

        Button button1 = (Button) findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MapsActivity.class);
                view.getContext().startActivity(intent);
            }
        });





       /* ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null & datac != null)
                && (wifi.isConnected() | datac.isConnected())) {
            //connection is avlilable
        }else{
            //no connection
            Toast toast = Toast.makeText(this, "No Internet Connection",
                    Toast.LENGTH_LONG);
            toast.show();
        }*/




    }

    @Override
    public void onResume() {
        super.onResume();

        CheckGpsAndInternetConnectivity();
    }

    private void CheckGpsAndInternetConnectivity(){


        if(!CheckGpsAndInternet.checkGPSEnabled(this))
            (CheckGpsAndInternet.buildAlertMessageGpsDisabled(this)).show();
        else if (!CheckGpsAndInternet.checkInternetConnectivity(this))
            (CheckGpsAndInternet.buildAlertMessageNoInternet(this,onRetry)).show();
    }

    private final RetryClickListener onRetry = new RetryClickListener() {
        @Override
        public void onRetryClick() {
            CheckGpsAndInternetConnectivity();
        }
    };
}
