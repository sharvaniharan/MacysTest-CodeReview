package com.macystest.sharvani.macystest.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.macystest.sharvani.macystest.R;
import com.macystest.sharvani.macystest.activities.FilesListActivity;
import com.macystest.sharvani.macystest.activities.MainActivity;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sharvani on 9/13/16.
 * Adapter to hold information on largest files of the SD Card
 */
public class BiggestFilesAdapter extends RecyclerView.Adapter<BiggestFilesAdapter.BiggestVH> {
    Activity activity;
    Context context;
    LayoutInflater inflater;
    List<File> data = Collections.emptyList();

    public BiggestFilesAdapter(Context context, List<File> data) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;

        if (context instanceof MainActivity) {
            activity = (MainActivity) context;
        }
    }


    @Override
    public BiggestFilesAdapter.BiggestVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row_recycler2, parent, false);
        BiggestVH vh = new BiggestVH(view);

        return vh;    }

    @Override
    public void onBindViewHolder(BiggestFilesAdapter.BiggestVH holder, int position) {
        File temp = data.get(position);
        holder.fileName.setText(temp.getName());
        holder.fileSize.setText(""+temp.length()+" bytes");

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class BiggestVH extends RecyclerView.ViewHolder {
        TextView fileName;
        TextView fileSize;
        public BiggestVH(View itemView) {
            super(itemView);
            fileName = (TextView) itemView.findViewById(R.id.filename);
            fileSize = (TextView) itemView.findViewById(R.id.filesize);
        }
    }
}
