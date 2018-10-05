package io.dylan.snipebanker.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import io.dylan.snipebanker.R;
import io.dylan.snipebanker.models.Match;
import io.dylan.snipebanker.models.MatchParent;
import io.dylan.snipebanker.models.Odds;
import io.dylan.snipebanker.models.Result;
import io.dylan.snipebanker.persist.converters.DateConverter;

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
        setParentList(matchParentList, true);
        notifyDataSetChanged();
        expandToLatestMatch(matchParentList);
    }

    private void expandToLatestMatch(@NonNull List<MatchParent> matchParentList) {
        Date now = DateConverter.dateFromYmdString(DateConverter.dateToYmdString(new Date()));
        int pos = 0;
        for(MatchParent matchParent : matchParentList) {
            if(matchParent.getDate().compareTo(now) >= 0) {
                break;
            }
            pos++;
        }
        expandParent(pos);
    }

    class MatchParentViewHolder extends ParentViewHolder<MatchParent, Match> {

        TextView displayTitle;

        /**
         * Default constructor.
         *
         * @param itemView The {@link View} being hosted in this ViewHolder
         */
        public MatchParentViewHolder(@NonNull View itemView) {
            super(itemView);
            displayTitle = itemView.findViewById(R.id.tv_display_title);
        }

        public void onBind(int parentPosition, MatchParent parent) {
            displayTitle.setText(parent.getDisplayTitle());
        }
    }

    class MatchChildViewHolder extends ChildViewHolder<Match> {

        TextView matchNo;
        TextView league;
        TextView time;

        TextView vs;


        TextView oddsWin;
        TextView oddsDraw;
        TextView oddsLose;

        TextView handicaps;

        TextView handicapsWin;
        TextView handicapsDraw;
        TextView handicapsLose;

        Map<Result, Integer> resultViewMap = new HashMap<>();

        /**
         * Default constructor.
         *
         * @param itemView The {@link View} being hosted in this ViewHolder
         */
        public MatchChildViewHolder(@NonNull View itemView) {

            super(itemView);

            matchNo = itemView.findViewById(R.id.tv_match_no);
            vs = itemView.findViewById(R.id.tv_vs);
            time = itemView.findViewById(R.id.tv_time);
            league = itemView.findViewById(R.id.tv_league);

            oddsWin = itemView.findViewById(R.id.tv_odds_win);
            oddsDraw = itemView.findViewById(R.id.tv_odds_draw);
            oddsLose = itemView.findViewById(R.id.tv_odds_lose);

            handicaps = itemView.findViewById(R.id.tv_handicaps);

            handicapsWin = itemView.findViewById(R.id.tv_handicaps_win);
            handicapsDraw = itemView.findViewById(R.id.tv_handicaps_draw);
            handicapsLose = itemView.findViewById(R.id.tv_handicaps_lose);

            initResultViewMap();
        }

        private void initResultViewMap() {
            resultViewMap.put(Result.WIN, R.id.tv_odds_win);
            resultViewMap.put(Result.DRAW, R.id.tv_odds_draw);
            resultViewMap.put(Result.LOSE, R.id.tv_odds_lose);

            resultViewMap.put(Result.HANDICAP_WIN, R.id.tv_handicaps_win);
            resultViewMap.put(Result.HANDICAP_DRAW, R.id.tv_handicaps_draw);
            resultViewMap.put(Result.HANDICAP_LOSE, R.id.tv_handicaps_lose);
        }


        public void onBind(int parentPosition, int childPosition, Match child) {
            matchNo.setText(child.getMatchNo());
            league.setText(child.getMatchName());
            time.setText(DateConverter.dateToHmString(child.getMatchTime()));

            Match.Team home = child.getHome();
            Match.Team away = child.getAway();

            if (child.isFinished() && child.getFinishedScore() != null) {
                vs.setText(showHtml(context.getResources().getString(R.string.strVsFinishedScore,
                        home.getRanking(),
                        home.getName(),
                        "<font color=\"#D81B60\">" + child.getFinishedScore() + "</font>",
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

            handicaps.setText(String.valueOf(child.getHandicaps() > 0 ? "+" + child.getHandicaps() : child.getHandicaps()));

            onBindOdds(child);

        }

        @SuppressWarnings("deprecation")
        public Spanned showHtml(String html) {
            Spanned result;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                result = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT);
            } else {
                result = Html.fromHtml(html);
            }
            return result;
        }

        private void onBindOdds(Match match) {
            Odds.OddsChange oddsOfSporttery = match.getOddsOfSporttery();
            if (oddsOfSporttery != null) {
                this.oddsWin.setText(String.valueOf(oddsOfSporttery.getWin()));
                this.oddsDraw.setText(String.valueOf(oddsOfSporttery.getDraw()));
                this.oddsLose.setText(String.valueOf(oddsOfSporttery.getLose()));
            }

            enableOddsViews(!match.isFinished());
            setActualResultOfOddsView(oddsOfSporttery);

            Odds.OddsChange handicapOddsOfSporttery = match.getHandicapOddsOfSporttery();
            if (handicapOddsOfSporttery != null) {
                this.handicapsWin.setText(String.valueOf(handicapOddsOfSporttery.getWin()));
                this.handicapsDraw.setText(String.valueOf(handicapOddsOfSporttery.getDraw()));
                this.handicapsLose.setText(String.valueOf(handicapOddsOfSporttery.getLose()));
            }
            enableHandicapOddsViews(!match.isFinished());
            setActualResultOfHandicapOddsView(handicapOddsOfSporttery);
        }

        private void setActualResultOfOddsView(@NonNull Odds.OddsChange oddsOfSporttery) {
            Result actualResult = oddsOfSporttery.getActualResult();
            Integer resId = resultViewMap.get(actualResult);
            List<TextView> oddsViewList = getOddsViewList();
            for(TextView textView : oddsViewList) {
                if(resId!= null && resId.equals(textView.getId())) {
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
            for(TextView textView : oddsViewList) {
                if(resId!= null && resId.equals(textView.getId())) {
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