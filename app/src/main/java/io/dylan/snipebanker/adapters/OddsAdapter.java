package io.dylan.snipebanker.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;

import java.util.List;

import io.dylan.snipebanker.R;
import io.dylan.snipebanker.models.Match;
import io.dylan.snipebanker.models.Odds;

public class OddsAdapter extends ExpandableRecyclerAdapter<Odds, Odds.OddsChange,
        OddsAdapter.OddsParentViewHolder, OddsAdapter.OddsChildViewHolder> {

    private final Context context;

    private final Match match;

    public OddsAdapter(Context context, @NonNull List<Odds> parentList, Match match) {
        super(parentList);
        this.context = context;
        this.match = match;
    }

    @NonNull
    @Override
    public OddsParentViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View view = LayoutInflater.from(parentViewGroup.getContext()).inflate(R.layout.list_item_odds_parent, parentViewGroup, false);
        return new OddsParentViewHolder(view);
    }

    @NonNull
    @Override
    public OddsChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View view = LayoutInflater.from(childViewGroup.getContext()).inflate(R.layout.list_item_odds_child, childViewGroup, false);
        return new OddsChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(@NonNull OddsParentViewHolder parentViewHolder, int parentPosition, @NonNull Odds parent) {
        parentViewHolder.onBind(parentPosition, parent);
    }

    @Override
    public void onBindChildViewHolder(@NonNull OddsChildViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull Odds.OddsChange child) {
        childViewHolder.onBind(parentPosition, childPosition, child);
    }

    public void parentListChanged(@NonNull List<Odds> oddsList) {
        if (oddsList.isEmpty()) {
            return;
        }
        setParentList(oddsList, true);
        notifyParentDataSetChanged(true);
    }

    class OddsParentViewHolder extends ParentViewHolder<Odds, Odds.OddsChange> {

        TextView providerName;

        TextView startOddsWin;
        TextView startOddsDraw;
        TextView startOddsLose;

        TextView latestOddsWin;
        TextView latestOddsDraw;
        TextView latestOddsLose;

        ImageView arrow;

        public OddsParentViewHolder(@NonNull View itemView) {
            super(itemView);

            initViews(itemView);
        }

        private void initViews(@NonNull View itemView) {
            providerName = itemView.findViewById(R.id.tv_provider_name);

            startOddsWin = itemView.findViewById(R.id.tv_start_odds_win);
            startOddsDraw = itemView.findViewById(R.id.tv_start_odds_draw);
            startOddsLose = itemView.findViewById(R.id.tv_start_odds_lose);

            latestOddsWin = itemView.findViewById(R.id.tv_lates_odds_win);
            latestOddsDraw = itemView.findViewById(R.id.tv_latest_odds_draw);
            latestOddsLose = itemView.findViewById(R.id.tv_latest_odds_lose);

            arrow = itemView.findViewById(R.id.iv_arrow_odds);
        }

        public void onBind(final int parentPosition, Odds parent) {

            providerName.setText(parent.getProviderName());

            Odds.OddsChange started = parent.getStarted();

            startOddsWin.setText(String.valueOf(started.getOddsOfWin()));
            startOddsDraw.setText(String.valueOf(started.getOddsOfDraw()));
            startOddsLose.setText(String.valueOf(started.getOddsOfLose()));

            Odds.OddsChange latest = parent.getLatest();
            latestOddsWin.setText(String.valueOf(latest.getOddsOfWin()));
            latestOddsDraw.setText(String.valueOf(latest.getOddsOfDraw()));
            latestOddsLose.setText(String.valueOf(latest.getOddsOfLose()));

            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arrow.setRotation(!isExpanded() ? 180 : 0);
                    if (!isExpanded()) {
                        expandParent(parentPosition);
                    } else {
                        collapseParent(parentPosition);
                    }
                }
            });
        }
    }

    class OddsChildViewHolder extends ChildViewHolder<Match> {

        TextView hoursBeforeMatchStarted;

        TextView currentOddsWin;
        TextView currentOddsDraw;
        TextView currentOddsLose;

        TextView currentKellyWin;
        TextView currentKellyDraw;
        TextView currentKellyLose;

        TextView lossRatio;

        public OddsChildViewHolder(@NonNull View itemView) {
            super(itemView);

            hoursBeforeMatchStarted = itemView.findViewById(R.id.tv_hours_before_match_started);

            currentOddsWin = itemView.findViewById(R.id.tv_current_odds_win);
            currentOddsDraw = itemView.findViewById(R.id.tv_current_odds_draw);
            currentOddsLose = itemView.findViewById(R.id.tv_current_odds_lose);

            currentKellyWin = itemView.findViewById(R.id.tv_current_kelly_win);
            currentKellyDraw = itemView.findViewById(R.id.tv_current_kelly_draw);
            currentKellyLose = itemView.findViewById(R.id.tv_current_kelly_lose);

            lossRatio = itemView.findViewById(R.id.tv_loss_ratio);
        }


        public void onBind(int parentPosition, int childPosition, final Odds.OddsChange oddsChange) {

            hoursBeforeMatchStarted.setText(String.valueOf(oddsChange.getHoursBeforeMatch()));

            currentOddsWin.setText(String.valueOf(oddsChange.getOddsOfWin()));
            currentOddsDraw.setText(String.valueOf(oddsChange.getOddsOfDraw()));
            currentOddsLose.setText(String.valueOf(oddsChange.getOddsOfLose()));

            currentKellyWin.setText(String.valueOf(oddsChange.getKellyOfWin()));
            currentKellyDraw.setText(String.valueOf(oddsChange.getKellyOfDraw()));
            currentKellyLose.setText(String.valueOf(oddsChange.getKellyOfLose()));

            lossRatio.setText(String.valueOf(oddsChange.getLossRatio()));

        }

    }

}
