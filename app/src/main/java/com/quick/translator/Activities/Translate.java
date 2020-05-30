package com.quick.translator.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.quick.translator.Adapter.PhraseAdapter;
import com.quick.translator.Database.Database;
import com.quick.translator.Model.ModelLanguage;
import com.quick.translator.Model.ModelPhrase;
import com.quick.translator.R;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.Translation;
import com.ibm.watson.language_translator.v3.model.TranslationResult;

import java.time.temporal.TemporalAmount;
import java.util.ArrayList;

public class Translate extends AppCompatActivity implements View.OnClickListener {



    final String apiKey="10hC-Fe-bFjfyipWBr0MLfkDKAvdNA1Sv8K4Ostqh427";
    final String URL="https://api.eu-gb.language-translator.watson.cloud.ibm.com/instances/52c232a9-6306-4c45-b038-b7770df8c955";

    Spinner spinnerSublanguages,spinnerSavedWords;
    Database obj;
    Button btnTranslate;
    TextView txtResult;

    ArrayList<ModelLanguage> arrayListSubLanguages=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        init(); //Initialize Views Like Buttons etc

    }

    private void init()
    {
        obj=new Database(this); //Object Of Database

        // Get Views
        spinnerSublanguages=findViewById(R.id.spnrLanguage);
        spinnerSavedWords=findViewById(R.id.spnrPhrases);
        txtResult=findViewById(R.id.txtResult);
        btnTranslate=findViewById(R.id.btnTranslate);






        final ArrayList<String> listPhrases=new ArrayList<>();  //Our Array List Has Model Of Phrases But We Need Only String Of Words Phrase



        final ProgressDialog pb=new ProgressDialog(Translate.this);
        pb.setMessage("Loading...");
        pb.setCancelable(false);
        pb.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Phrases")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {

                                listPhrases.add(String.valueOf(document.getData().get("phrase")));
                            }
                            if(listPhrases.size()>=1)  // If Result List Has Size >1
                            {

                                ArrayAdapter<String> adapterWords=new ArrayAdapter<>(Translate.this,R.layout.support_simple_spinner_dropdown_item,listPhrases);
                                spinnerSavedWords.setAdapter(adapterWords);

                                getLanguages(pb);
                            }
                            else
                            {
                                pb.dismiss();
                                showErrorDialog("No Saved Words/Phrases | Go To Add Phrases to Add New Words/Phrases","Empty");
                                return;
                            }

                        }
                        else
                        {
                            pb.dismiss();
                            showErrorDialog("Failed To Get Data","Empty");
                        }
                    }
                });










        /// ON CLICK LISTENERS ///////
        btnTranslate.setOnClickListener(this);


    }

    private void getLanguages(final ProgressDialog pb)
    {


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("languages")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        pb.dismiss();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {

                                ModelLanguage modelLanguage=new ModelLanguage();
                                modelLanguage.setLanguage(String.valueOf(document.getData().get("language")));
                                modelLanguage.setName(String.valueOf(document.getData().get("name")));
                                arrayListSubLanguages.add(modelLanguage);
                            }
                            if(arrayListSubLanguages.size()>=1)  // If Result List Has Size >1
                            {

                                ArrayAdapter<String> adapterLanguages=new ArrayAdapter<>(Translate.this,R.layout.support_simple_spinner_dropdown_item,getStringArrrayofLangauges(arrayListSubLanguages));
                                spinnerSublanguages.setAdapter(adapterLanguages);
                            }
                            else
                            {
                                pb.dismiss();
                                showErrorDialog("No Saved Words/Phrases | Go To Add Phrases to Add New Words/Phrases","Empty");
                                return;
                            }

                        }
                        else
                        {
                            pb.dismiss();
                            showErrorDialog("Failed To Get Data","Empty");
                        }
                    }
                });


    }

    private ArrayList<String> getStringArrrayofLangauges(ArrayList<ModelLanguage> arrayListSubLanguages)
    {

        ArrayList<String> toReturn=new ArrayList<>();
        for(ModelLanguage singlePhrase:arrayListSubLanguages)
        {
            toReturn.add(singlePhrase.getName());
        }
        return  toReturn;
    }


    private void showErrorDialog(String Message,String Title)
    {

        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle(Title);
        dialog.setMessage(Message);
        dialog.setPositiveButton("Close ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }
        });
        dialog.show();
    }

    public  void translate(final String toTranslate, final String model)  //This Method Is Used To Translate
    {

        final ProgressDialog pb=new ProgressDialog(this);
        pb.setMessage("Please Wait Translating");
        pb.setCancelable(false);
        pb.show();
        new AsyncTask<String, String, String>()
        {
            @Override
            protected String doInBackground(String... strings)
            {
                            // Translate In BackGround
                try
                {
                    IamAuthenticator authenticator = new IamAuthenticator(apiKey);
                    LanguageTranslator languageTranslator = new LanguageTranslator("2018-05-01", authenticator);
                    languageTranslator.setServiceUrl(URL);

                    TranslateOptions translateOptions = new TranslateOptions.Builder()
                            .addText(toTranslate)
                            .modelId(model)
                            .build();

                    final TranslationResult result = languageTranslator.translate(translateOptions).execute().getResult();
                    StringBuilder toReturn=new StringBuilder();
                    int i=0;
                    for(Translation a: result.getTranslations())  //Loop On translation Results
                    {
                        toReturn.append("Result  "+(++i)+" : "+a.getTranslation());

                    }
                    return  toReturn.toString();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return e.getMessage();
                }
            }
            @Override
            protected void onPostExecute(String s)  //This Method Will Be Called After Downloading Translation
            {

                pb.dismiss();
                txtResult.setText(s);  //Set Response From The Api (it can be translation Or Error )
                super.onPostExecute(s);
            }
        }.execute();

    }

    @Override
    public void onClick(View v)
    {


        if(v==btnTranslate)
        {

            String totranslate=spinnerSavedWords.getSelectedItem().toString();
            String  Model="en-"+arrayListSubLanguages.get(spinnerSublanguages.getSelectedItemPosition()).getLanguage();
            translate(totranslate,Model);

        }

    }
}
