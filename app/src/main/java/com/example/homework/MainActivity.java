package com.example.homework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.homework.databinding.ActivityMainBinding;
import com.example.homework.thread.MyWorker;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private ActivityMainBinding binding;

    private UUID id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Work is in progress");
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Log.d(TAG, "Work is finished");
            }
        });

        binding.btnJustDoIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Data myData = new Data.Builder()
                        .putString("KeyA", "value1")
                        .putInt("keyB", 2)
                        .build();

                OneTimeWorkRequest oneTimeWorkRequest =
                        new OneTimeWorkRequest.Builder(MyWorker.class)
                                .setInputData(myData)
                                .build();

                id = oneTimeWorkRequest.getId();

                WorkManager.getInstance(getApplicationContext()).enqueue(oneTimeWorkRequest);
            }
        });

        binding.btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id != null) {
                    WorkManager.getInstance(getApplicationContext())
                            .getWorkInfoByIdLiveData(id)
                            .observe(MainActivity.this, new Observer<WorkInfo>() {
                                @Override
                                public void onChanged(WorkInfo workInfo) {
                                    if (workInfo != null) {
                                        Log.d(TAG, "Work state: " + workInfo.getState());
                                        String msg = workInfo.getOutputData().getString("KeyC");
                                        Log.d(TAG, "Result message: " + msg);
                                    }
                                }
                            });
                }
            }
        });
    }
}