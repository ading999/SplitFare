package cn.youthocracy.splitfare;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justice on 3/13/15.
 */
public class Pair implements Comparable<Pair> {
    public String name="";
    public double value=0;
    private List<String> transaction = new ArrayList<String>();

    public Pair(String name, double value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public int compareTo(Pair other) {
        //multiplied to -1 as the author need descending sort order
        if(this.value<other.value){
            return -1;
        }
        else if(this.value==other.value)
        {
            return 0;
        }
        else{
            return 1;
        }
    }

    private double roundOff(double a){
        return Math.round(a * 100.0) / 100.0;
    }

    public List<String> getTransaction(){
        return transaction;
    }

    public void addTransaction(String str){
            transaction.add(str);

    }
}
