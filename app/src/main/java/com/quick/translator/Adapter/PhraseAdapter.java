package com.quick.translator.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.quick.translator.Model.ModelPhrase;
import com.quick.translator.R;

import java.util.ArrayList;

public class PhraseAdapter extends BaseAdapter {
    ArrayList<ModelPhrase> data;
    Context context;

    public PhraseAdapter(Context context, ArrayList<ModelPhrase> data) {
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
        View row = inflater.inflate(R.layout.lyt_phrases, parent, false);

        final ModelPhrase Data = data.get(position);

        try
        {
            final Activity ac= (Activity) context;
            TextView txtPhrase = row.findViewById(R.id.txtPhrase);
            TextView txtDate = row.findViewById(R.id.txtdate);

            txtDate.setText(Data.getDate());
            txtPhrase.setText(Data.getPhrase());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        return row;
    }
}