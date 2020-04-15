package com.questnr.services;

import com.questnr.util.LinearRegression;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinearRegressionService {

    public LinearRegression createLinearRegressionFromCollection(List<Double> xAxisList, List<Double> yAxisList) {
        double[] xAxis = new double[xAxisList.size()];
        double[] yAxis = new double[yAxisList.size()];
        for (int n = 0; n < xAxisList.size(); n++) {
            xAxis[n] = xAxisList.get(n);
        }
        for (int n = 0; n < yAxisList.size(); n++) {
            yAxis[n] = yAxisList.get(n);
        }
        return new LinearRegression(xAxis, yAxis);
    }

    public Double getSlopFromDataOverTime(List<Double> xAxisList, List<Double> yAxisList){
        return this.createLinearRegressionFromCollection(xAxisList, yAxisList).slope();
    }
}
