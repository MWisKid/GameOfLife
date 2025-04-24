package SwingGUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class exponentialFit {
    
    public double[] calculate(ArrayList<Double> xData, ArrayList<Double> yData) {
        float sumX = 0.0f;
        float sumYln = 0.0f;
        float sumX2 = 0.0f;
        float sumXYln = 0.0f;
        float sumY = 0.0f;
        int n = xData.size();

        // Calculate necessary sums
        for (int i = 0; i < n; i++) {
            float x = xData.get(i).floatValue();
            float y = yData.get(i).floatValue();

            if (y > 0) {
                float lnY = (float) Math.log(y);
                sumX += x;
                sumXYln += (x * lnY);
                sumX2 += (x * x);
                sumYln += lnY;
                sumY += y;
            } else {
                throw new IllegalArgumentException("Invalid y value for logarithm: " + y);
            }
        }

        // Calculate coefficients A and B
        float denominator1 = (n * sumX2) - (sumX * sumX);
        float a = ((sumYln * sumX2) - (sumX * sumXYln)) / denominator1;
        float b = ((n * sumXYln) - (sumX * sumYln)) / denominator1;

        double A = Math.exp(a);
        double B = b;

        return new double[]{A, B};
    }
}
