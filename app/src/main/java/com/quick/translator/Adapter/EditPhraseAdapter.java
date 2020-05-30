package com.quick.translator.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.quick.translator.Activities.EditPhrase;
import com.quick.translator.Model.ModelPhrase;
import com.quick.translator.R;

import java.util.ArrayList;

public class EditPhraseAdapter extends BaseAdapter {
    ArrayList<ModelPhrase> data;
    Context context;
    public  RadioButton CheckedBtn;
    public ModelPhrase selectedModel=null;

    public EditPhraseAdapter(Context context, ArrayList<ModelPhrase> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder")
        View row = inflater.inflate(R.layout.lyt_edit_phrases, parent, false);


        final ModelPhrase Data = data.get(position);

        try
        {
            final Activity ac= (Activity) context;
            TextView txtPhrase = row.findViewById(R.id.txtPhrase);
            TextView txtDate = row.findViewById(R.id.txtdate);
            final RadioButton radioEdit=row.findViewById(R.id.radioEdit);
            radioEdit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {

                    if(CheckedBtn!=null)
                    {
                        if(!TextUtils.isEmpty(getactivity().editTextPhraseToEdit.getText()) && !selectedModel.getId().equals(Data.getId()))
                        {
                            getactivity().editTextPhraseToEdit.setText(Data.getPhrase());
                            getactivity().modeltoEdit=Data;
                        }
                        CheckedBtn.setChecked(false);
                    }
                    selectedModel=Data;
                    CheckedBtn=radioEdit;
                    CheckedBtn.setChecked(isChecked);


                }
            });
            txtDate.setText(Data.getDate());
            txtPhrase.setText(Data.getPhrase());
            if(selectedModel!=null)
            {
                if(selectedModel.getId().equals(Data.getId()))
                {
                    radioEdit.performClick();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        return row;
    }

    private EditPhrase getactivity()
    {

    return (EditPhrase) context;
    }
}