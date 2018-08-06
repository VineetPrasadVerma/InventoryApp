package com.example.vineetprasadverma.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.vineetprasadverma.inventoryapp.data.ProductContract.ProductEntry;

public class ProductDbHelper extends SQLiteOpenHelper {

    //Name of the database file
    private static final String DATABASE_NAME = "products.db";

    // If we change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    private static final String LOG_TAG = ProductDbHelper.class.getSimpleName();

    /**
     * Constructs a new instance of ProductDbHelper.
     *
     * @param context of the app
     */
    public ProductDbHelper(Context context){
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    //This is called when the database is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + ProductContract.ProductEntry.TABLE_NAME + "("
                + ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + ProductEntry.COLUMN_PRODUCT_PRICE + " REAL NOT NULL, "
                + ProductEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL, "
                + ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME + " TEXT NOT NULL, "
                + ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER + " TEXT NOT NULL );";
        Log.i(LOG_TAG, SQL_CREATE_BOOKS_TABLE);

        //Execute the SQL statement.
        db.execSQL(SQL_CREATE_BOOKS_TABLE);
    }

    //This is called when the database need to be upgraded.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ProductEntry.TABLE_NAME;

        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}
