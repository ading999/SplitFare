package cn.youthocracy.splitfare;

import android.util.Log;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
    private List<String> TransactionSet;

    public SplitFare(String[] names, double[] eachPaid) {
        NumOfPpl = names.length;
        Sum = getSum(eachPaid);

        Average = Sum / NumOfPpl;
        Difference = getDifference(eachPaid);
        pairs = getPairs(names, Difference);
        Arrays.sort(pairs);


           resultSet = Split(pairs);
        TransactionSet = getTranscations(resultSet);
    }


    private Pair[] Split(Pair[] arr) {

        if ((Math.abs(arr[0].value) < 0.01)&&(Math.abs(arr[arr.length-1].value)<0.01)) {
            return arr;
        } else {
            //nested function
            double oldMin = arr[0].value;
            Log.d("OldMin",String.valueOf(oldMin));
            double oldMax = arr[NumOfPpl-1].value;
            arr[0].value = ((oldMax+oldMin)>0.01)?0:(oldMax+oldMin);
            double payment = roundOff(((oldMax+oldMin)>0.01)?Math.abs(oldMin):oldMax);
            arr[0].addTransaction(arr[0].name+" pays "+arr[NumOfPpl-1].name+" $"+String.valueOf(payment));
            Log.d("transaction",arr[0].name+" pays "+arr[NumOfPpl-1].name+" $"+String.valueOf(payment));
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
        double[] d = new double[arr.length];
        for (int i = 0; i < NumOfPpl; i++) {
            d[i] = arr[i] - Average;
            Log.d("average",String.valueOf(Average));
            Log.d("difference", String.valueOf(d[i]));
        }
        return d;
    }

    private Pair[] getPairs(String[] names, double[] difference) {

        Pair[] par  = new Pair[difference.length];
        Log.d("par length",String.valueOf(par.length));
        for (int i = 0; i < NumOfPpl; i++) {
            Log.d("name pairs", names[i]);
            par[i] = new Pair(names[i],difference[i]);
        }
        return par;
    }

    private double roundOff(double a) {
        return Math.round(a * 100.0) / 100.0;
    }

    private List<String> getTranscations(Pair[] rSet) {
        List<String> subset = new ArrayList<>();
        List<String> tset = new ArrayList<String>();
        for (int i = 0; i < NumOfPpl; i++) {
            subset = rSet[i].getTransaction();
                for (int n = 0; n < subset.size(); n++) {
                    tset.add(subset.get(n));
                }

        }
        return tset;
    }

    public String getTrans(){
        String str="";
        for(int i=0;i<TransactionSet.size();i++){
            str +=(TransactionSet.get(i)+ "\n");
        }
        return str;
    }

}
