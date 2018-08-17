package com.example.vineetprasadverma.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vineetprasadverma.inventoryapp.data.ProductContract.ProductEntry;

public class ProductDetails extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private Uri mCurrentProductUri;
    private static final int PRODUCT_LOADER = 0;
    private TextView mBookTextView;
    private TextView mPriceTextView;
    private TextView mSupplierNameTextView;
    private TextView mSupplierPhoneNoTextView;
    private TextView mQuantityTextView;
    private Button mOrderButton;
    private Button mIncrementButton;
    private Button mDecrementButton;
    private EditText mEnterQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        mBookTextView = findViewById(R.id.details_book_name);
        mPriceTextView = findViewById(R.id.details_book_price);
        mSupplierNameTextView = findViewById(R.id.details_supplier_name);
        mSupplierPhoneNoTextView = findViewById(R.id.details_supplier_phone_no);
        mQuantityTextView = findViewById(R.id.details_quantity);
        mOrderButton = findViewById(R.id.order_button);
        mIncrementButton = findViewById(R.id.increment_button);
        mDecrementButton = findViewById(R.id.decrement_button);
        mEnterQuantity = findViewById(R.id.detail_enter_quantity);

        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(ProductDetails.this, EditorActivity.class);
                intent.setData(mCurrentProductUri);
                startActivity(intent);
                finish();
                return true;

            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;

            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(ProductDetails.this);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteBook();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the book in the database.
     */
    private void deleteBook() {
        // Only perform the delete if this is an existing product.
        if (mCurrentProductUri != null) {
            // Call the ContentResolver to delete the product at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);
            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_procduct_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        // Close the activity
        finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Since the editor shows all book attributes, define a projection that contains
        // all columns from the pet table
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER
        };
        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentProductUri,         // Query the content URI for the current product
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Find the columns of book attributes that we're interested in
        int bookNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int supplierNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
        int supplierPhoneNoColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER);

        if (cursor.moveToNext()) {
            // Extract out the value from the Cursor for the given column index
            String bookName = cursor.getString(bookNameColumnIndex);
            Float price = cursor.getFloat(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String supplierName = cursor.getString(supplierNameColumnIndex);
            final String phoneNo = cursor.getString(supplierPhoneNoColumnIndex);

            // Update the views on the screen with the values from the database
            mBookTextView.setText(bookName);
            mPriceTextView.setText("₹ " + String.valueOf(price));
            mQuantityTextView.setText(String.valueOf(quantity));
            mSupplierNameTextView.setText(supplierName);
            mSupplierPhoneNoTextView.setText(phoneNo);

            String quantityString = mEnterQuantity.getText().toString().trim();
            if (!TextUtils.isEmpty(quantityString)) {
                int newQuantity = Integer.valueOf(quantityString);
                ContentValues values = new ContentValues();
                values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, newQuantity);
                getContentResolver().update(mCurrentProductUri, values, null, null);
                mQuantityTextView.setText(String.valueOf(newQuantity));
            }

            mIncrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String quantity = mQuantityTextView.getText().toString();
                    if (Integer.valueOf(quantity) >= 0) {
                        int newQuantity = Integer.valueOf(quantity) + 1;
                        ContentValues values = new ContentValues();
                        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, newQuantity);
                        getContentResolver().update(mCurrentProductUri, values, null, null);
                        mQuantityTextView.setText(String.valueOf(newQuantity));
                    }
                }
            });

            mDecrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String quantity = mQuantityTextView.getText().toString();
                    if (Integer.valueOf(quantity) > 0) {
                        int newQuantity = Integer.valueOf(quantity) - 1;
                        ContentValues values = new ContentValues();
                        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, newQuantity);
                        getContentResolver().update(mCurrentProductUri, values, null, null);
                        mQuantityTextView.setText(String.valueOf(newQuantity));
                    } else {
                        Toast.makeText(ProductDetails.this, R.string.book_cant_be_neative, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            mOrderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = "tel:" + phoneNo;
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                }
            });

        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mBookTextView.setText("");
        mPriceTextView.setText("");
        mQuantityTextView.setText("");
        mSupplierNameTextView.setText("");
        mSupplierPhoneNoTextView.setText("");
    }
}
