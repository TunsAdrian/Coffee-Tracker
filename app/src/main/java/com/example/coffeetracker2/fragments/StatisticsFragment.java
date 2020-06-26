package com.example.coffeetracker2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.coffeetracker2.R;
import com.example.coffeetracker2.database.Coffee;
import com.example.coffeetracker2.utils.CoffeeRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;


public class StatisticsFragment extends Fragment {

    private static final String TAG = "StatisticsFragment";
    private CoffeeRepository coffeeRepository;
    private LineChartView lineChartView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_statistics, container, false);
        coffeeRepository = new ViewModelProvider(requireActivity()).get(CoffeeRepository.class);
        lineChartView = root.findViewById(R.id.frag_activity_level_chart);
        setStatistics(root);
        try {
            createActivityLevelChart();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return root;
    }

    // Update the fields for each attribute in the top of Statistics fragment
    private void setStatistics(View view) {

        final TextView coffeeNrToday_TW = view.findViewById(R.id.frag_coffee_today_count);
        final TextView coffeeNrWeek_TW = view.findViewById(R.id.frag_coffee_week_count);
        final TextView caffeineToday_TW = view.findViewById(R.id.frag_caffeine_today_count);
        final TextView caffeineWeek_TW = view.findViewById(R.id.frag_caffeine_week_count);

        coffeeRepository.getCoffeeNrToday().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                coffeeNrToday_TW.setText(integer.toString() + " coffee/s");

            }
        });

        coffeeRepository.getCoffeeNrThisWeek().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                coffeeNrWeek_TW.setText(integer.toString() + " coffee/s");
            }
        });

        coffeeRepository.getCaffeineToday().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                if (integer != null) {
                    caffeineToday_TW.setText(integer.toString() + " mg caffeine");
                }
            }
        });

        coffeeRepository.getCaffeineThisWeek().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                if (integer != null) {
                    caffeineWeek_TW.setText(integer.toString() + " mg caffeine");
                }
            }
        });
    }

    private void createActivityLevelChart() throws ExecutionException, InterruptedException {

        final int NR_DAYS_SEEN_ON_SCREEN = 5;
        List<AxisValue> xAxisValues = new ArrayList<>();
        List<PointValue> yAxisValues = new ArrayList<>();
        List<Coffee> coffees = coffeeRepository.getCoffeesWithProductivity();

        // Set line color Primary Color and set labels for the selected points
        Line line = new Line(yAxisValues)
                .setColor(this.getResources().getColor(R.color.colorPrimary))
                .setHasLabels(true)
                .setHasLabelsOnlyForSelected(true);

        String pattern = "MMM dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        for (int i = 0; i < coffees.size(); i++) {
            xAxisValues.add(i, new AxisValue(i).setLabel(simpleDateFormat.format(coffees.get(i).getDate())));
            yAxisValues.add(new PointValue(i, coffees.get(i).getProductivityRating()));
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
        viewport.left = xAxisValues.size() - NR_DAYS_SEEN_ON_SCREEN;
        viewport.right = xAxisValues.size();

        // Update the lineChartView
        lineChartView.setCurrentViewport(viewport);
        lineChartView.setViewportCalculationEnabled(false);
    }
}

