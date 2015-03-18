package cn.youthocracy.splitfare;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Justice on 3/13/15.
 */
public class SplitFare {
    private int NumOfPpl;
    private double Sum;
    private double Average;
    private double[] Difference;
    private Pair[] pairs;
    private double err = 0;
    private Pair[] resultSet;
    private String[] TransactionSet;

    public SplitFare(String[] names, double[] eachPaid) {
        NumOfPpl = names.length;
        Sum = getSum(eachPaid);
        Average = Sum / NumOfPpl;
        Difference = getDifference(eachPaid);
        pairs = getPairs(names, eachPaid);
        Arrays.sort(pairs);
        resultSet = Split(pairs);
        TransactionSet = getTranscations(resultSet);
    }


    private Pair[] Split(Pair[] arr) {
        for (int i = 0; i < NumOfPpl; i++) {
            err += arr[i].value;
        }
        if (Math.abs(err - 0) < 0.00000001) {
            return arr;
        } else {
            //nested function
            double oldMin = arr[0].value;
            double oldMax = arr[NumOfPpl-1].value;
            arr[0].value = ((oldMax+oldMin)>0.01)?0:(oldMax+oldMin);
            double payment = roundOff(((oldMax+oldMin)>0.01)?Math.abs(oldMin):oldMax);
            arr[0].addTransaction(arr[0].name+" pays "+arr[NumOfPpl-1].name+" $"+payment);
            arr[NumOfPpl-1].value = ((oldMax+oldMin)>0.01)?(oldMax+oldMin):0;
            Arrays.sort(arr);
            return Split(arr);
        }
    }

    private double getSum(double[] arr) {
        double s = 0.00;
        for (int i = 0; i < NumOfPpl; i++) {
            s += arr[i];
        }
        return s;
    }

    private double[] getDifference(double[] arr) {
        double[] d = {};
        for (int i = 0; i < NumOfPpl; i++) {
            d[i] = arr[i] - Average;
        }
        return d;
    }

    private Pair[] getPairs(String[] names, double[] difference) {
        for (int i = 0; i < NumOfPpl; i++) {
            pairs[i].name = names[i];
            pairs[i].value = difference[i];
        }
        return pairs;
    }

    private double roundOff(double a) {
        return Math.round(a * 100.0) / 100.0;
    }

    private String[] getTranscations(Pair[] rSet) {
        String[] subset = null;
        String[] tset = null;
        int Counter = 0;
        for (int i = 0; i < NumOfPpl; i++) {
            subset = rSet[i].getTransaction();
            if (subset != null) {
                for (int n = 0; n < subset.length; n++) {
                    tset[Counter] = subset[n];
                    Counter += 1;
                }
            }
        }
        return tset;
    }
}
