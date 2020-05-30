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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.quick.translator.Database.Database;
import com.quick.translator.Model.ModelPhrase;
import com.quick.translator.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddPhrases extends AppCompatActivity implements View.OnClickListener {



    ImageView back;
    Button btnSave;
    EditText editTextPhrase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phrases);
        init(); //Initialize Views Like Buttons etc
    }

    private void init()
    {

        back=findViewById(R.id.back);
        btnSave=findViewById(R.id.btnSave);
        editTextPhrase=findViewById(R.id.edittextPhrase);



        // Set On ClickListener ( OnClick Method Will Be Called When Back Button Or Btn Save Is Clicked )
        back.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)   // OnClick Method Will Be Called When Back Button Or Btn Save Is Clicked
    {
        if(v==back)
        {
            onBackPressed();  //Call Function OnBackPressed If btnBack is Clicked
        }
        if(v==btnSave)
        {
            Save();   //Call Method Save
        }
    }

    private void Save()
    {

        if(!TextUtils.isEmpty(editTextPhrase.getText()))  //Check If TextBox is Not Empty
        {
            Database obj=new Database(this);  // Create Object Of Database
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()); //Get Current Date

            ModelPhrase model=new ModelPhrase();    //Create Object Of ModelPhrase

            model.setDate(currentDate);     //Save Current Date in ModelPhrase Variabel Date
            model.setPhrase(editTextPhrase.getText().toString());   // Save Word/Phrase


            final ProgressDialog pb=new ProgressDialog(AddPhrases.this);
            pb.setMessage("Saving in Database");
            pb.setCancelable(false);
            pb.show();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Phrases").add(model).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {

                    pb.dismiss();
                    if(task.isSuccessful())  //if inserted
                    {
                        Toast.makeText(AddPhrases.this,"Saved In Database",Toast.LENGTH_SHORT).show();
                        editTextPhrase.setText(null);
                    }
                    else //if failed to insert in database
                    {
                        Toast.makeText(AddPhrases.this,"Failed To Save In Database",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else  // if textbox is empty
        {
            editTextPhrase.setError("Please Enter Any Word / Phrase");  //set error
            editTextPhrase.requestFocus();
        }
    }

    @Override
    public void onBackPressed()
    {
        finish();  //Close This Activity
        super.onBackPressed();
    }
}
