package cn.youthocracy.splitfare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Justice on 3/14/15.
 */
public class BillDataSource {
    private SQLiteDatabase db;
    private MySQLiteHelper dbHelper;
    private String collectionTableName = "Collections";
    private String billTableName = "Bills";
    private String personTableName = "Persons";
    public BillDataSource(Context context){
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException{
        db = dbHelper.getWritableDatabase();
        if(db.isOpen())
            Log.d("dbopen","db is open");
            Log.d("version",String.valueOf(db.getVersion()));
    }

    public void close(){
        dbHelper.close();
    }



    public int addBill(Bill b){
        ContentValues values = new ContentValues();
        values.put("Description",b.getBillDescription());
        values.put("Amount",b.getAmount());
        values.put("PayerID",b.getPayerID());
        values.put("CollectionID",b.getCollectionID());
        int id = (int) db.insert(billTableName,null,values);
        calculateCollectionTotal(b.getCollectionID());
        return id;
    }

    public Bill getBill(int id){
        String[] col = {"id","Description","Amount","PayerID","CollectionID"};
        String[] arg = {""};
        arg[0] = String.valueOf(id);
        Log.d("getAllBills collectionID",arg[0]);
        Cursor cursor = db.query(billTableName,col,"id = ?",arg,null,null,null);
        cursor.moveToNext();
        Bill tmp = new Bill();
        Log.d("BillDataSource",String.valueOf(tmp.getCollectionID()));
        return tmp;
    }

    public void removeBill(int BillID,int CollectionID){
        String[] arg = {String.valueOf(BillID)};
        db.delete(billTableName,"id = ?",arg);
        //String removeQuery = "DELETE * FROM "+billTableName+" WHERE id = "+ BillID;
       calculateCollectionTotal(CollectionID);
     //   db.execSQL(removeQuery);

    }

    public List<Bill> getAllBills(int CollectionID){
        String[] col = {"id","Description","Amount","PayerID","CollectionID"};
        List<Bill> b = new ArrayList<>();
        String[] arg = {""};
        arg[0] = String.valueOf(CollectionID);
        Log.d("getAllBills collectionID",arg[0]);
        Cursor cursor = db.query(billTableName,col,"CollectionID = ?",arg,null,null,"id DESC");
    //    Log.d("cursor",String.valueOf(cursor.getCount()));
        if(cursor.getCount()==0){
            return null;
        }
        else{
            while (cursor.moveToNext() ) {
            b.add(CursorToBill(cursor));
            }
                return b;}
    }

    private Bill CursorToBill(Cursor cursor){
        Bill tmp = new Bill();
        tmp.setBillID(cursor.getInt(0));
//            Log.d("BilldataSource",tmp.getBillDescription());
        tmp.setBillDescription(cursor.getString(1));
        tmp.setAmount(cursor.getDouble(2));
        tmp.setPayerID(cursor.getInt(3));
        tmp.setCollectionID(cursor.getInt(4));

        return tmp;
    }


    //Collection
    public int addCollection(FareCollection c){
        ContentValues values = new ContentValues();
        values.put("CollectionTotal",0);
        values.put("CollectionName", c.getCollectionName());
        int id = (int) db.insert(collectionTableName,null,values);
        return id;
    }

    private void updateCollectionTotal(double total, int CollectionID){
        ContentValues values = new ContentValues();
        values.put("CollectionTotal",total);
        String[] Args = {""};
        Args[0] = String.valueOf(CollectionID);
        db.update(collectionTableName,values,"id = ?",Args);
    }

    private String calculateCollectionTotal(int CollectionID){
        String[] col = {"Amount"};
        String[] arg = {String.valueOf(CollectionID)};
        Log.d("getAllBills collectionID",arg[0]);
        Cursor cursor = db.query(billTableName,col,"CollectionID = ?",arg,null,null,"id DESC");
        double total=0;
        //    Log.d("cursor",String.valueOf(cursor.getCount()));
        if(cursor.getCount()==0){
            return null;
        }
        else{
            while (cursor.moveToNext() ) {
            total+=cursor.getDouble(0);}
            updateCollectionTotal(total,CollectionID);
            return String.valueOf(total);
        }
    }

    public String getCollectionTotal(int CollectionID){
        String[] col = {"CollectionTotal"};
        String[] arg = {""};
        arg[0] = String.valueOf(CollectionID);
        Log.d("getCollection collectionID",arg[0]);
        Cursor cursor = db.query(collectionTableName,col,"id = ?",arg,null,null,null);
        cursor.moveToNext();
        return cursor.getString(0);
    }

    public FareCollection getCollection(int id){
        String[] col = {"id","CollectionName","CollectionTotal"};
        String[] arg = {""};
        arg[0] = String.valueOf(id);
        Log.d("getCollection collectionID",arg[0]);
        Cursor cursor = db.query(collectionTableName,col,"id = ?",arg,null,null,null);
        cursor.moveToNext();
        FareCollection tmp = new FareCollection();
        tmp.setCollectionID(cursor.getInt(0));
        tmp.setCollectionName(cursor.getString(1));
        tmp.setCollectionTotal(Double.parseDouble(cursor.getString(2)));
        return tmp;
    }

    public void removeCollection(int CollectionID){
        String[] arg = {String.valueOf(CollectionID)};
        String[] billcol = {"id"};
        String[] arg2 = {""};
        Cursor cursor = db.query(billTableName,billcol,"CollectionID = ?",arg,null,null,null);
        while(cursor.moveToNext()){
            arg2[0] = cursor.getString(0);
            db.delete(billTableName,"id = ?",arg2);
        }
        cursor = db.query(personTableName,billcol,"CollectionID = ?",arg,null,null,null);
        while(cursor.moveToNext()){
            arg2[0] = cursor.getString(0);
            db.delete(personTableName,"id = ?",arg2);
        }
        db.delete(collectionTableName,"id = ?",arg);
    }

    public List<FareCollection> getAllCollections(){
        String[] col = {"id","CollectionName","CollectionTotal"};
        List<FareCollection> c = new ArrayList<>();
        Cursor cursor = db.query(collectionTableName,col,null,null,null,null,"id DESC");
        //    Log.d("cursor",String.valueOf(cursor.getCount()));
        if(cursor.getCount()==0){
            return null;
        }
        else{
            while (cursor.moveToNext() ) {
                c.add(CursorToCollection(cursor));
            }
            return c;}
    }

    private FareCollection CursorToCollection(Cursor cursor){
        FareCollection tmp = new FareCollection();
        tmp.setCollectionID(cursor.getInt(0));
//            Log.d("BilldataSource",tmp.getBillDescription());
        tmp.setCollectionName(cursor.getString(1));
        tmp.setCollectionTotal(cursor.getDouble(2));
        return tmp;
    }


    //person
    public void addPerson(Person p){
        ContentValues values = new ContentValues();
        values.put("PersonName",p.getPersonName());
        values.put("CollectionID", p.getCollectionID());
        Log.d("PersonName",p.getPersonName());
        Log.d("CollectionID", String.valueOf(p.getCollectionID()));
        db.insert(personTableName,null,values);
    }

    public void removePerson(int PersonID){
        String[] arg = {String.valueOf(PersonID)};
        String[] billcol = {"id"};
        String[] arg2 = {""};
        Cursor cursor = db.query(billTableName,billcol,"PayerID = ?",arg,null,null,null);
        while(cursor.moveToNext()){
            arg2[0] = cursor.getString(0);
            db.delete(billTableName,"id = ?",arg2);
        }
        db.delete(personTableName,"id = ?",arg);
    }

    public List<Person> getAllPersons(int CollectionID){
        String[] col = {"id","PersonName","CollectionID"};
        List<Person> p = new ArrayList<Person>();
        String[] Args = {""};
        Args[0] = String.valueOf(CollectionID);
        Cursor cursor = db.query(personTableName,col,"CollectionID = ?",Args,null,null,"id DESC");
        //    Log.d("cursor",String.valueOf(cursor.getCount()));
        if(cursor.getCount()==0){
            return null;
        }
        else{
            while (cursor.moveToNext() ) {
                p.add(CursorToPerson(cursor));
            }
            return p;}
    }

    private Person CursorToPerson(Cursor cursor){
        Person tmp = new Person();
        tmp.setPersonID(cursor.getInt(0));
//            Log.d("BilldataSource",tmp.getBillDescription());
        tmp.setPersonName(cursor.getString(1));
        tmp.setCollectionID(cursor.getInt(2));
        return tmp;
    }

 public String[] getPersonNames(int CollectionID){
     String[] col = {"PersonName"};
     String[] Args = {""};
     Args[0] = String.valueOf(CollectionID);
     Cursor cursor = db.query(personTableName,col,"CollectionID = ?",Args,null,null,"id DESC");
         Log.d("cursor",String.valueOf(cursor.getCount()));
     String[] names = new String[cursor.getCount()];
     if(cursor.getCount()==0){
         return null;
     }
     else{
        for(int i=0;i<cursor.getCount();i++){
            cursor.moveToNext();
            names[i]=cursor.getString(0);
            Log.d("name",names[i]);
        }
         return names;}
 }

    private int[] getPersonIds(int CollectionID){
        String[] col = {"id"};
        String[] Args = {""};
        Args[0] = String.valueOf(CollectionID);
        Cursor cursor = db.query(personTableName,col,"CollectionID = ?",Args,null,null,"id DESC");
        int[] Ids = new int[cursor.getCount()];
        //    Log.d("cursor",String.valueOf(cursor.getCount()));
        if(cursor.getCount()==0){
            return null;
        }
        else{
            for(int i=0;i<cursor.getCount();i++){
                cursor.moveToNext();
                Ids[i]=cursor.getInt(0);
                Log.d("name",String.valueOf(Ids[i]));
            }
            return Ids;}
    }

public double[] getEachPaid(int CollectionID){
    int[] Ids = getPersonIds(CollectionID);
    double[] eachPaid = new double[Ids.length];
    String[] Args = {""};
    String[] col = {"Amount"};
    for(int i=0;i<Ids.length;i++) {
        Args[0] = String.valueOf(i);
        Cursor cursor = db.query(billTableName, col, "PayerID = ?", Args, null, null, "id DESC");
        double sum = 0;
        {
            for (int b = 0; b < cursor.getCount(); b++) {
                cursor.moveToNext();
                sum += cursor.getDouble(0);
            }
            eachPaid[i] = sum;
        }
    }
    return eachPaid;
}

}
