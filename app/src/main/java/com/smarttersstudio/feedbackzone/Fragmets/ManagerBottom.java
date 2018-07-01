package com.smarttersstudio.feedbackzone.Fragmets;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smarttersstudio.feedbackzone.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManagerBottom extends Fragment {


    public ManagerBottom() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manager_bottom, container, false);
    }

}
