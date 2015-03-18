package cn.youthocracy.splitfare;

/**
 * Created by Justice on 3/14/15.
 */
public class Bill {
    private String BillDescription;
    private double amount;
    private int PayerID;
    private int BillID;
    private int CollectionID;

    public String getBillDescription(){
        return BillDescription;
    }

    public void setBillDescription(String d){
        BillDescription = d;
    }

    public double getAmount(){
        return amount;
    }

    public void setAmount(double a){
        amount = a;
    }

    public int getPayerID(){
        return PayerID;
    }

    public void setPayerID(int pi){
        PayerID = pi;
    }

    public int getBillID(){
        return BillID;
    }

    public void setBillID(int ID){
        BillID = ID;
    }

    public void setCollectionID(int ID){
        CollectionID = ID;
    }

    public int getCollectionID(){
        return CollectionID;
    }
}
