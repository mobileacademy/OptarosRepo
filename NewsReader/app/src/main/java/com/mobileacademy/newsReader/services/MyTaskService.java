package com.mobileacademy.NewsReader.services;

/*
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.TaskParams;

public class MyTaskService extends GcmTaskService {

    private static final String TAG = "MyTaskService";
    private static final String MY_TAG = "MyTag";

    public static final String ACTION_DONE = "GcmTaskService#ACTION_DONE";
    public static final String EXTRA_TAG = "extra_tag";
    public static final String EXTRA_RESULT = "extra_result";

    private static GcmNetworkManager mGcmNetworkManager;


    @Override
    public void onInitializeTasks() {
        Log.d(TAG, "onInitializeTasks");
        // When your package is removed or updated, all of its network tasks are cleared by
        // the GcmNetworkManager. You can override this method to reschedule them in the case of
        // an updated package. This is not called when your application is first installed.
        //
        // This is called on your application's main thread.

        // TODO(developer): In a real app, this should be implemented to re-schedule important tasks.
    }

    @Override
    public int onRunTask(TaskParams taskParams) {
        Log.d(TAG, "onRunTask: " + taskParams.getTag());

        String tag = taskParams.getTag();

        Log.d(TAG, "do something here!");

        // Default result is success.
        int result = GcmNetworkManager.RESULT_SUCCESS;

        // Create Intent to broadcast the task information.
        Intent intent = new Intent();
        intent.setAction(ACTION_DONE);
        intent.putExtra(EXTRA_TAG, tag);
        intent.putExtra(EXTRA_RESULT, result);

        // Send local broadcast, running Activities will be notified about the task.
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.sendBroadcast(intent);

        // Return one of RESULT_SUCCESS, RESULT_FAILURE, or RESULT_RESCHEDULE
        return result;
    }

    public static void startChargingTask(Context context) {
        Log.d(TAG, "startChargingTask");

        mGcmNetworkManager = GcmNetworkManager.getInstance(context);
        OneoffTask task = new OneoffTask.Builder()
                .setService(MyTaskService.class)
                .setTag(MY_TAG)
                .setExecutionWindow(0L, 3600L)
                .setRequiresCharging(false)
                .build();
        mGcmNetworkManager.schedule(task);
    }

}

