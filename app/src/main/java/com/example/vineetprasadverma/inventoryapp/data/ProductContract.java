package com.example.vineetprasadverma.inventoryapp.data;

import android.provider.BaseColumns;

public class ProductContract {

    /**
     * Here the product is the Book.
     * Inner class that defines constant values for the Books database table.
     * Each entry in the table represents a single Book.
     */
    public static class ProductEntry implements BaseColumns{

        public static final String TABLE_NAME = "books";
        public static final String COLUMN_PRODUCT_NAME = "product_name";
        public static final String COLUMN_PRODUCT_PRICE = "price";
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
        public static final String COLUMN_PRODUCT_SUPPLIER_NAME = "supplier_name";
        public static final String COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER = "supplier_phone_number";
    }
}
