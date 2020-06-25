package com.example.coffeetracker2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.coffeetracker2.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;


public class StatisticsFragment extends Fragment {

    private static final String TAG = "StatisticsFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_statistics, container, false);
        createActivityLevelChart(root);
        setStatistics(root);

        return root;
    }

    // Update the fields for each attribute in the top of Statistics fragment
    private void setStatistics(View view) {

//        MainActivity mainActivity = (MainActivity) getActivity();
//
//        // Set coffee numbers in real time
//        final TextView coffeeNrToday_TW = view.findViewById(R.id.frag_coffee_today_count);
//
//        LiveData<Integer> coffeeNrToday = mainActivity.getCOFFEE_TODAY();
//        coffeeNrToday.observe(getViewLifecycleOwner(), new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//                coffeeNrToday_TW.setText(integer.toString() + " coffee/s");
//            }
//        });
//
//        final TextView coffeeNrWeek_TW = view.findViewById(R.id.frag_coffee_week_count);
//
//        LiveData<Integer> coffeeNrWeek = mainActivity.getCOFFEE_WEEK();
//        coffeeNrWeek.observe(getViewLifecycleOwner(), new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//                coffeeNrWeek_TW.setText(integer.toString() + " coffee/s");
//            }
//        });
//
//        // Set caffeine levels in realtime
//        final TextView caffeineToday_TW = view.findViewById(R.id.frag_caffeine_today_count);
//
//        LiveData<Integer> caffeineToday = mainActivity.getCAFFEINE_TODAY();
//        caffeineToday.observe(getViewLifecycleOwner(), new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//                caffeineToday_TW.setText(integer.toString() + " mg caffeine");
//            }
//        });
//
//        final TextView caffeineWeek_TW = view.findViewById(R.id.frag_caffeine_week_count);
//
//        LiveData<Integer> caffeineWeek = mainActivity.getCAFFEINE_WEEK();
//        caffeineWeek.observe(getViewLifecycleOwner(), new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//                caffeineWeek_TW.setText(integer.toString() + " mg caffeine");
//            }
//        });
    }

    private void createActivityLevelChart(View v) {

        final int NR_DAYS_SEEN_ON_SCREEN = 5;

        //TODO Must be connected with the DB. yAxisData should be the productivity grades, coffeeConsumed the number of coffee from that day
        String[] xAxisData = {"May 4", "May 5", "May 6", "May 7", "May 8", "May 9", "May 10", "May 11", "May 12", "May 13", "May 14", "May 15",
                "May 16", "MAy 17", "May 18", "May 19", "May 20", "May 21", "May 22", "May 23", "May 24", "May 25", "May 26",
                "May 27", "May 28", "May 30", "May 31", "Jun 1", "Jun 2", "Jun 3"};
        int[] yAxisData = {1, 2, 3, 5, 4, 2, 5, 1, 2, 3, 5, 4, 2, 5, 5, 4, 2, 3, 5, 4, 2, 1, 2, 2, 5, 2, 1, 5, 3, 4};
        int[] coffeeConsumed = {2, 3, 1, 5, 4, 2, 4, 2, 3, 1, 5, 4, 2, 4, 6, 1, 6, 5, 3, 4, 4, 6, 5, 3, 4, 3, 5, 3, 2, 5};

        LineChartView lineChartView = v.findViewById(R.id.frag_activity_level_chart);

        List<AxisValue> xAxisValues = new ArrayList<>();
        List<PointValue> yAxisValues = new ArrayList<>();

        // Set line color with our Secondary Color and labels for the selected points
        Line line = new Line(yAxisValues)
                .setColor(this.getResources().getColor(R.color.colorPrimary))
                .setHasLabels(true)
                .setHasLabelsOnlyForSelected(true);

        // Populate xAxisValues array
        for (int i = 0; i < xAxisData.length; i++) {
            xAxisValues.add(i, new AxisValue(i).setLabel(xAxisData[i]));
        }

        // Populate yAxisValues array and set each point's labels
        for (int i = 0; i < yAxisData.length; i++) {
            yAxisValues.add(new PointValue(i, yAxisData[i]).setLabel(String.valueOf(coffeeConsumed[i])));
        }

        // This list holds the line of the graph chart
        List<Line> lines = new ArrayList<>();
        lines.add(line);

        // The graph line is then added to the overall data chart
        LineChartData data = new LineChartData();
        data.setLines(lines);

        // Populate xAxis and link it to data's bottom x Axis
        Axis xAxis = new Axis();
        xAxis.setValues(xAxisValues);
        xAxis.setTextSize(16);
        xAxis.setTextColor(this.getResources().getColor(R.color.colorPrimary));
        data.setAxisXBottom(xAxis);

        // This section is used to put only whole numbers from 1 to 5(productivity levels) as labels on the y Axis,
        //  because by default it autoGenerates several floats between the values, and this way setAutoGenerated() is set to false
        List<AxisValue> yAxisValuesWithLabels = new ArrayList<>();

        for (int i = 1; i <= 5; i++)
            yAxisValuesWithLabels.add(new AxisValue(i).setLabel(i + ""));

        // Populate yAxis and link it to data's left y Axis
        Axis yAxis = new Axis(yAxisValuesWithLabels);
        yAxis.setName("Productivity level");
        yAxis.setTextColor(this.getResources().getColor(R.color.colorPrimary));
        yAxis.setTextSize(16);
        yAxis.setHasLines(true);
        data.setAxisYLeft(yAxis);

        // Add the line chat data to the view and take a viewport reference
        lineChartView.setLineChartData(data);
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());

        // Top is set with that value to prevent it from showing in the top of the labels. Without it, the top would be 5 and it wouldn't fit properly in the screen
        viewport.top = (float) 5.1;
        viewport.bottom = 1;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setZoomEnabled(false);

        // Set left and right as length of dates and length minus a number of days
        viewport.left = xAxisData.length - NR_DAYS_SEEN_ON_SCREEN;
        viewport.right = xAxisData.length;

        // Update the lineChartView
        lineChartView.setCurrentViewport(viewport);
        lineChartView.setViewportCalculationEnabled(false);
    }
}

