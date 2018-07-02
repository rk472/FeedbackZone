package com.smarttersstudio.feedbackzone.Fragmets;


import android.graphics.Color;
import android.graphics.Typeface;
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
import com.smarttersstudio.feedbackzone.R;

import java.util.ArrayList;

public class ManagerTop extends Fragment {
    private View root;
    private AppCompatActivity main;
    private PieChart mChart;
    private Typeface tf;
    public ManagerTop() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_manager_top, container, false);
        main=(AppCompatActivity)getActivity();
        mChart = (PieChart)root.findViewById(R.id.chartManager);
        initializeChart();
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

        for (int i = 0; i < count; i++) {
            String mParties = null;
            entries.add(new PieEntry((float) (Math.random() * mult) + mult / 5, (i+1)+"*"));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Manager");
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
        Toast.makeText(main, "Manager Start", Toast.LENGTH_SHORT).show();
        super.onResume();
        mChart.animateXY(1400, 1400);
    }
}
