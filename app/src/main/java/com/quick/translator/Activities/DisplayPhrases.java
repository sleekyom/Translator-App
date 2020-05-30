package com.quick.translator.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.quick.translator.Adapter.PhraseAdapter;
import com.quick.translator.Database.Database;
import com.quick.translator.Model.ModelPhrase;
import com.quick.translator.R;

import java.util.ArrayList;

public class DisplayPhrases extends AppCompatActivity implements View.OnClickListener {


    ArrayList<ModelPhrase> arrayList=new ArrayList<>();
    ImageView imgNothing,back;
    ListView listPhrases;
    Database obj;
    PhraseAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_phrases);
        init(); //Initialize Views Like Buttons etc
    }


    private void init()
    {

        obj=new Database(this);  //Object Of Database

         // Get Views
        listPhrases=findViewById(R.id.listPhrases);
        back=findViewById(R.id.back);
        imgNothing=findViewById(R.id.imgNothing);


        back.setOnClickListener(this);


        final ProgressDialog pb=new ProgressDialog(DisplayPhrases.this);
        pb.setMessage("Loading...");
        pb.setCancelable(false);
        pb.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Phrases")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        pb.dismiss();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                ModelPhrase obj=new ModelPhrase();
                                obj.setDate(String.valueOf(document.getData().get("date")));
                                obj.setPhrase(String.valueOf(document.getData().get("phrase")));
                                obj.setId(document.getId());
                                arrayList.add(obj);
                            }
                            if(arrayList.size()>=1)  // If Result List Has Size >1
                            {

                                imgNothing.setVisibility(View.GONE);  //hide Emtpy Image Error
                                adapter=new PhraseAdapter(DisplayPhrases.this,arrayList);  //Creatae List Adapter
                                listPhrases.setAdapter(adapter); // Set Adapter On List View
                            }
                            else
                            {
                                imgNothing.setVisibility(View.VISIBLE);  //if List is Empty Show Empty Image Error
                            }

                        }
                        else
                            {
                                Toast.makeText(DisplayPhrases.this,"Failed To Get Data",Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

    @Override
    public void onClick(View v)
    {

        if(v==back)  //if btn Back is pressed
        {
            onBackPressed();
        }

    }



    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
