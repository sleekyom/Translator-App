package com.quick.translator.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.quick.translator.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
    public void onclick(View view)   // This Method Will Be Called When AnyCard View Is Clicked This  Method Is Set to Called In Xml (   android:onClick="onclick" )
    {

        Class distinationClass=null;  //This Variable Contains Which Activity  is to Call

        if(view.getId()==R.id.crdAddphrases)  // if Add Phrases Button Is Clicked
        {
            distinationClass=AddPhrases.class;  //Update Destination Variable To AddPhrase Class
        }

        if(view.getId()==R.id.crdDisplayPhrases)   // if DisplayPhrases Button Is Clicked
        {
            distinationClass=DisplayPhrases.class;  //Update Destination Variable To DisplayPhrases Class
        }

        if(view.getId()==R.id.crdEditPhrases)   // if EditPhrase Button Is Clicked
        {
            distinationClass=EditPhrase.class;    //Update Destination Variable To EditPhrase Class
        }



        if(view.getId()==R.id.crdTranslate)
        {
            distinationClass= Translate.class;
        }


        if(distinationClass!=null)
        {
            startActivity(new Intent(MainActivity.this,distinationClass));  //start New Activity Based On Which Class Reference Is In Distination Class
        }
    }
}
