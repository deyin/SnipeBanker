package io.dylan.snipebanker.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.dylan.snipebanker.R;
import io.dylan.snipebanker.models.Match;

@SuppressLint("ValidFragment")
public class ExponentFragment extends Fragment {

    private final Match match;

    private ExponentFragment(Match match) {
        this.match = match;
    }

    public static ExponentFragment newInstance(Match match) {

        return new ExponentFragment(match);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exponent, container, false);
    }
}
