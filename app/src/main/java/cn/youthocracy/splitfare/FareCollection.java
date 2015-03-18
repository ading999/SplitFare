package cn.youthocracy.splitfare;

/**
 * Created by Justice on 3/15/15.
 */
public class FareCollection {
    private int CollectionID;
    private String CollectionName;
    private double CollectionTotal;
    public void setCollectionID(int ID){
        CollectionID = ID;
    }

    public int getCollectionID(){
        return CollectionID;
    }

    public String getCollectionName(){
        return CollectionName;
    }

    public void setCollectionName(String name){
        CollectionName = name;
    }

    public double getCollectionTotal(){
        return CollectionTotal;
    }

    public void setCollectionTotal(double total){
        CollectionTotal = total;
    }
}
