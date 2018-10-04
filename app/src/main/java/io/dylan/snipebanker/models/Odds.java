package io.dylan.snipebanker.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.Date;
import java.util.List;

import io.dylan.snipebanker.persist.converters.DateConverter;
import io.dylan.snipebanker.persist.converters.JsonObjectContainerConverter;
import io.dylan.snipebanker.persist.converters.JsonObjectConverter;


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

    public static class OddsChange {

        @TypeConverters({DateConverter.class})
        private Date time;

        private String name;

        private double win;

        private double draw;

        private double lose;

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
    }
}