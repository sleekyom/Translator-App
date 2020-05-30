package com.quick.translator.Database;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.quick.translator.Model.ModelLanguage;
import com.quick.translator.Model.ModelPhrase;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    private static final String DBNAME="Translator.db";  //DATABASE NAME
    private static final int DBVERSION=4;                // DATABASE VERSION
    private SQLiteDatabase db;                          //DATABASE OBJECT

    private Context ctx;

    public Database (Context context)                   //DATABASE CONSTRUCTOR
    {
        super(context,DBNAME,null,DBVERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {          //ONCREATE FUNCTION USED TO CREATE TABLES
        db.execSQL("Create table  if not exists 'Phrases' (ID INTEGER PRIMARY KEY   AUTOINCREMENT,Date TEXT,Phrase TEXT)");

        db.execSQL("Create table  if not exists 'Languages' (ID INTEGER PRIMARY KEY   AUTOINCREMENT,Language TEXT,Name TEXT,IsSubscribed TEXT)");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)   //ONUPGRADE FUNCTION CALLED WHEN WE CHANGE DBVERSION VALUE THIS FUNCTION IS USED TO DELETE OLD TABLES AND CREATA NEW ONE
    {
        db.execSQL("DROP TABLE IF  EXISTS 'Phrases'");
        db.execSQL("DROP TABLE IF  EXISTS 'Languages'");
        onCreate(db);
    }

    private Activity getActivity() {

        return (Activity) ctx;
    }


    public void deletePhrase(String phrase)
    {

        final ProgressDialog Progressdialog = new ProgressDialog(ctx);
        Progressdialog.setMessage("Deleting..");
        Progressdialog.setCancelable(false);
        Progressdialog.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Phrases").document(phrase).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    Toast.makeText(getActivity(),"Deleted Successfully",Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    getActivity().startActivity(getActivity().getIntent());
                }
                else
                {
                    Toast.makeText(getActivity(),"Failed To Delete",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    public  boolean addRecordsinLanguage(ModelLanguage language)
    {
        try
        {
            db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("Language",language.getLanguage());
            cv.put("Name",language.getName());
            cv.put("IsSubscribed","false");
            long login;
            login=  db.insert("Languages", null, cv);
            return login >=1;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public   ArrayList<ModelLanguage>  getRecordFromLanguages()
    {
        Cursor cursor = null;

        ArrayList<ModelLanguage> savedPhrasesList=new ArrayList<>();
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("select * from Languages;", null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
            {

                ModelLanguage singlePhrase=new ModelLanguage();
                singlePhrase.setIsSubscribed(cursor.getString(cursor.getColumnIndex("IsSubscribed")));
                singlePhrase.setLanguage(cursor.getString(cursor.getColumnIndex("Language")));
                singlePhrase.setName(cursor.getString(cursor.getColumnIndex("Name")));
                singlePhrase.setId(String.valueOf(cursor.getInt(cursor.getColumnIndex("ID"))));
                savedPhrasesList.add(singlePhrase);
            }
            return savedPhrasesList;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return  savedPhrasesList;
        }
        finally
        {
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    public  boolean updateLanguage(ModelLanguage modelLanguage)
    {
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("IsSubscribed",modelLanguage.getIsSubscribed());
            long id;
            id=db.update("Languages",cv,"ID=?",new String[]{modelLanguage.getId()});
            return id>0;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return  false;
        }
        finally
        {
            if (cursor != null) {
                cursor.close();
            }
        }

    }




    //////////////////////////

    public  boolean addRecordPhrases(ModelPhrase Phrase)
    {
        try
        {
            db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("Date",Phrase.getDate());
            cv.put("Phrase",Phrase.getPhrase());
            long login;
            login=  db.insert("Phrases", null, cv);
            return login >=1;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public   ArrayList<ModelPhrase>  getSavedPhrases()
    {
        Cursor cursor = null;

        ArrayList<ModelPhrase> savedPhrasesList=new ArrayList<>();
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("select * from Phrases;", null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
            {

                ModelPhrase singlePhrase=new ModelPhrase();
                singlePhrase.setDate(cursor.getString(cursor.getColumnIndex("Date")));
                singlePhrase.setPhrase(cursor.getString(cursor.getColumnIndex("Phrase")));
                singlePhrase.setId(String.valueOf(cursor.getInt(cursor.getColumnIndex("ID"))));
                savedPhrasesList.add(singlePhrase);
            }
            return savedPhrasesList;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return  savedPhrasesList;
        }
        finally
        {
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    public  boolean update(ModelPhrase modelPhrase)
    {
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("Phrase",modelPhrase.getPhrase());
            long id;
            id=db.update("Phrases",cv,"ID=?",new String[]{modelPhrase.getId()});
            return id>0;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return  false;
        }
        finally
        {
            if (cursor != null) {
                cursor.close();
            }
        }

    }
}

