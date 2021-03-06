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

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.dylan.snipebanker.R;
import io.dylan.snipebanker.callbacks.AnalyzeCallBack;
import io.dylan.snipebanker.models.Match;
import io.dylan.snipebanker.models.MatchParent;
import io.dylan.snipebanker.models.Odds;
import io.dylan.snipebanker.models.Result;
import io.dylan.snipebanker.models.Status;
import io.dylan.snipebanker.persist.converters.DateConverter;
import io.dylan.snipebanker.utils.HtmlUtils;

public class MatchAdapter extends ExpandableRecyclerAdapter<MatchParent, Match,
        MatchAdapter.MatchParentViewHolder, MatchAdapter.MatchChildViewHolder> {

    private final Context context;

    public MatchAdapter(Context context, @NonNull List<MatchParent> parentList) {
        super(parentList);
        this.context = context;
    }

    @NonNull
    @Override
    public MatchParentViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View view = LayoutInflater.from(parentViewGroup.getContext()).inflate(R.layout.list_item_match_parent, parentViewGroup, false);
        return new MatchParentViewHolder(view);
    }

    @NonNull
    @Override
    public MatchChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View view = LayoutInflater.from(childViewGroup.getContext()).inflate(R.layout.list_item_match, childViewGroup, false);
        return new MatchChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(@NonNull MatchParentViewHolder parentViewHolder, int parentPosition, @NonNull MatchParent parent) {
        parentViewHolder.onBind(parentPosition, parent);
    }

    @Override
    public void onBindChildViewHolder(@NonNull MatchChildViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull Match child) {
        childViewHolder.onBind(parentPosition, childPosition, child);
    }

    public void parentListChanged(@NonNull List<MatchParent> matchParentList) {
        if (matchParentList.isEmpty()) {
            return;
        }
        setParentList(matchParentList, true);
        notifyParentDataSetChanged(true);
        expandToLatestMatchParent(matchParentList);
    }

    private void expandToLatestMatchParent(@NonNull List<MatchParent> matchParentList) {
        Date now = DateConverter.dateFromYmdString(DateConverter.dateToYmdString(new Date()));
        int pos = 0;
        for (MatchParent matchParent : matchParentList) {
            if (matchParent.getDate().compareTo(now) >= 0) {
                break;
            }
            pos++;
        }
        if (pos >= matchParentList.size()) {
            pos--;
        }
        expandParent(pos);
    }

    class MatchParentViewHolder extends ParentViewHolder<MatchParent, Match> {

        TextView displayTitle;

        ImageView arrow;


        public MatchParentViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(@NonNull View itemView) {
            displayTitle = itemView.findViewById(R.id.tv_display_title);
            arrow = itemView.findViewById(R.id.iv_arrow_match);
        }

        public void onBind(final int parentPosition, MatchParent parent) {
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
            displayTitle.setText(parent.getDisplayTitle());
        }
    }

    class MatchChildViewHolder extends ChildViewHolder<Match> {

        TextView matchNo;
        TextView league;
        TextView time;
        TextView analyze;

        TextView vs;

        TextView oddsWin;
        TextView oddsDraw;
        TextView oddsLose;

        TextView handicaps;

        TextView handicapsWin;
        TextView handicapsDraw;
        TextView handicapsLose;

        Map<Result, Integer> resultViewMap = new HashMap<>();


        public MatchChildViewHolder(@NonNull View itemView) {

            super(itemView);

            initViews(itemView);

            initResultViewMap();
        }

        private void initViews(@NonNull View itemView) {
            matchNo = itemView.findViewById(R.id.tv_match_no);
            vs = itemView.findViewById(R.id.tv_vs);
            league = itemView.findViewById(R.id.tv_league);
            time = itemView.findViewById(R.id.tv_time);
            analyze = itemView.findViewById(R.id.tv_analyze);

            oddsWin = itemView.findViewById(R.id.tv_odds_win);
            oddsDraw = itemView.findViewById(R.id.tv_odds_draw);
            oddsLose = itemView.findViewById(R.id.tv_odds_lose);

            handicaps = itemView.findViewById(R.id.tv_handicaps);

            handicapsWin = itemView.findViewById(R.id.tv_handicaps_win);
            handicapsDraw = itemView.findViewById(R.id.tv_handicaps_draw);
            handicapsLose = itemView.findViewById(R.id.tv_handicaps_lose);
        }

        private void initResultViewMap() {
            resultViewMap.put(Result.WIN, R.id.tv_odds_win);
            resultViewMap.put(Result.DRAW, R.id.tv_odds_draw);
            resultViewMap.put(Result.LOSE, R.id.tv_odds_lose);

            resultViewMap.put(Result.HANDICAP_WIN, R.id.tv_handicaps_win);
            resultViewMap.put(Result.HANDICAP_DRAW, R.id.tv_handicaps_draw);
            resultViewMap.put(Result.HANDICAP_LOSE, R.id.tv_handicaps_lose);
        }


        public void onBind(int parentPosition, int childPosition, final Match match) {
            matchNo.setText(match.getMatchNo());
            league.setText(match.getLeague().getName());
            time.setText(DateConverter.dateToHmString(match.getMatchTime()));
            analyze.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context instanceof AnalyzeCallBack) {
                        ((AnalyzeCallBack) context).analyze(match);
                    }
                }
            });

            Match.Team home = match.getHome();
            Match.Team away = match.getAway();

            if (match.getStatus() == Status.FINISHED & match.getFinishedScore() != null) {
                vs.setText(HtmlUtils.showHtml(context.getResources().getString(R.string.strVsFinishedScore,
                        home.getRanking(),
                        home.getName(),
                        "<font color=\"#D81B60\">" + match.getFinishedScore() + "</font>",
                        away.getName(),
                        away.getRanking())));
            } else {
                vs.setText(context.getResources().getString(R.string.strVs,
                        home.getRanking(),
                        home.getName(),
                        away.getName(),
                        away.getRanking()
                ));
            }

            handicaps.setText(String.valueOf(match.getHandicaps() > 0 ? "+" + match.getHandicaps() : match.getHandicaps()));

            onBindOdds(match);

        }

        private void onBindOdds(Match match) {
            Odds.OddsChange oddsOfSporttery = match.getOddsOfSporttery();
            if (oddsOfSporttery != null) {
                this.oddsWin.setText(String.valueOf(oddsOfSporttery.getOddsOfWin()));
                this.oddsDraw.setText(String.valueOf(oddsOfSporttery.getOddsOfDraw()));
                this.oddsLose.setText(String.valueOf(oddsOfSporttery.getOddsOfLose()));
            }

            enableOddsViews(!match.hasFinished());
            setActualResultOfOddsView(oddsOfSporttery);

            Odds.OddsChange handicapOddsOfSporttery = match.getHandicapOddsOfSporttery();
            if (handicapOddsOfSporttery != null) {
                this.handicapsWin.setText(String.valueOf(handicapOddsOfSporttery.getOddsOfWin()));
                this.handicapsDraw.setText(String.valueOf(handicapOddsOfSporttery.getOddsOfDraw()));
                this.handicapsLose.setText(String.valueOf(handicapOddsOfSporttery.getOddsOfLose()));
            }
            enableHandicapOddsViews(!match.hasFinished());
            setActualResultOfHandicapOddsView(handicapOddsOfSporttery);
        }

        private void setActualResultOfOddsView(@NonNull Odds.OddsChange oddsOfSporttery) {
            Result actualResult = oddsOfSporttery.getActualResult();
            Integer resId = resultViewMap.get(actualResult);
            List<TextView> oddsViewList = getOddsViewList();
            for (TextView textView : oddsViewList) {
                if (resId != null && resId.equals(textView.getId())) {
                    textView.setBackgroundResource(R.color.colorActualResult);
                    continue;
                }
                textView.setBackgroundResource(R.color.colorOdds);
            }
        }

        private void setActualResultOfHandicapOddsView(@NonNull Odds.OddsChange handicapOddsOfSporttery) {
            Result actualResult = handicapOddsOfSporttery.getActualResult();
            Integer resId = resultViewMap.get(actualResult);
            List<TextView> oddsViewList = getHandicapOddsViewList();
            for (TextView textView : oddsViewList) {
                if (resId != null && resId.equals(textView.getId())) {
                    textView.setBackgroundResource(R.color.colorActualResult);
                    continue;
                }
                textView.setBackgroundResource(R.color.colorOdds);
            }
        }

        private List<TextView> getOddsViewList() {
            List<TextView> oddsList = Arrays.asList(new TextView[]{oddsWin, oddsDraw, oddsLose});
            return oddsList;
        }

        private List<TextView> getHandicapOddsViewList() {
            List<TextView> handicapOddsList = Arrays.asList(new TextView[]{handicapsWin, handicapsDraw, handicapsLose});
            return handicapOddsList;
        }

        private void enableOddsViews(boolean flag) {
            for (TextView oddsView : getOddsViewList()) {
                oddsView.setEnabled(flag);
            }
        }

        private void enableHandicapOddsViews(boolean flag) {
            for (TextView oddsView : getHandicapOddsViewList()) {
                oddsView.setEnabled(flag);
            }
        }
    }

}
