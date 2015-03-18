package cn.youthocracy.splitfare;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by Justice on 3/14/15.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME="SplitFare.db";
    private static int DATABASE_VERSION = 1;
       public MySQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }
    @Override
    public void onCreate(SQLiteDatabase db){
  // Log.d("db onCreate", TABLE_CREATE);
        String TABLE1_CREATE = "CREATE TABLE IF NOT EXISTS Collections(id INTEGER PRIMARY KEY, CollectionName TEXT, CollectionTotal TEXT)";
  //  Log.d("table create 1",TABLE1_CREATE);
        String TABLE2_CREATE = "CREATE TABLE IF NOT EXISTS Bills(id INTEGER PRIMARY KEY, Description TEXT, Amount TEXT, PayerID INTEGER, CollectionID INTEGER)";
      //  Log.d("table create 2",TABLE2_CREATE);

        String TABLE3_CREATE = "CREATE TABLE IF NOT EXISTS Persons(id INTEGER PRIMARY KEY, PersonName TEXT, CollectionID INTEGER)";
     //   Log.d("table create 3",TABLE3_CREATE);

        db.execSQL(TABLE1_CREATE);
        db.execSQL(TABLE2_CREATE);
        db.execSQL(TABLE3_CREATE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
   // db.execSQL("DROP TABLE IF EXISTS "+TABLE_COLLECTIONS);
    }
}
