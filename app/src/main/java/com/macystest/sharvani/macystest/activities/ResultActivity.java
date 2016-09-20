package com.macystest.sharvani.macystest.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.macystest.sharvani.macystest.R;

/**
 * Created by Sharvani on 9/13/16.
 * Result Activity - Created for a landing place for notifications
 */
public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //Cancels Notifications after load
        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();

    }
}
