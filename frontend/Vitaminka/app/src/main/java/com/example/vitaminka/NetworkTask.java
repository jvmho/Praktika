package com.example.vitaminka;

import android.os.AsyncTask;

public abstract class NetworkTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    private Exception exception;

    @Override
    protected Result doInBackground(Params... params) {
        try {
            return doInBackgroundImpl(params);
        } catch (Exception e) {
            exception = e;
            return null;
        }
    }

    protected abstract Result doInBackgroundImpl(Params... params) throws Exception;

    protected Exception getException() {
        return exception;
    }
}