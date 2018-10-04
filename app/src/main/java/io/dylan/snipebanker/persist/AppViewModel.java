package io.dylan.snipebanker.persist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

import io.dylan.snipebanker.models.Match;

public class AppViewModel extends AndroidViewModel {

    private AppRepository mAppRepository;

    public AppViewModel(@NonNull Application application) {
        super(application);
        mAppRepository = new AppRepository(application);
    }

    public <T> void insert(T... array) {
        mAppRepository.insert(array);
    }

    public LiveData<List<Match>> getMatchesByDate(Date start, Date end) {
        LiveData<List<Match>> matchesByDate = mAppRepository.getMatchesByDate(start, end);
        return matchesByDate;
    }

//    public LiveData<List<Odds>> getAllOdds(String matchId) {
//        LiveData<List<Odds>> allOdds = mAppRepository.getAllOdds(matchId);
//        return allOdds;
//    }
//
//    public LiveData<List<Handicap>> getAllHandicaps(String matchId) {
//        LiveData<List<Handicap>> allHandicaps = mAppRepository.getAllHandicaps(matchId);
//        return allHandicaps;
//    }

}
