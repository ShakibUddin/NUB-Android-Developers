package com.nubandroiddev.nubandroiddevelopers.shakib;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {

    int totalTabs;

    public TabAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.totalTabs = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Skills SkillsFragment = new Skills();
                return SkillsFragment;
            case 1:
                Projects projectsFragment = new Projects();
                return projectsFragment;
            case 2:
                Achievements achievementsFragment = new Achievements();
                return achievementsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
