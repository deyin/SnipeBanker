package io.dylan.snipebanker.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.dylan.snipebanker.persist.converters.DateConverter;
import io.dylan.snipebanker.persist.converters.JsonObjectContainerConverter;
import io.dylan.snipebanker.persist.converters.ResultConverter;


@Entity(tableName = "t_odds"
        , foreignKeys = {@ForeignKey(entity = Match.class, parentColumns = "id", childColumns = "matchId")}
        , indices = {@Index(value = "matchId"), @Index(value = "providerId")}
)
public class Odds implements Parent<Odds.OddsChange> {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private String matchId;

    private String providerId;
    private String providerName;

    @TypeConverters({JsonObjectContainerConverter.class})
    private List<OddsChange> oddsChangeList = new ArrayList<>();

    public Odds() {

    }

    public Odds(@NonNull Odds copy) { // without oddsChangeList copy
        this.id = copy.id;
        this.matchId = copy.matchId;
        this.providerId = copy.matchId;
        this.providerName = copy.providerName;
    }


    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public OddsChange getLatest() {
        return oddsChangeList.get(oddsChangeList.size() - 1);
    }

    public OddsChange getStarted() {
        return oddsChangeList.get(0);
    }

    public List<OddsChange> getOddsChangeList() {
        return oddsChangeList;
    }

    public void setOddsChangeList(List<OddsChange> oddsChangeList) {
        this.oddsChangeList = oddsChangeList;
    }

    @Override
    public List<OddsChange> getChildList() {
        return this.oddsChangeList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public static class OddsChange implements Serializable, Comparable<OddsChange> {

        private String providerId = "2"; // sporttery.cn

        @TypeConverters({DateConverter.class})
        private Date time;

        private String hoursBeforeMatch;

        private double oddsOfWin;
        private double oddsOfDraw;
        private double oddsOfLose;

        private double probabilityOfWin;
        private double probabilityOfDraw;
        private double probabilityOfLose;

        private double kellyOfWin;
        private double kellyOfDraw;
        private double kellyOfLose;

        private double lossRatio;

        @TypeConverters({ResultConverter.class})
        private Result expectedResult = Result.UNKNOWN; // default

        @TypeConverters({ResultConverter.class})
        private Result actualResult = Result.UNKNOWN; // default

        public String getProviderId() {
            return providerId;
        }

        public void setProviderId(String providerId) {
            this.providerId = providerId;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }

        public String getHoursBeforeMatch() {
            return hoursBeforeMatch;
        }

        public void setHoursBeforeMatch(String hoursBeforeMatch) {
            this.hoursBeforeMatch = hoursBeforeMatch;
        }

        public double getOddsOfWin() {
            return oddsOfWin;
        }

        public void setOddsOfWin(double oddsOfWin) {
            this.oddsOfWin = oddsOfWin;
        }

        public double getOddsOfDraw() {
            return oddsOfDraw;
        }

        public void setOddsOfDraw(double oddsOfDraw) {
            this.oddsOfDraw = oddsOfDraw;
        }

        public double getOddsOfLose() {
            return oddsOfLose;
        }

        public void setOddsOfLose(double oddsOfLose) {
            this.oddsOfLose = oddsOfLose;
        }

        public double getProbabilityOfWin() {
            return probabilityOfWin;
        }

        public void setProbabilityOfWin(double probabilityOfWin) {
            this.probabilityOfWin = probabilityOfWin;
        }

        public double getProbabilityOfDraw() {
            return probabilityOfDraw;
        }

        public void setProbabilityOfDraw(double probabilityOfDraw) {
            this.probabilityOfDraw = probabilityOfDraw;
        }

        public double getProbabilityOfLose() {
            return probabilityOfLose;
        }

        public void setProbabilityOfLose(double probabilityOfLose) {
            this.probabilityOfLose = probabilityOfLose;
        }

        public double getKellyOfWin() {
            return kellyOfWin;
        }

        public void setKellyOfWin(double kellyOfWin) {
            this.kellyOfWin = kellyOfWin;
        }

        public double getKellyOfDraw() {
            return kellyOfDraw;
        }

        public void setKellyOfDraw(double kellyOfDraw) {
            this.kellyOfDraw = kellyOfDraw;
        }

        public double getKellyOfLose() {
            return kellyOfLose;
        }

        public void setKellyOfLose(double kellyOfLose) {
            this.kellyOfLose = kellyOfLose;
        }

        public Result getExpectedResult() {
            return expectedResult;
        }

        public void setExpectedResult(Result expectedResult) {
            this.expectedResult = expectedResult;
        }

        public double getLossRatio() {
            return lossRatio;
        }

        public void setLossRatio(double lossRatio) {
            this.lossRatio = lossRatio;
        }

        public Result getActualResult() {
            return actualResult;
        }

        public void setActualResult(Result actualResult) {
            this.actualResult = actualResult;
        }

        @Override
        public String toString() {
            return "OddsChange{" +
                    "time=" + time +
                    ", oddsOfWin=" + oddsOfWin +
                    ", oddsOfDraw=" + oddsOfDraw +
                    ", oddsOfLose=" + oddsOfLose +
                    '}';
        }

        @Override
        public int compareTo(OddsChange o) {
            return o.time.compareTo(this.time);
        }
    }
}