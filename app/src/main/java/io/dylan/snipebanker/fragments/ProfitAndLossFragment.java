package io.dylan.snipebanker.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.dylan.snipebanker.R;

public class ProfitAndLossFragment extends Fragment {

    public static ProfitAndLossFragment newInstance() {

        return new ProfitAndLossFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profit_and_loss, container, false);
    }
}
