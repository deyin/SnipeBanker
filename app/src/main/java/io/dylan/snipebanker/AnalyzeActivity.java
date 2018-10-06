package io.dylan.snipebanker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.List;

import io.dylan.snipebanker.fragments.ExponentFragment;
import io.dylan.snipebanker.fragments.HandicapFragment;
import io.dylan.snipebanker.fragments.OddsFragment;
import io.dylan.snipebanker.fragments.ProfitAndLossFragment;
import io.dylan.snipebanker.models.Match;
import io.dylan.snipebanker.models.Odds;
import io.dylan.snipebanker.persist.OddsViewModel;

public class AnalyzeActivity extends AppCompatActivity {

    private Match match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        Intent intent = getIntent();
        if (intent == null || (match = (Match) intent.getSerializableExtra("match")) == null) {
            this.finish();
        }
        if (savedInstanceState == null) {
            loadOddsFragment(match);
        }

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_odds:
                    loadOddsFragment(match);
                    return true;
                case R.id.nav_handicap:
                    loadHandicapFragment(match);
                    return true;
                case R.id.nav_profit_and_loss:
                    loadProfitAndLossFragment(match);
                    return true;
                case R.id.nav_exponent:
                    loadExponentFragment(match);
                    return true;
            }
            return false;
        }
    };

    private void loadExponentFragment(@NonNull Match match) {
        setTitle(R.string.exponent);
        ExponentFragment fragment = ExponentFragment.newInstance(match);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }

    private void loadProfitAndLossFragment(@NonNull Match match) {
        setTitle(R.string.profitAndLoss);
        ProfitAndLossFragment fragment = ProfitAndLossFragment.newInstance(match);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }

    private void loadHandicapFragment(@NonNull Match match) {
        setTitle(R.string.handicap);
        HandicapFragment fragment = HandicapFragment.newInstance(match);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }

    private void loadOddsFragment(@NonNull Match match) {
        setTitle(R.string.odds);
        OddsFragment fragment = OddsFragment.newInstance(match);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }

}
