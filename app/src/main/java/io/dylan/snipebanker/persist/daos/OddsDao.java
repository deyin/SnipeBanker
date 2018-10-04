package io.dylan.snipebanker.persist.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import io.dylan.snipebanker.models.Odds;

@Dao
public interface OddsDao extends BaseDao<Odds> {

    @Query("SELECT * FROM t_odds WHERE matchId = :matchId")
    LiveData<List<Odds>> getAllOdds(String matchId);

}
