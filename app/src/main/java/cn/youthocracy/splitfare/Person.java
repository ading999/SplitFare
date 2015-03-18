package cn.youthocracy.splitfare;

/**
 * Created by Justice on 3/15/15.
 */
public class Person {
private int PersonID;
private String PersonName;
private int CollectionID;
    public void setPersonID(int ID){
        PersonID = ID;
    }
    public void setPersonName(String Name){
        PersonName = Name;
    }
    public void setCollectionID(int ID){
        CollectionID = ID;
    }

    public int getPersonID(){
        return PersonID;
    }

    public String getPersonName(){
        return PersonName;
    }

    public int getCollectionID(){
        return CollectionID;
    }
}
