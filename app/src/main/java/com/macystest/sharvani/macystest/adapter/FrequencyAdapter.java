package com.macystest.sharvani.macystest.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.macystest.sharvani.macystest.R;
import com.macystest.sharvani.macystest.activities.MainActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sharvani on 9/13/16.
 */
public class FrequencyAdapter extends RecyclerView.Adapter<FrequencyAdapter.FreqVH>{
    Activity activity;
    Context context;
    LayoutInflater inflater;
    Map<String, Integer> data;
    LinkedList<String> extensions=new LinkedList<>();
    LinkedList<String> frequencies=new LinkedList<>();
    LinkedList<String> extensions5=new LinkedList<>();
    LinkedList<String> frequencies5=new LinkedList<>();

    public FrequencyAdapter(Context context, HashMap<String, Integer> data) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
        if(data!=null) {
            Set<Map.Entry<String, Integer>> set = data.entrySet();
            List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(set);
            Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return (o2.getValue()).compareTo(o1.getValue());
                }
            });

            for (Map.Entry<String, Integer> entry : list) {
                extensions.add(entry.getKey());
                frequencies.add("" + entry.getValue());
            }
        }
        if(extensions.size()>=5)
        extensions5 = new LinkedList<>(extensions.subList(0, 5));
        else
        extensions5=extensions;
        if(frequencies.size()>=5)
            frequencies5 = new LinkedList<>(frequencies.subList(0, 5));
        else
            frequencies5=frequencies;
        String s="";

        for(int i=0;i<frequencies5.size();i++){
            s=s+"\n"+extensions5.get(i)+"\t"+frequencies5.get(i);

        }
        if (context instanceof MainActivity) {

            activity = (MainActivity) context;
            MainActivity ma = (MainActivity) activity;
            ma.setFreqFiles(s);
        }
    }


    @Override
    public FrequencyAdapter.FreqVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row_recycler2, parent, false);
        FreqVH vh = new FreqVH(view);

        return vh;    }

    @Override
    public void onBindViewHolder(FrequencyAdapter.FreqVH holder, int position) {

        holder.fileName.setText(extensions5.get(position));
        holder.fileSize.setText(frequencies5.get(position));

    }


    @Override
    public int getItemCount() {
        return frequencies5.size();
    }

    public class FreqVH extends RecyclerView.ViewHolder {
        TextView fileName;
        TextView fileSize;
        public FreqVH(View itemView) {
            super(itemView);
            fileName = (TextView) itemView.findViewById(R.id.filename);
            fileSize = (TextView) itemView.findViewById(R.id.filesize);
        }
    }
}
