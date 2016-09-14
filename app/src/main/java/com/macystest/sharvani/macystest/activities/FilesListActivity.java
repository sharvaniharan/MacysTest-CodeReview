package com.macystest.sharvani.macystest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.macystest.sharvani.macystest.R;
import com.macystest.sharvani.macystest.adapter.FolderAdapter;
import com.macystest.sharvani.macystest.async.ScanAsync;

import java.io.File;
import java.util.ArrayList;

public class FilesListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FolderAdapter folderAdapter;
TextView textView;
    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files_list);
        String folder = getIntent().getStringExtra("FILEPATH");
       file=new File(folder);
        recyclerView = (RecyclerView) findViewById(R.id.fileslist);
        textView= (TextView) findViewById(R.id.folder_message_textview);
        new ScanAsync(FilesListActivity.this,folder).execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void setList(ArrayList<File> fileList, int totalFiles) {
        //numOfFiles.setText("" + (totalFiles - fileList.size()));
        if(fileList.isEmpty()){
            textView.setText("NO FILES");
        }else {
            textView.setText(file.getName()+" has "+totalFiles+" files");
            folderAdapter = new FolderAdapter(this, fileList);
            recyclerView.setAdapter(folderAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    public void refreshPage(String name) {
        Intent intent=new Intent(getApplicationContext(),FilesListActivity.class);
        intent.putExtra("FILEPATH",name);
        //finish();
        startActivity(intent);
    }
}
