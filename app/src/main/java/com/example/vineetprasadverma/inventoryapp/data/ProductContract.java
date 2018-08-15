package com.example.vineetprasadverma.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ProductContract {

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.vineetprasadverma.inventoryapp";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.vineetprasadverma.inventoryapp/books/ is a valid path for
     * looking at book data. com.example.vineetprasadverma.inventoryapp/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_BOOKS = "books";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private ProductContract(){
    }
    /**
     * Here the product is the Book.
     * Inner class that defines constant values for the Books database table.
     * Each entry in the table represents a single Book.
     */
    public static class ProductEntry implements BaseColumns{

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of books.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single book.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        /** The content URI to access the book data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);

        /**
         * Inner class that defines constant values for the books database table.
         * Each entry in the table represents a single book.
         */
        public static final String TABLE_NAME = "books";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "product_name";
        public static final String COLUMN_PRODUCT_PRICE = "price";
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
        public static final String COLUMN_PRODUCT_SUPPLIER_NAME = "supplier_name";
        public static final String COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER = "supplier_phone_number";
    }
}
