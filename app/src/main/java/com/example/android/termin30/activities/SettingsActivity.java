package com.example.android.termin30.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.example.android.termin30.R;

/**
 * Created by BBLOJB on 25.11.2017..
 */

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            //odgovara na poziv u Action bar-u Up/Home dugme
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static class  PrefsFragment extends PreferenceFragment{
        @Override
        public void onCreate( Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            //ucitava podatke iz XML-a
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
