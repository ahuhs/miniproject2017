package com.a4dotsinc.profilo;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.widget.Toast;

/**
 * Created by ARAVIND on 02-01-2018.
 */

public class JobSchedulerService extends JobService {
    private JobShedulerWorker jobShedulerWorker;
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        jobShedulerWorker = new JobShedulerWorker()
        {
            @Override
            protected void onPostExecute(String s) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                jobFinished(jobParameters, false);
            }
        };
        jobShedulerWorker.execute();
        return true; //since in separate thread
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        jobShedulerWorker.cancel(true);
        return false;
    }
}
