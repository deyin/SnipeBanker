package io.dylan.snipebanker.fragments;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.dylan.snipebanker.R;
import io.dylan.snipebanker.adapters.OddsAdapter;
import io.dylan.snipebanker.models.Match;
import io.dylan.snipebanker.models.Odds;
import io.dylan.snipebanker.persist.OddsViewModel;
import io.dylan.snipebanker.tasks.DownloadOddsTask;

@SuppressLint("ValidFragment")
public class OddsFragment extends Fragment {

    private static String TAG = OddsFragment.class.getName();

    private final Match match;

    /// UI
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    OddsAdapter oddsAdapter;

    private Context context;
    private OddsViewModel oddsViewModel;

    private OddsFragment(@NonNull Match match) {
        this.match = match;
    }

    public static OddsFragment newInstance(@NonNull Match match) {
        return new OddsFragment(match);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_odds, container, false);
        context = getContext();

        setupUi(contentView);

        oddsViewModel = ViewModelProviders.of(this).get(OddsViewModel.class);

        return contentView;
    }

    private void setupUi(@NonNull View contentView) {

        initSwipeRefresh(contentView);

        initRecyclerView(contentView);
    }

    private void initRecyclerView(@NonNull View contentView) {
        recyclerView = contentView.findViewById(R.id.rv_odds);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.odds_child_divider));
        recyclerView.addItemDecoration(itemDecorator);

        oddsAdapter = new OddsAdapter(context, new ArrayList<Odds>(), match);
        recyclerView.setAdapter(oddsAdapter);
    }

    private void initSwipeRefresh(@NonNull View contentView) {
        swipeRefreshLayout = contentView.findViewById(R.id.swipe_refresh_odds);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadOddsList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void downloadOddsList() {
        Log.d(TAG, "downloadOddsList of match: " + match);
        new DownloadOddsTask(context, match).execute();
    }


    @Override
    public void onStart() {
        super.onStart();

        oddsViewModel.getAllOdds(match.getId()).observe(this, new Observer<List<Odds>>() {
            @Override
            public void onChanged(@Nullable List<Odds> odds) {
                loadOddsListFromDb();
            }
        });
    }

    private void loadOddsListFromDb() {
        Log.d(TAG, "loadOddsListFromDb of match: "+ match);

        oddsViewModel.getAllOdds(match.getId()).observe(this, new Observer<List<Odds>>() {
            @Override
            public void onChanged(List<Odds> oddsList) {
                for(Odds odds : oddsList) {
                    List<Odds.OddsChange> oddsChangeList = odds.getChildList();
                    Collections.sort(oddsChangeList);
                    odds.setOddsChangeList(oddsChangeList);
                }
                oddsAdapter.parentListChanged(oddsList);
            }
        });
    }
}
