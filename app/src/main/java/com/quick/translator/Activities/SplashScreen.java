package com.quick.translator.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import com.quick.translator.Database.Database;
import com.quick.translator.Model.ModelLanguage;
import com.quick.translator.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguage;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguages;

import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends AppCompatActivity
{

    final String apiKey="10hC-Fe-bFjfyipWBr0MLfkDKAvdNA1Sv8K4Ostqh427";
    final String URL="https://api.eu-gb.language-translator.watson.cloud.ibm.com/instances/52c232a9-6306-4c45-b038-b7770df8c955";
    Database obj;
    ArrayList<ModelLanguage> Languagess=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        obj=new Database(this);


        if(!checkConnection()) //check internet connection
        {
            showErrorDialog(); //Show No Internet Dialog
            return;
        }


        // Other Wise Show Splash Image For 3 Seconds And Go to MainActiivyt

        new CountDownTimer(3000, 1000)   //Show Splash For 3 Seconds
        {
            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish()
            {
                finish();
                startActivity(new Intent(SplashScreen.this, Login.class));
            }
        }.start();



    }

    private void showErrorDialog()  //Dialog To Show When There is No internet Connection
    {

        AlertDialog.Builder dialog=new AlertDialog.Builder(SplashScreen.this);
        dialog.setTitle("No Internet");
        dialog.setMessage("You Have No Internet Connection ");
        dialog.setPositiveButton("Close ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }
        });
        dialog.show();
    }


    public  boolean checkConnection()
    {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        return  connected;
    }

}
