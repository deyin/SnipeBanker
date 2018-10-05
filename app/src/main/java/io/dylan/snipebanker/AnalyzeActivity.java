package io.dylan.snipebanker;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import io.dylan.snipebanker.fragments.ExponentFragment;
import io.dylan.snipebanker.fragments.HandicapFragment;
import io.dylan.snipebanker.fragments.OddsFragment;
import io.dylan.snipebanker.fragments.ProfitAndLossFragment;

public class AnalyzeActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_odds:
                    loadOddsFragment();
                    return true;
                case R.id.nav_handicap:
                    loadHandicapFragment();
                    return true;
                case R.id.nav_profit_and_loss:
                    loadProfitAndLossFragment();
                    return true;
                case R.id.nav_exponent:
                    loadExponentFragment();
                    return true;
            }
            return false;
        }
    };

    private void loadExponentFragment() {
        setTitle(R.string.exponent);
        ExponentFragment fragment = ExponentFragment.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }

    private void loadProfitAndLossFragment() {
        setTitle(R.string.profitAndLoss);
        ProfitAndLossFragment fragment = ProfitAndLossFragment.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }

    private void loadHandicapFragment() {
        setTitle(R.string.handicap);
        HandicapFragment fragment = HandicapFragment.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }

    private void loadOddsFragment() {
        setTitle(R.string.odds);
        OddsFragment fragment = OddsFragment.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        if (savedInstanceState == null) {
            loadOddsFragment();
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
