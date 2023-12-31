package com.example.homework.thread;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {
    public static final String TAG = "MyWorker";

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "Work is in progress");
        try {
            for (int i = 0; i < 3; i++) {
                Log.d(TAG, "Doing anything");

                String stringValue = getInputData().getString("KeyA");
                int intValue = getInputData().getInt("KeyB", 0);

                Log.d(TAG, "String :" + stringValue);
                Log.d(TAG, "Int: " + intValue);

                Thread.sleep(3 * 1000);
            }
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage());
            return Result.failure();
        }
        return Result.success();
    }
}
