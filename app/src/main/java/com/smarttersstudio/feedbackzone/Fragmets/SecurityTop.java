package com.smarttersstudio.feedbackzone.Fragmets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarttersstudio.feedbackzone.R;

import java.util.ArrayList;

public class SecurityTop extends Fragment {
    private View root;
    private AppCompatActivity main;
    private PieChart mChart;
    private Typeface tf;
    private DatabaseReference totalRef;
    private float one,two,three,four,five,total;
    public SecurityTop() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_security_top, container, false);
        main=(AppCompatActivity)getActivity();
        mChart = (PieChart)root.findViewById(R.id.chartSecurity);
        totalRef= FirebaseDatabase.getInstance().getReference().child("total").child("security");
        totalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                one=Float.parseFloat(dataSnapshot.child("one").getValue().toString());
                two=Float.parseFloat(dataSnapshot.child("two").getValue().toString());
                three=Float.parseFloat(dataSnapshot.child("three").getValue().toString());
                four=Float.parseFloat(dataSnapshot.child("four").getValue().toString());
                five=Float.parseFloat(dataSnapshot.child("five").getValue().toString());
                total=one+two+three+four+five;
                initializeChart();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        totalRef.keepSynced(true);
        return root;
    }
    private void initializeChart(){
        //Chart Initialization
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        //mChart.setExtraOffsets(5, 10, 5, 5);
        mChart.setDragDecelerationFrictionCoef(0.95f);
        tf = Typeface.createFromAsset(main.getAssets(), "OpenSans-Regular.ttf");
        mChart.setCenterTextTypeface(Typeface.createFromAsset(main.getAssets(), "OpenSans-Light.ttf"));
        mChart.setCenterText("Summery");
        // mChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);
        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);
        mChart.setHoleRadius(45f);
        mChart.setTransparentCircleRadius(49f);
        mChart.setDrawCenterText(true);
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);
        setData(5, 100);
        mChart.animateXY(1400, 1400);
        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setTextSize(12f);
        l.setEnabled(true);
    }
    private void setData(int count, float range) {

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        entries.add(new PieEntry(one/total, ""+1+"*"));
        entries.add(new PieEntry(two/total, ""+2+"*"));
        entries.add(new PieEntry(three/total, ""+3+"*"));
        entries.add(new PieEntry(four/total, ""+4+"*"));
        entries.add(new PieEntry(five/total, ""+5+"*"));
        PieDataSet dataSet = new PieDataSet(entries, "Security");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        //dataSet.setUsingSliceColorAsValueLineColor(true);

        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(tf);
        mChart.setData(data);
        // undo all highlights
        mChart.highlightValues(null);
        mChart.invalidate();
    }
    @Override
    public void onStart() {
        super.onStart();
        mChart.animateXY(1400, 1400);
    }
}
