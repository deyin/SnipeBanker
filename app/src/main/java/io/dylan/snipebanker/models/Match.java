package io.dylan.snipebanker.models;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

import io.dylan.snipebanker.persist.converters.DateConverter;
import io.dylan.snipebanker.persist.converters.JsonObjectConverter;
import io.dylan.snipebanker.persist.converters.StatusConverter;

@Entity(tableName = "t_match", indices = {@Index(value = "matchNo")})
public class Match implements Serializable {

    @NonNull
    @PrimaryKey
    private String id = "N/A";

    @NonNull
    private String matchNo = "000";

    @NonNull
    @TypeConverters({JsonObjectConverter.class})
    @Embedded(prefix = "league")
    private League league = new League();

    @NonNull
    @TypeConverters({DateConverter.class})
    private Date matchDate = new Date();

    @NonNull
    @TypeConverters({DateConverter.class})
    private Date matchTime = new Date();

    @NonNull
    @Embedded(prefix = "home")
    @TypeConverters({JsonObjectConverter.class})
    private Team home = new Team();

    @Embedded(prefix = "away")
    @TypeConverters({JsonObjectConverter.class})
    private Team away = new Team();

    @TypeConverters({StatusConverter.class})
    private Status status = Status.NOT_STARTED;

    private String finishedScore;

    private int handicaps = 0;

    @TypeConverters({JsonObjectConverter.class})
    @Embedded(prefix = "odds")
    private Odds.OddsChange oddsOfSporttery;

    @TypeConverters({JsonObjectConverter.class})
    @Embedded(prefix = "handicap_odds")
    private Odds.OddsChange handicapOddsOfSporttery;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getMatchNo() {
        return matchNo;
    }

    public void setMatchNo(@NonNull String matchNo) {
        this.matchNo = matchNo;
    }

    @NonNull
    public League getLeague() {
        return league;
    }

    public void setLeague(@NonNull League league) {
        this.league = league;
    }

    @NonNull
    public Date getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(@NonNull Date matchDate) {
        this.matchDate = matchDate;
    }

    @NonNull
    public Date getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(@NonNull Date matchTime) {
        this.matchTime = matchTime;
    }

    @NonNull
    public Team getHome() {
        return home;
    }

    public void setHome(@NonNull Team home) {
        this.home = home;
    }

    @NonNull
    public Team getAway() {
        return away;
    }

    public void setAway(@NonNull Team away) {
        this.away = away;
    }

    public String getFinishedScore() {
        return finishedScore;
    }

    public void setFinishedScore(String finishedScore) {
        this.finishedScore = finishedScore;
    }

    public int getHandicaps() {
        return handicaps;
    }

    public void setHandicaps(int handicaps) {
        this.handicaps = handicaps;
    }

    public Odds.OddsChange getOddsOfSporttery() {
        return oddsOfSporttery;
    }

    public void setOddsOfSporttery(Odds.OddsChange oddsOfSporttery) {
        this.oddsOfSporttery = oddsOfSporttery;
    }

    public Odds.OddsChange getHandicapOddsOfSporttery() {
        return handicapOddsOfSporttery;
    }

    public void setHandicapOddsOfSporttery(Odds.OddsChange handicapOddsOfSporttery) {
        this.handicapOddsOfSporttery = handicapOddsOfSporttery;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public boolean hasFinished() {
        return Status.FINISHED.equals(status);
    }

    public static class League implements Serializable {
        private int id = 0;

        @NonNull
        private String name = "N/A";

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @NonNull
        public String getName() {
            return name;
        }

        public void setName(@NonNull String name) {
            this.name = name;
        }
    } // end class League

    public static class Team implements Serializable {
        @NonNull
        private String name = "N/A";
        private String league;
        private int ranking;

        @NonNull
        public String getName() {
            return name;
        }

        public void setName(@NonNull String name) {
            this.name = name;
        }

        public String getLeague() {
            return league;
        }

        public void setLeague(String league) {
            this.league = league;
        }

        public int getRanking() {
            return ranking;
        }

        public void setRanking(int ranking) {
            this.ranking = ranking;
        }

        @NonNull
        @Override
        public String toString() {
            return "Team{" +
                    "name='" + name + '\'' +
                    ", league='" + league + '\'' +
                    ", ranking=" + ranking +
                    '}';
        }
    } // end class Team

} // end class Match
