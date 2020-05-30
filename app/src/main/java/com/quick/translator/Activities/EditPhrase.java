package com.quick.translator.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.quick.translator.Adapter.EditPhraseAdapter;
import com.quick.translator.Adapter.PhraseAdapter;
import com.quick.translator.Database.Database;
import com.quick.translator.Model.ModelPhrase;
import com.quick.translator.R;

import java.util.ArrayList;

public class EditPhrase  extends AppCompatActivity implements View.OnClickListener {


    ArrayList<ModelPhrase> arrayList=new ArrayList<>();
    ImageView imgNothing,back;
    ListView listPhrases;
    EditPhraseAdapter adapter;
    Button btnEdit,btnSave;
    public  EditText editTextPhraseToEdit;
    public  ModelPhrase modeltoEdit=null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phrase);
        init(); //Initialize Views Like Buttons etc
    }

    private void init()
    {



        // Get Views
        listPhrases=findViewById(R.id.listPhrases);
        back=findViewById(R.id.back);
        imgNothing=findViewById(R.id.imgNothing);
        btnEdit=findViewById(R.id.btnEdit);
        btnSave=findViewById(R.id.btnSave);
        editTextPhraseToEdit=findViewById(R.id.edittextEdit);


        // ON CLICK LISTENERS //
        back.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        updateList();

    }

    private void updateList()
    {


        final ProgressDialog pb=new ProgressDialog(EditPhrase.this);
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
                                obj.setId(String.valueOf(document.getId()));
                                arrayList.add(obj);
                            }
                            if(arrayList.size()>=1)  // If Result List Has Size >1
                            {

                                imgNothing.setVisibility(View.GONE);  //hide Emtpy Image Error
                                adapter=new EditPhraseAdapter(EditPhrase.this,arrayList);  //Creatae List Adapter
                                listPhrases.setAdapter(adapter); // Set Adapter On List View
                            }
                            else
                            {
                                imgNothing.setVisibility(View.VISIBLE);  //if List is Empty Show Empty Image Error
                            }

                        }
                        else
                        {
                            Toast.makeText(EditPhrase.this,"Failed To Get Data",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v)
    {

        if(v==back)
        {
            onBackPressed();
        }
        if(v==btnEdit) //If Button Edit Is Pressed
        {

            if(adapter!=null && adapter.selectedModel!=null)  // Check If Adapter Is Not == null and user has clicked on any radio button
            {
                modeltoEdit=adapter.selectedModel;   //Current Edit Model = Edit Model On Selected Radio Button
                editTextPhraseToEdit.setText(adapter.selectedModel.getPhrase());  //Set Text On TextBox So That User CanMake Changes
                editTextPhraseToEdit.setEnabled(true);  //Enable EditText So That User Can Type Etc
            }

        }
        if(v==btnSave)  //If Button Save Is Clicked
        {

            if(modeltoEdit!=null)  // if Current EditModel Is Not Null Means User Has Clicked On Edit Button
            {

                if(!TextUtils.isEmpty(editTextPhraseToEdit.getText()))  //If ToEdit Text is Not Empty
                {


                    modeltoEdit.setPhrase(editTextPhraseToEdit.getText().toString());  //Update Text with New Entered Text  Which User updated


                    final ProgressDialog pb=new ProgressDialog(EditPhrase.this);
                    pb.setMessage("updating...");
                    pb.setCancelable(false);
                    pb.show();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Phrases").document(modeltoEdit.getId()).update("phrase",modeltoEdit.getPhrase()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            pb.dismiss();
                            if(task.isSuccessful()) //If Database Method Update Return Tru3
                            {
                                Toast.makeText(EditPhrase.this,"Edited Successfully",Toast.LENGTH_SHORT).show(); //Show Success Message
                                arrayList.clear();  //Clear Current List Of  Words
                                updateList();
                                adapter.notifyDataSetChanged();  //Update List

                            }
                            else   //If Database Method Update Return false
                            {
                                Toast.makeText(EditPhrase.this,"Failed To Edit",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                    editTextPhraseToEdit.setEnabled(false); //Disable EditText
                    editTextPhraseToEdit.setText(null); // Clear Text On EditText
                    modeltoEdit=null;  // Clear Current  EditModel

                }
                else  //If ToEdit Text is  Empty
                {
                    editTextPhraseToEdit.setError("Please Fill This");
                    editTextPhraseToEdit.requestFocus();
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
