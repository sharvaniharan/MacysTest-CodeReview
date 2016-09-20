package com.macystest.sharvani.macystest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.macystest.sharvani.macystest.R;
import com.macystest.sharvani.macystest.adapter.BiggestFilesAdapter;
import com.macystest.sharvani.macystest.adapter.FrequencyAdapter;
import com.macystest.sharvani.macystest.async.ScanAsync;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Created by Sharvani on 9/13/16.
 * MainActivity - Created for the central place for behavioral code.
 * Maintains instances of all adapters
 * Maintains UI Handles
 */

public class MainActivity extends AppCompatActivity {
    RecyclerView biggestFiles, frequency;
    TextView numOfFiles;
    TextView scanStatus;
    TextView filesScanned;
    Button scanBtn, bonusBtn;
    ProgressBar progressBar;
    List<File> biggestFilesList;
    ScanAsync task;
    boolean scanCancelled;
    LinearLayout biggestFilesLayout, frequencyLayout;
    ArrayList<File> fileListForFileViewer;
    ArrayList<File> fileListTotal;
    private ShareActionProvider mShareActionProvider;
    int totalNumOfFiles = 0;
    long averageSize = 0;
    String frequentExtensions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Finding Views to Set UI and their listeners
        setUI();
        setListeners();

        //Checking for SD Card
        Boolean isSDCardPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (isSDCardPresent) {
            // yes SD-card is present
            scanSDCard();
        } else {
            // no SD-card is not present
            scanStatus.setText(getResources().getString(R.string.no_sdcard_message));

        }

    }

    private void setUI() {
        biggestFilesLayout = (LinearLayout) findViewById(R.id.biggest);
        frequencyLayout = (LinearLayout) findViewById(R.id.frequent);
        frequency = (RecyclerView) findViewById(R.id.freq_files);
        biggestFiles = (RecyclerView) findViewById(R.id.biggest_files);
        numOfFiles = (TextView) findViewById(R.id.num_of_files);
        progressBar = (ProgressBar) findViewById(R.id.prog_bar);
        scanStatus = (TextView) findViewById(R.id.scanning_status);
        scanBtn = (Button) findViewById(R.id.scanning_btn);
        filesScanned = (TextView) findViewById(R.id.files_scanned);
        bonusBtn = (Button) findViewById(R.id.bonusBtn);
    }

    private void setListeners() {
        scanBtn.setBackgroundResource(R.drawable.ic_restore_page_black_24dp);
        scanBtn.setTag(R.drawable.ic_restore_page_black_24dp);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (Integer) v.getTag();
                //Handling toggle of Scan and stop with single button
                if (tag == R.drawable.ic_restore_page_black_24dp) {
                    hideStatusBoxes();
                    biggestFiles.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    scanStatus.setText(getString(R.string.scanning));
                    scanBtn.setBackgroundResource(R.drawable.stop);
                    scanBtn.setTag(R.drawable.stop);
                    getNewTask();
                    task.execute();
                } else if (tag == R.drawable.stop) {
                    scanCancelled = true;
                    scanStatus.setText(R.string.scan_stopped);
                    scanBtn.setBackgroundResource(R.drawable.ic_restore_page_black_24dp);
                    scanBtn.setTag(R.drawable.ic_restore_page_black_24dp);
                    task.cancel(true);
                }
            }
        });

        //This feature was pro-actively coded, for the viewer to be able to view the file System GUI representation
        //Folders are clickable, files are not.
        bonusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> filepaths = new ArrayList<String>();
                Intent intent = new Intent(MainActivity.this, FileViewer.class);
                for (File file : fileListForFileViewer) {
                    filepaths.add(file.getAbsolutePath());
                }
                intent.putStringArrayListExtra("LIST", filepaths);
                startActivity(intent);
            }
        });

    }

    //AsyncTask to scan sd card and update ui simultaneously
    private void getNewTask() {
        task = new ScanAsync(MainActivity.this, null);
    }

    private void scanSDCard() {
        new ScanAsync(MainActivity.this, null).execute();
        scanStatus.setText(getString(R.string.scanning));
        hideStatusBoxes();
    }

    //Result postings back from AsyncTask
    public void setList(HashMap<String, Integer> sortedFileExtensions, ArrayList<File> totalFileList, ArrayList<File> fileList) {
        fileListForFileViewer = fileList;
        fileListTotal = totalFileList;
        totalNumOfFiles = totalFileList.size();
        scanBtn.setBackgroundResource(R.drawable.ic_restore_page_black_24dp);
        scanBtn.setTag(R.drawable.ic_restore_page_black_24dp);
        if (!scanCancelled) {
            scanStatus.setText(getString(R.string.scan_complete));
        } else {
            scanStatus.setText(getString(R.string.scan_partial));

        }
        scanCancelled = false;
        unhideStatusBoxes();
        numOfFiles.setText("" + totalNumOfFiles);

        if (!totalFileList.isEmpty() && totalFileList.size() >= 10) {
            biggestFilesList = new ArrayList<>(totalFileList.subList(0, 10));
        } else {
            biggestFilesList = totalFileList;
        }
        biggestFiles.setAdapter(new BiggestFilesAdapter(this, biggestFilesList));
        biggestFiles.setLayoutManager(new LinearLayoutManager(this));

        frequency.setAdapter(new FrequencyAdapter(this, sortedFileExtensions));
        frequency.setLayoutManager(new LinearLayoutManager(this));

    }

    //UI handling for clarity
    private void unhideStatusBoxes() {
        biggestFiles.setVisibility(View.VISIBLE);
        filesScanned.setVisibility(View.VISIBLE);
        scanBtn.setVisibility(View.VISIBLE);
        numOfFiles.setVisibility(View.VISIBLE);
        biggestFilesLayout.setVisibility(View.VISIBLE);
        frequencyLayout.setVisibility(View.VISIBLE);
        bonusBtn.setVisibility(View.VISIBLE);
        frequency.setVisibility(View.VISIBLE);
    }

    private void hideStatusBoxes() {
        frequency.setVisibility(View.GONE);
        filesScanned.setVisibility(View.GONE);
        numOfFiles.setVisibility(View.GONE);
        biggestFilesLayout.setVisibility(View.GONE);
        frequencyLayout.setVisibility(View.GONE);
        bonusBtn.setVisibility(View.GONE);

    }

    //Function to update progress on the Progress bar
    public void setProgressBar(int i) {
        if (i == 100) {
            progressBar.setProgress(i);
            progressBar.setVisibility(View.GONE);
        } else {

            progressBar.setProgress(i);
        }

    }

    //Scan stops only on back button and not at home button press.
    @Override
    protected void onStop() {
        super.onStop();
        if (task != null && !task.isCancelled()) {
            task.cancel(true);
        }
    }

    //ShareActionProvider for share feature
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);
        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        // Return true to display menu
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_share) {
            setShareIntent(createShareIntent());
        }
        return super.onOptionsItemSelected(item);
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    //Content creation and intent creation for share
    private Intent createShareIntent() {
        String biggestFiles = "";
        long total = 0;

        int i = 0;
        for (File file : fileListTotal) {
            total += file.length();
        }
        averageSize = total / fileListTotal.size();
        for (File file : biggestFilesList) {
            i++;
            if (i <= 10)
                biggestFiles = biggestFiles + "\n" + file.getName() + "\t" + file.length() + " bytes";
        }
        StringBuilder message = new StringBuilder("Total Number of Files : \t" + totalNumOfFiles + "\n" + "Average Size : \t" + averageSize + "\n" + "Biggest Files" + "\n" + biggestFiles + "\n\n" + "Frequent Extensions" + "\n" + frequentExtensions);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, message.toString());
        shareIntent.setType("text/plain");
        return shareIntent;
    }

    public void setFreqFiles(String s) {
        frequentExtensions = s;
    }
}
