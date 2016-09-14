package com.macystest.sharvani.macystest.async;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.macystest.sharvani.macystest.R;
import com.macystest.sharvani.macystest.activities.FilesListActivity;
import com.macystest.sharvani.macystest.activities.MainActivity;
import com.macystest.sharvani.macystest.activities.ResultActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sharvani on 9/12/16.
 */
public class ScanAsync extends AsyncTask<String, Integer, Void> {
    ArrayList<File> fileList = new ArrayList<File>();
    ArrayList<File> totalFileList = new ArrayList<File>();
    ArrayList<String> fileNamesList = new ArrayList<String>();
    int totalFiles = 0;
    private Activity activity;
    String folderName;
    int progress = 1;
    HashMap<String, Integer> sortedFileExtensions;
    NotificationCompat.Builder notification;
    PendingIntent pIntent;
    NotificationManager manager;
    Intent resultIntent;
    TaskStackBuilder stackBuilder;

    public ScanAsync(Activity activity, String folderName) {
        this.activity = activity;
        this.folderName = folderName;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (activity instanceof MainActivity) {
            MainActivity ma = (MainActivity) activity;
            ma.setProgressBar(0);
        }
    }

    //Work done, heavy lifting on the worker thread
    @Override
    protected Void doInBackground(String... params) {
        startNotification();
        fileList.clear();
        File root = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath());
        if (folderName != null) {
            getFile(new File(folderName));
        } else {
            getFile(root);
            getBigFiles(root);
        }
        return null;
    }

    //Building nptification when the scan starts
    protected void startNotification() {
        //Creating Notification Builder
        notification = new NotificationCompat.Builder(activity);
        //Title for Notification
        notification.setContentTitle("Macys SD Card Scan Notification ");
        //Message in the Notification
        notification.setContentText("New Scan on the app.");
        //Alert shown when Notification is received
        notification.setTicker("New Message Alert!");
        //Icon to be set on Notification
        notification.setSmallIcon(R.drawable.macys);
        //Creating new Stack Builder
        stackBuilder = TaskStackBuilder.create(activity);
       stackBuilder.addParentStack(ResultActivity.class);
        //Intent which is opened when notification is clicked
        resultIntent = new Intent(activity, ResultActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pIntent);
        manager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification.build());

    }

    //Publishing progress to UI
    @Override
    protected void onProgressUpdate(Integer... values) {

        if (activity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) activity;
            mainActivity.setProgressBar(values[0]);
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //manager.cancelAll();   ===> This has been commented because the notification was vanishing too soon
        for (File file : totalFileList) {
            if (file.isFile()) {
                String extension = "";
                String fileName = file.getName();
                int i = fileName.lastIndexOf('.');
                if (i > 0) {
                    extension = fileName.substring(i + 1);
                }
                if (!extension.isEmpty())
                    fileNamesList.add(extension);
            }
        }

        sortExtensionsByFrequency(fileNamesList);
        if (activity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) activity;
            mainActivity.setProgressBar(100);
            mainActivity.setList(sortedFileExtensions, totalFileList, fileList);
        } else {
            FilesListActivity filesListActivity = (FilesListActivity) activity;
            filesListActivity.setList(fileList, totalFiles);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (activity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) activity;
            mainActivity.setList(sortedFileExtensions, totalFileList, fileList);
        } else {
            FilesListActivity filesListActivity = (FilesListActivity) activity;
            filesListActivity.setList(fileList, totalFiles);
        }
    }


    public ArrayList<File> getBigFiles(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    getBigFiles(listFile[i]);

                } else {
                    totalFileList.add(listFile[i]);
                }

            }
        }
        Collections.sort(totalFileList, new Comparator<File>() {
            public int compare(File f1, File f2) {
                return Long.valueOf(f2.length()).compareTo(Long.valueOf(f1.length()));
            }
        });
        return totalFileList;
    }

    private Map<String, Integer> sortExtensionsByFrequency(List<String> filenames) {
        String[] filenamesArr = filenames.toArray(new String[filenames.size()]);

        Arrays.sort(filenamesArr, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                // the +1 is to avoid including the '.' in the extension and to avoid exceptions
                // We first need to make sure that either both files or neither file
                // has an extension (otherwise we'll end up comparing the extension of one
                // to the start of the other, or else throwing an exception)
                final int s1Dot = s1.lastIndexOf('.');
                final int s2Dot = s2.lastIndexOf('.');
                if ((s1Dot == -1) == (s2Dot == -1)) { // both or neither
                    s1 = s1.substring(s1Dot + 1);
                    s2 = s2.substring(s2Dot + 1);
                    return s1.compareTo(s2);
                } else if (s1Dot == -1) { // only s2 has an extension, so s1 goes first
                    return -1;
                } else { // only s1 has an extension, so s1 goes second
                    return 1;
                }
            }
        });

        Map<String, Integer> extensionMap = new HashMap<>();
        for (String extension : filenamesArr) {
            if (extensionMap.containsKey(extension)) {
                Integer i = extensionMap.get(extension);
                i++;
                extensionMap.put(extension, i);
            } else {
                extensionMap.put(extension, 1);

            }
        }

        sortedFileExtensions = (HashMap<String, Integer>) extensionMap;
        return extensionMap;
    }

    public void getFile(File dir) {
        File listFile[] = dir.listFiles();

        if (listFile != null) {

            totalFiles = listFile.length;
            if (listFile.length > 0) {
                for (int i = 0; i < listFile.length; i++) {
                    fileList.add(listFile[i]);
                    progress++;
                    publishProgress(progress++);
                    SystemClock.sleep(200);
                }
            }


        }

    }

}
