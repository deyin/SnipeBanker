package io.dylan.snipebanker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import io.dylan.snipebanker.adapters.MatchAdapter;
import io.dylan.snipebanker.callbacks.AnalyzeCallBack;
import io.dylan.snipebanker.models.Match;
import io.dylan.snipebanker.models.MatchParent;
import io.dylan.snipebanker.persist.MatchViewModel;
import io.dylan.snipebanker.tasks.DownloadMatchTask;
import io.dylan.snipebanker.utils.DateUtils;

public class MainActivity extends AppCompatActivity implements AnalyzeCallBack {

    private final static String TAG = MainActivity.class.getName();

    /// UI
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    MatchAdapter matchAdapter;

    private MatchViewModel appViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUi();

        appViewModel = ViewModelProviders.of(this).get(MatchViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadMatchListFromDb();

    }

    private void setupUi() {
        initSwipeRefresh();
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.rv_matches);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        matchAdapter = new MatchAdapter(this, new ArrayList<MatchParent>());
        recyclerView.setAdapter(matchAdapter);
    }

    private void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_matches);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadMatchList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void downloadMatchList() {
        Date todayMatchDate = getTodayMatchDate();
        if(new Date().before(todayMatchDate)) {
            ///if before everyday update time at 11:30, fetch yesterday
            todayMatchDate = DateUtils.getDaysBefore(todayMatchDate, 1);
        }
        Log.d(TAG, "downloadMatchList by date: " + todayMatchDate);
        new DownloadMatchTask(MainActivity.this).execute(todayMatchDate);
    }

    private void loadMatchListFromDb() {
        final Date todayMatchDate = getTodayMatchDate();
        Date start = DateUtils.getDaysBefore(todayMatchDate, 2);
        Date end = DateUtils.getDaysAfter(todayMatchDate, 3);

        Log.d(TAG, "loadMatchListFromDb: start with " + start + ", end with " + end);

        appViewModel.getMatchesByDate(start, end).observe(this, new Observer<List<Match>>() {
            @Override
            public void onChanged(List<Match> matches) {
                List<MatchParent> matchParentList = generateMatchParents(matches);
                matchAdapter.parentListChanged(matchParentList);
            }
        });
    }

    @NonNull
    private static List<MatchParent> generateMatchParents(@NonNull List<Match> matches) {
        Map<Date, List<Match>> dateListMap = groupMatchListByDate(matches);
        Set<Map.Entry<Date, List<Match>>> entries = dateListMap.entrySet();
        List<MatchParent> matchParentList = new ArrayList<>();
        for (Map.Entry<Date, List<Match>> entry : entries) {
            Date date = entry.getKey();
            List<Match> matchList = entry.getValue();
            MatchParent matchParent = new MatchParent(date, matchList);
            matchParentList.add(matchParent);
        }
        return matchParentList;
    }

    private static Date getTodayMatchDate() {
        // default 11:30
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    private static Map<Date, List<Match>> groupMatchListByDate(@NonNull List<Match> matchList) {
        Map<Date, List<Match>> map = new TreeMap<>(); // ordered
        for (Match match : matchList) {
            Date matchDate = match.getMatchDate();
            List<Match> matches = map.get(matchDate);
            if (matches == null) {
                matches = new ArrayList<>();
            }
            matches.add(match);
            map.put(matchDate, matches);
        }
        return map;
    }

    @Override
    public void analyze(Match match) {
        Intent intent = new Intent();
        intent.setClass(this, AnalyzeActivity.class);
        intent.putExtra("match", match);
        startActivity(intent);
    }
}
