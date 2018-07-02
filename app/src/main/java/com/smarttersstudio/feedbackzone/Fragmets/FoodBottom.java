package com.smarttersstudio.feedbackzone.Fragmets;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.smarttersstudio.feedbackzone.R;


public class FoodBottom extends Fragment {


    private View root;
    private Button submitButton;
    private CheckBox anonymous;
    private EditText feedBackText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root=inflater.inflate(R.layout.fragment_food_bottom, container, false);


        return root;
    }

}
