package io.dylan.snipebanker.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.dylan.snipebanker.persist.converters.DateConverter;
import io.dylan.snipebanker.persist.converters.JsonObjectContainerConverter;
import io.dylan.snipebanker.persist.converters.JsonObjectConverter;
import io.dylan.snipebanker.persist.converters.ResultConverter;


@Entity(tableName = "t_odds"
        , foreignKeys = {@ForeignKey(entity = Match.class, parentColumns = "id", childColumns = "matchId")}
        , indices = {@Index(value = "matchId", unique = true), @Index(value = "providerId")}
)
public class Odds implements Parent<Odds.OddsChange> {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private int matchId;

    private String providerId;
    private String providerName;

    @TypeConverters({JsonObjectConverter.class})
    private OddsChange latest;

    @TypeConverters({JsonObjectConverter.class})
    private OddsChange started;

    @TypeConverters({JsonObjectContainerConverter.class})
    private List<OddsChange> oddsChangeList;

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
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
        return latest;
    }

    public void setLatest(OddsChange latest) {
        this.latest = latest;
    }

    public OddsChange getStarted() {
        return started;
    }

    public void setStarted(OddsChange started) {
        this.started = started;
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

    public static class OddsChange implements Serializable {

        @TypeConverters({DateConverter.class})
        private Date time;

        private double win;

        private double draw;

        private double lose;

        @TypeConverters({ResultConverter.class})
        private Result expectedResult = Result.UNKNOWN; // default

        @TypeConverters({ResultConverter.class})
        private Result actualResult = Result.UNKNOWN; // default

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }

        public double getWin() {
            return win;
        }

        public void setWin(double win) {
            this.win = win;
        }

        public double getDraw() {
            return draw;
        }

        public void setDraw(double draw) {
            this.draw = draw;
        }

        public double getLose() {
            return lose;
        }

        public void setLose(double lose) {
            this.lose = lose;
        }

        public Result getExpectedResult() {
            return expectedResult;
        }

        public void setExpectedResult(Result expectedResult) {
            this.expectedResult = expectedResult;
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
                    ", win=" + win +
                    ", draw=" + draw +
                    ", lose=" + lose +
                    '}';
        }
    }
}