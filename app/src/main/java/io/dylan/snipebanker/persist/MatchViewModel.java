package io.dylan.snipebanker.persist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

import io.dylan.snipebanker.models.Handicap;
import io.dylan.snipebanker.models.Match;
import io.dylan.snipebanker.models.Odds;

public class MatchViewModel extends AndroidViewModel {

    private AppRepository mAppRepository;

    public MatchViewModel(@NonNull Application application) {
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
}
