package io.dylan.snipebanker.models;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

public class MatchParent implements Parent<Match> {

    private List<Match> childList;

    @Override
    public List<Match> getChildList() {
        return childList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
