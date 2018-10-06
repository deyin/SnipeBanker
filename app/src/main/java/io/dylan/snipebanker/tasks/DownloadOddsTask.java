package io.dylan.snipebanker.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.dylan.snipebanker.models.Match;
import io.dylan.snipebanker.models.Odds;
import io.dylan.snipebanker.network.VolleySingleton;
import io.dylan.snipebanker.parsers.OddsChangeParser;
import io.dylan.snipebanker.parsers.OddsParser;
import io.dylan.snipebanker.persist.AppRoomDatabase;
import io.dylan.snipebanker.persist.daos.OddsDao;

/**
 * Download match list from network, and insert into db
 */
public class DownloadOddsTask extends AsyncTask<String, Void, List<Odds>> {

    private final static String TAG = DownloadOddsTask.class.getName();

    private final static String MATCH_ID_PLACEHOLDER = "@matchId";
    private final static String TYPE_PLACEHOLDER = "BaijiaBooks"; // default;
    private final static String PROVIDER_ID_PLACEHOLDER = "BaijiaBooks"; // default;

    private final static String MATCH_ODDS_LIST_URL = "http://www.okooo.com/soccer/match/" + MATCH_ID_PLACEHOLDER + "/odds/ajax/?page=0&trnum=0&companytype=" + TYPE_PLACEHOLDER + "&type=0";
    private final static String MATCH_ODDS_CHANGE_LIST_URL = "http://www.okooo.com/soccer/match/" + MATCH_ID_PLACEHOLDER + "/odds/change/" + PROVIDER_ID_PLACEHOLDER + "/";

    @NonNull
    private final WeakReference<Context> context;

    @NonNull
    private final Match match;

    public DownloadOddsTask(@NonNull Context context, @NonNull Match match) {
        this.context = new WeakReference<>(context);
        this.match = match;
    }

    @Override
    protected List<Odds> doInBackground(@NonNull String... params) {
        String url = MATCH_ODDS_LIST_URL.replace(MATCH_ID_PLACEHOLDER, match.getId());
        if (params.length >= 1) {
            url = url.replace(TYPE_PLACEHOLDER, params[0]);
        }
        download(url);
        return null;
    }

    @Override
    protected void onPostExecute(List<Odds> oddsList) {
        super.onPostExecute(oddsList);
    }

    private void download(@NonNull String url) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleOddsResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleErrorResponse(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "text/html, */*");
                headers.put("Referer", "http://www.okooo.com/soccer/match/1009999/odds/");
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
                return headers;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String parsed;
                try {
                    parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
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

    private void handleErrorResponse(VolleyError error) {
        Log.e(TAG, "handleErrorResponse: ", error);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    private void handleOddsResponse(@NonNull String response) {
        Log.d(TAG, "handleResponse: " + response);

        final List<Odds> tempOddsList = OddsParser.parse(response, match.getId());
        Log.d(TAG, "OddsParser.parsed odds list: " + tempOddsList);

        List<Callable<Void>> tasks = new ArrayList<>();

        for (final Odds odds : tempOddsList) {
            tasks.add(new Callable<Void>() {
                @Override
                public Void call() {
                    downloadOddsChangeList(odds);
                    return null;
                }
            });
        }

        ExecutorService threadPool = Executors.newCachedThreadPool();
        try {
            threadPool.invokeAll(tasks);
        } catch (Exception e) {
            Log.e(TAG, "handleResponse: ", e);
        } finally {
            threadPool.shutdown();
        }
    }

    private void downloadOddsChangeList(@NonNull final Odds tempOdds) {
        String providerId = tempOdds.getProviderId();
        String url = MATCH_ODDS_CHANGE_LIST_URL.replace(MATCH_ID_PLACEHOLDER, match.getId()).replace(PROVIDER_ID_PLACEHOLDER, providerId);

        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleOddsChangeResponse(response, tempOdds);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleErrorResponse(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Connection", "keep-alive");
                headers.put("Cache-Control", "max-age=0");
                headers.put("Upgrade-Insecure-Requests", "1");
                headers.put("Referer", "http://www.okooo.com/soccer/match/1009999/odds/");
                headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
                headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
                return headers;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String parsed;
                try {
                    parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "gb2312"));
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

    private void handleOddsChangeResponse(String response, @NonNull final Odds tempOdds) {
        final Odds expectedOdds = new Odds(tempOdds); // copy partially

        List<Odds.OddsChange> oddsChangeList = OddsChangeParser.parse(response, tempOdds.getProviderId());

        expectedOdds.setOddsChangeList(oddsChangeList);

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                OddsDao oddsDao = AppRoomDatabase.getInstance(context.get().getApplicationContext()).oddsDao();
                Log.d(TAG, "run: insert into db");
                oddsDao.insert(expectedOdds);
            }
        });
    }

    private List<Odds.OddsChange> parse(@NonNull String response, @NonNull String providerId) {
        return OddsChangeParser.parse(response, providerId);
    }
}
