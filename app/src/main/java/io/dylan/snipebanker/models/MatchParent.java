package io.dylan.snipebanker.models;

import android.support.annotation.NonNull;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.Date;
import java.util.List;

import io.dylan.snipebanker.persist.converters.DateConverter;

public class MatchParent implements Parent<Match> {

    private final Date date;

    private final List<Match> childList;

    public MatchParent(@NonNull Date date, @NonNull List<Match> childList) {
        this.date = date;
        this.childList = childList;
    }

    @Override
    public List<Match> getChildList() {
        return childList;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public String getDisplayTitle() {
        String ymdString = DateConverter.dateToYmdString(date);
        int size = childList.size();
        return String.format(ymdString + "共%3d场比赛", size);
    }
}
