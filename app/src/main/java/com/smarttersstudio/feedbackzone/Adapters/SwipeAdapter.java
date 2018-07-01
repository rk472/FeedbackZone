package com.smarttersstudio.feedbackzone.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.smarttersstudio.feedbackzone.Fragmets.AdministrationTop;
import com.smarttersstudio.feedbackzone.Fragmets.FoodTop;
import com.smarttersstudio.feedbackzone.Fragmets.ManagerTop;
import com.smarttersstudio.feedbackzone.Fragmets.SecurityBottom;
import com.smarttersstudio.feedbackzone.Fragmets.SecurityTop;

public class SwipeAdapter extends FragmentStatePagerAdapter {
    public SwipeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        switch(position){
            case 0:
                f = new FoodTop();
                break;
            case 1:
                f = new AdministrationTop();
                break;
            case 2:
                f = new ManagerTop();
                break;
            case 3:
                f = new SecurityTop();
                break;
        }
        return f;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
