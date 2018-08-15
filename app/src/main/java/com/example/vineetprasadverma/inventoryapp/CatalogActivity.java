package com.example.vineetprasadverma.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.vineetprasadverma.inventoryapp.data.ProductContract.ProductEntry;
import com.example.vineetprasadverma.inventoryapp.data.ProductDbHelper;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ProductDbHelper mDbHelper;
    ProductCursorAdapter mCursorAdapter;
    private static final int PET_LOADER = 0;

    public static final String LOG_TAG = CatalogActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog_activity);

        mDbHelper = new ProductDbHelper(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });


        ListView listView = findViewById(R.id.list);
        mCursorAdapter = new ProductCursorAdapter(this, null);
        listView.setAdapter(mCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        //Kick off the loader
        getLoaderManager().initLoader(PET_LOADER, null, this);
    }

    private void insertData() {
        // Gets the data repository in write mode.
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys.
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Wings of fire");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 50.5);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 53);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, "Vineet");
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, "8985796745");

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(ProductEntry.TABLE_NAME, null, values);
        Log.i(LOG_TAG, String.valueOf(newRowId));
    }

    private void readData() {

        // Create and/or open a database to read from it.
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER
        };

        Cursor cursor = db.query(
                ProductEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null              // The sort order
        );

        try {
            // Figure out the index of each column
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
            int supplierPhoneNumberColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                String currentProductName = cursor.getString(nameColumnIndex);
                float currentProductPrice = cursor.getFloat(priceColumnIndex);
                int currentProductQuantity = cursor.getInt(quantityColumnIndex);
                String currentProductSupplierName = cursor.getString(supplierNameColumnIndex);
                String currentProductSupplierPhoneNumber = cursor.getString(supplierPhoneNumberColumnIndex);
                Log.i(LOG_TAG, currentProductName
                        + " - " + String.valueOf(currentProductPrice)
                        + " - " + String.valueOf(currentProductQuantity)
                        + " - " + currentProductSupplierName
                        + " - " + currentProductSupplierPhoneNumber);
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_book:
                return true;

            case R.id.action_delete_all_books:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.

        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY
        };

        return new CursorLoader(this,
                ProductEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Swap the new cursor in. (The framework will take care of closing the
        // old cursor once we return.)
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed. We need to make sure we are no
        // longer using it.
        mCursorAdapter.swapCursor(null);
    }
}
