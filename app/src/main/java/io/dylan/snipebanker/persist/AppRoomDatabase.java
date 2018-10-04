package io.dylan.snipebanker.persist;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import io.dylan.snipebanker.models.Handicap;
import io.dylan.snipebanker.models.Match;
import io.dylan.snipebanker.models.Odds;
import io.dylan.snipebanker.persist.daos.HandicapDao;
import io.dylan.snipebanker.persist.daos.MatchDao;
import io.dylan.snipebanker.persist.daos.OddsDao;


@Database(entities = {Match.class, Odds.class, Handicap.class}, version = 1)
public abstract class AppRoomDatabase extends RoomDatabase {

    private static volatile AppRoomDatabase instance;

    public static AppRoomDatabase getInstance(@NonNull final Context context) {
        if (instance == null) {
            synchronized (AppRoomDatabase.class) {
                if (instance == null) { // double check
                    instance = Room.databaseBuilder(context.getApplicationContext(), AppRoomDatabase.class, "app_database").build();
                }
            }
        }
        return instance;
    }

    public abstract MatchDao matchDao();

    public abstract OddsDao oddsDao();

    public abstract HandicapDao handicapDao();


}
