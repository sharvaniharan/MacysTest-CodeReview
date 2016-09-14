package com.macystest.sharvani.macystest.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.macystest.sharvani.macystest.R;
import com.macystest.sharvani.macystest.adapter.FolderAdapter;

import java.io.File;
import java.util.ArrayList;

public class FileViewer extends AppCompatActivity {
    ArrayList<File> fileListForFileViewer = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_viewer);
        recyclerView = (RecyclerView) findViewById(R.id.folderList);

        //List of all Files
        ArrayList<String> filepaths = getIntent().getStringArrayListExtra("LIST");
        for (String path : filepaths) {
            File file = new File(path);
            fileListForFileViewer.add(file);
        }

        //Segregating Fodlers and Files
        ArrayList<File> fileListFolder = new ArrayList<>();
        ArrayList<File> fileListFiles = new ArrayList<>();

        for (File file : fileListForFileViewer) {
            if (file.isDirectory()) {
                fileListFolder.add(file);
            } else {
                fileListFiles.add(file);
            }
        }
        for (File file : fileListFiles)
            fileListFolder.add(file);
        FolderAdapter folderAdapter = new FolderAdapter(this, fileListFolder);

        //Setting RecyclerView
        recyclerView.setAdapter(folderAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void startResultActivity(String filepath) {
        Intent intent = new Intent(this, FilesListActivity.class);
        intent.putExtra("FILEPATH", filepath);
        startActivity(intent);
    }
}
