package com.a4dotsinc.profilo;

import android.os.AsyncTask;

/**
 * Created by ARAVIND on 02-01-2018.
 */

public class JobShedulerWorker extends AsyncTask<Void, Void, String> {
    @Override
    protected String doInBackground(Void... voids) {
        return "Background Long running process Finished....";
    }
}
