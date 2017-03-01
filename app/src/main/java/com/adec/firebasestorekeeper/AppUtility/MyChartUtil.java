package com.adec.firebasestorekeeper.AppUtility;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;

import com.adec.firebasestorekeeper.Model.StoreTransactionModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sohel on 2/8/2017.
 */

public class MyChartUtil {
    private Context context;

    public MyChartUtil(Context context) {
        this.context = context;
    }

    public PieChart getStorePieChart(List<StoreTransactionModel> storeTransactionModelList){
        PieChart pieChart = new PieChart(context);
        pieChart.setUsePercentValues(true);
        //pieChart.setDescription();

        // enable hole and configure
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(false);
        pieChart.setDrawSliceText(true);
        //pieChart.setDrawHoleEnabled(false);
        pieChart.getLegend().setEnabled(true);

        pieChart.setRotationAngle(0);

        pieChart.setRotationEnabled(true);

        pieChart.animateXY(1000,1000);

        List<StoreTransactionModel> modifiedList = getModifiedList(storeTransactionModelList);

        List<Entry> pieEntries = getPieEntriesForStore(modifiedList);
        List<String> labels = getPieLabelForStore(modifiedList);

        PieDataSet dataSet = new PieDataSet(pieEntries,"Store Sales");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setValueTextSize(6);
        //dataSet.setValueTypeface(tf);

        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData(labels,dataSet);
        data.setValueFormatter(new MyValueFormatter());
        pieChart.setData(data);

        pieChart.setDescription("Store Sales");

        pieChart.invalidate();

        return pieChart;

    }

    public View getBarChart(List<StoreTransactionModel> storeTransactionModelList){
        //Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),Constant.T_RAILWAY_REGULAR);
        BarChart barChart = new BarChart(context);

        barChart.setMaxVisibleValueCount(60);

        barChart.setPinchZoom(false);

        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(true);
        //barChart.setDrawGridBackground(false);

        XAxis xAxis = barChart.getXAxis();
        //xAxis.setTypeface(tf);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setDrawAxisLine(false);
        yAxis.setEnabled(false);
        //yAxis.set



        barChart.getAxisLeft().setDrawGridLines(false);
        //barChart.getAxisLeft().setTypeface(tf);
        barChart.getAxis(YAxis.AxisDependency.RIGHT).setDrawAxisLine(false);
        barChart.getAxis(YAxis.AxisDependency.RIGHT).setDrawLabels(false);

        barChart.animateXY(1000,1000);
        //barChart.setOnChartValueSelectedListener(this);

        barChart.getLegend().setEnabled(false);



        List<BarEntry> entries = getBarEntries(storeTransactionModelList);
        List<String> labels = getLabels(storeTransactionModelList);

        BarDataSet dataset = new BarDataSet(entries,"Sales");
        dataset.setColors(ColorTemplate.JOYFUL_COLORS);
        dataset.setValueTextSize(6);
        //dataset.setValueTypeface(tf);
        BarData data = new BarData(labels,dataset);
        data.setValueFormatter(new MyValueFormatter());

        barChart.setData(data);

        //barChart.animateX(1000);
        barChart.setDescription("Store Sales");





        barChart.invalidate();

        return barChart;
    }

    private List<BarEntry> getBarEntries(List<StoreTransactionModel> storeTransactionModelList) {
        List<BarEntry> barEntries = new ArrayList<>();

        for(int i=0;i<storeTransactionModelList.size();i++){
            barEntries.add(new BarEntry((float) storeTransactionModelList.get(i).getSales(),i));
        }

        return barEntries;

    }

    private List<String> getLabels(List<StoreTransactionModel> storeTransactionModelList) {
        List<String> labels = new ArrayList<>();

        for(int i=0;i<storeTransactionModelList.size();i++){
            labels.add(storeTransactionModelList.get(i).getStoreName());
        }

        return labels;
    }



    private List<String> getPieLabelForStore(List<StoreTransactionModel> storeTransactionModelList) {
        List<String> labels = new ArrayList<>();

        for(int i=0;i<storeTransactionModelList.size();i++){
            labels.add(storeTransactionModelList.get(i).getStoreName());

        }

        return labels;
    }

    private List<Entry> getPieEntriesForStore(List<StoreTransactionModel> storeTransactionModelList){

        List<Entry> entryList = new ArrayList<>();

        for(int i=0;i<storeTransactionModelList.size();i++){
            Entry entry = new Entry((float) storeTransactionModelList.get(i).getSales(),i);
            entryList.add(entry);

        }

        return entryList;

    }

    private List<StoreTransactionModel> getModifiedList(List<StoreTransactionModel> storeTransactionModelList){
        List<StoreTransactionModel> modifiedList = new ArrayList<>();

        for(StoreTransactionModel x : storeTransactionModelList){
            if(x.getSales()>0){
                modifiedList.add(x);
            }
        }

        return modifiedList;
    }
}
