package cn.youthocracy.splitfare;
/**
 * Created by Justice on 3/13/15.
 */
public class Pair implements Comparable<Pair> {
    public String name;
    public double value;
    private String[] transaction = null;

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

    public String[] getTransaction(){
        return transaction;
    }

    public void addTransaction(String str){
        if(transaction==null){
            transaction[0]=str;
        }
        else {
            transaction[transaction.length] = str;
        }
    }
}
