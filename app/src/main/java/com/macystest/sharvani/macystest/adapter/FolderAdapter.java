package com.macystest.sharvani.macystest.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.macystest.sharvani.macystest.R;
import com.macystest.sharvani.macystest.activities.FileViewer;
import com.macystest.sharvani.macystest.activities.FilesListActivity;
import com.macystest.sharvani.macystest.activities.MainActivity;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sharvani on 9/12/16.
 */
public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderVH> {
    Activity activity;
    Context context;
    LayoutInflater inflater;
    List<File> data = Collections.emptyList();

    public FolderAdapter(Context context, List<File> data) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
        if (context instanceof FileViewer) {
            activity = (FileViewer) context;
        }else{
            activity=(FilesListActivity)context;
        }
    }

    @Override

    public FolderVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row_recycler, parent, false);
        FolderVH vh = new FolderVH(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(FolderVH holder, int position) {
        File temp = data.get(position);
        holder.textView.setText(temp.getName());
        if(temp.isDirectory()){
            holder.imageView.setImageResource(R.drawable.ic_folder_black_24dp);
            holder.imageView.setPadding(5,5,5,5);
        }else{
            holder.imageView.setImageResource(R.drawable.file);
            holder.imageView.setPadding(5,5,5,5);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class FolderVH extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public FolderVH(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.folder_name);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(data.get(getAdapterPosition()).isDirectory())
                        if(activity instanceof FileViewer) {
                            FileViewer ma= (FileViewer) activity;
                            ma.startResultActivity(data.get(getAdapterPosition()).getPath());
                        }else{
                            FilesListActivity fla= (FilesListActivity) activity;
                            fla.refreshPage(data.get(getAdapterPosition()).getPath());
                        }

                }
            });
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(data.get(getAdapterPosition()).isDirectory())
                        if(activity instanceof FileViewer) {
                            FileViewer ma= (FileViewer) activity;
                            ma.startResultActivity(data.get(getAdapterPosition()).getPath());
                        }else{
                            FilesListActivity fla= (FilesListActivity) activity;

                            fla.refreshPage(data.get(getAdapterPosition()).getPath());
                        }                }
            });
        }
    }
}
