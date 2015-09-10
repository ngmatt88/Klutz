package com.duckwarlocks.klutz;

import com.duckwarlocks.klutz.fragments.MainFragment;
import com.duckwarlocks.klutz.fragments.SavedLocationsFragment;
import com.hartsolution.bedrock.BedrockApplication;
import com.hartsolution.bedrock.config.NavigationAction;

/**
 * Created by matt on 9/10/15.
 */
public class KlutzApplication extends BedrockApplication{

    @Override
    public void onCreate() {
        super.onCreate();
    }

        public static final NavigationAction MAIN_SCREEN = new NavigationAction.Builder()
            .replaceFragment(MainFragment.class)
            .setActionBarTitleId(R.string.home_title)
            .addToNavigationDrawer()
            .setNavigationDrawerIconId(R.drawable.ic_home)
            .addDefaultFor(MainActivity.class)
            .build();

        public static final NavigationAction SAVED_LOCATIONS = new NavigationAction.Builder()
                .replaceFragment(SavedLocationsFragment.class)
                .setActionBarTitleId(R.string.saved_locations_title)
                .addToNavigationDrawer()
                .setNavigationDrawerIconId(R.drawable.ic_park)
                .build();
}
