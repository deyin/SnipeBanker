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


@Entity(tableName = "t_handicap"
        , foreignKeys = {@ForeignKey(entity = Match.class, parentColumns = "id", childColumns = "matchId")}
        , indices = {@Index(value = "matchId", unique = true), @Index(value = "providerId")}
)
public class Handicap implements Parent<Handicap.HandicapChange> {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private int matchId;

    private String providerId;

    private String providerName;

    @TypeConverters({JsonObjectConverter.class})
    private HandicapChange latest;

    @TypeConverters({JsonObjectConverter.class})
    private HandicapChange started;

    @TypeConverters({JsonObjectContainerConverter.class})
    private List<HandicapChange> handicapChangeList;

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

    public HandicapChange getLatest() {
        return latest;
    }

    public void setLatest(HandicapChange latest) {
        this.latest = latest;
    }

    public HandicapChange getStarted() {
        return started;
    }

    public void setStarted(HandicapChange started) {
        this.started = started;
    }

    public List<HandicapChange> getHandicapChangeList() {
        return handicapChangeList;
    }

    public void setHandicapChangeList(List<HandicapChange> handicapChangeList) {
        this.handicapChangeList = handicapChangeList;
    }

    @Override
    public List<HandicapChange> getChildList() {
        return this.handicapChangeList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public static class HandicapChange {

        @TypeConverters({DateConverter.class})
        private Date time;

        private String name;

        private double waterOfHome;

        private double waterOfAway;

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

        public double getWaterOfHome() {
            return waterOfHome;
        }

        public void setWaterOfHome(double waterOfHome) {
            this.waterOfHome = waterOfHome;
        }

        public double getWaterOfAway() {
            return waterOfAway;
        }

        public void setWaterOfAway(double waterOfAway) {
            this.waterOfAway = waterOfAway;
        }
    }
}