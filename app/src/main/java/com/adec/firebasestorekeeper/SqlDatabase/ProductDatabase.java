package com.adec.firebasestorekeeper.SqlDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sohel on 1/16/2017.
 */

public class ProductDatabase {

    private static final String COL_ID="_id";
    private static final String COL_NAME="product_name";

    private static final String[] ALL_KEYS={COL_ID,COL_NAME};

    // Db Name and Table Name
    private static final String DB_NAME="productDb";
    private static final String TABLE_NAME="productTable";

    // Db Version
    private static final int DATABASE_VERSION=4;

    private static final String DATABASE_CREATE_SQL="create table "+TABLE_NAME
            +"("+COL_ID+" integer primary key autoincrement, "
            +COL_NAME+" string "
            +");";

    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;


    public ProductDatabase(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public ProductDatabase open(){
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    private long insertRow(String name){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME,name);
        return db.insert(TABLE_NAME,null,contentValues);
    }

    public long insertProduct(String name){
        return insertRow(name);
    }

    public List<String> getAllProducts(){
        List<String> allProducts = new ArrayList<>();

        Cursor cursor = getAllRows();

        if(cursor==null){
            Log.d("TTTTT","NULL");
        }



        if(cursor.moveToFirst()){
            do{

                String product = cursor.getString(1);
                allProducts.add(product);

            }while (cursor.moveToNext());
        }

        return allProducts;


    }

    private Cursor getAllRows(){
        String where = null;
        Cursor c=db.query(true,TABLE_NAME,ALL_KEYS,where,null,null,null,null,null);


        if(c!= null){
            c.moveToFirst();
        }
        return c;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_SQL);

            Log.d("TTTT","YES");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            // Destroy old database:
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

            Log.d("TTTT","YES");

            // Recreate new database:
            onCreate(db);

        }
    }
}
