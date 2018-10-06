package io.dylan.snipebanker.persist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import io.dylan.snipebanker.models.Handicap;

public class HandicapViewModel extends AndroidViewModel {

    private AppRepository mAppRepository;

    public HandicapViewModel(@NonNull Application application) {
        super(application);
        mAppRepository = new AppRepository(application);
    }

    public <T> void insert(T... array) {
        mAppRepository.insert(array);
    }


    public LiveData<List<Handicap>> getAllHandicaps(String matchId) {
        LiveData<List<Handicap>> allHandicaps = mAppRepository.getAllHandicaps(matchId);
        return allHandicaps;
    }

}
