package io.dylan.snipebanker.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import io.dylan.snipebanker.models.Match;
import io.dylan.snipebanker.network.VolleySingleton;
import io.dylan.snipebanker.parsers.MatchParser;
import io.dylan.snipebanker.persist.AppRoomDatabase;
import io.dylan.snipebanker.persist.converters.DateConverter;
import io.dylan.snipebanker.persist.daos.MatchDao;

/**
 * Download match list from network, and insert into db
 */
public class DownloadMatchTask extends AsyncTask<Date, Void, Void> {

    private final static String TAG = DownloadMatchTask.class.getName();

    private final static String DATE_PLACEHOLDER = "@yyyy-MM-dd";
    private final static String URL = "http://www.okooo.com/jingcai/shengpingfu/" + DATE_PLACEHOLDER + "/";

    @NonNull
    private final WeakReference<Context> context;

    public DownloadMatchTask(@NonNull Context context) {
        this.context = new WeakReference<>(context);
    }

    @Override
    protected Void doInBackground(Date... dates) {
        Date date;
        if (dates == null || dates.length == 0) {
            date = new Date(); // current day
        } else {
            date = dates[0];
        }
        String strDate = DateConverter.dateToYmdString(date);
        String url = URL.replace(DATE_PLACEHOLDER, strDate);
        download(url);
        return null;
    }

    private void download(@NonNull String url) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleErrorResponse(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return fakeBrowserHeaders();
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String parsed;
                try {
                    parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "GB2312"));
                } catch (UnsupportedEncodingException e) {
                    // Since minSdkVersion = 8, we can't call
                    // new String(response.data, Charset.defaultCharset())
                    // So suppress the warning instead.
                    parsed = new String(response.data);
                }
                return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        VolleySingleton.getInstance(context.get().getApplicationContext()).addToRequestQueue(request);
    }

    @NonNull
    private Map<String, String> fakeBrowserHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Connection", "keep-alive");
        headers.put("Cache-Control", "max-age=0");
        headers.put("Upgrade-Insecure-Requests", "1");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headers.put("Accept-Language", "en-US,en;q=0.9");
        return headers;
    }

    private void handleErrorResponse(VolleyError error) {
        Log.e(TAG, "handleErrorResponse: ", error);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    private void handleResponse(@NonNull String response) {
        Log.d(TAG, "handleResponse: " + response);

        final List<Match> matchList = MatchParser.parse(response);
        Log.d(TAG, "MatchParser.parsed match list: " + matchList);

        // insert into db
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                MatchDao matchDao = AppRoomDatabase.getInstance(context.get().getApplicationContext()).matchDao();
                Log.d(TAG, "run: insert into db");
                matchDao.insert(matchList.toArray(new Match[0]));
            }
        });
    }
}
