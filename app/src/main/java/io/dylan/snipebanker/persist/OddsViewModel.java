package io.dylan.snipebanker.persist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import io.dylan.snipebanker.models.Odds;

public class OddsViewModel extends AndroidViewModel {

    private AppRepository mAppRepository;

    public OddsViewModel(@NonNull Application application) {
        super(application);
        mAppRepository = new AppRepository(application);
    }

    public <T> void insert(T... array) {
        mAppRepository.insert(array);
    }

    public LiveData<List<Odds>> getAllOdds(String matchId) {
        LiveData<List<Odds>> allOdds = mAppRepository.getAllOdds(matchId);
        return allOdds;
    }

}
