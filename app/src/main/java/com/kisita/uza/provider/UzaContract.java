package com.kisita.uza.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/*
 * Created by HuguesKi on 03-09-17.
 */

public class UzaContract {
    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.kisita.uza";
    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.kisita.uza/items/ is a valid path for
    // looking at item data. content://com.kisita.uza/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.
    public static final String PATH_ITEMS      = "items";

    public static final String PATH_LIKES      = "likes";

    public static final String PATH_COMMANDS   = "commands";

    public static final String PATH_CHECKOUT   = "checkout";

    public static final String PATH_CATEGORY   = "category";



    public static final class LikesEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LIKES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LIKES;

        public static final String TABLE_NAME   = "likes";

        public static final String COLUMN_LIKES = "likes";

        public static final String COLUMN_ID    = "id";

        public static Uri buildPlaceUri() {
            return CONTENT_URI;
        }
    }

    public static final class CommandsEntry implements BaseColumns {
        public static final Uri CONTENT_URI_COMMANDS =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMMANDS).build();

        public static final Uri CONTENT_URI_CHECKOUT =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CHECKOUT).build();

        public static final String CONTENT_TYPE_COMMANDS =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COMMANDS;

        public static final String CONTENT_TYPE_CHECKOUT =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CHECKOUT;

        public static final String TABLE_NAME        = "commands";

        public static final String COLUMN_STATE      = "state";

        public static final String COLUMN_ID         = "id";

        public static final String COLUMN_COLOR      = "color";

        public static final String COLUMN_KEY        = "key";

        public static final String COLUMN_SIZE       = "size";

        public static final String COLUMN_QUANTITY   = "quantity";

        public static final String COLUMN_COMMENT    = "comment";

        public static Uri buildPlaceUri() {
            return CONTENT_URI_CHECKOUT;      }
    }

    /* Inner class that defines the table contents of the items table */
    public static final class ItemsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ITEMS).build();

        public static final Uri CATEGORY_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORY).build();


        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        public static final String TABLE_NAME         = "items";

        // Brand
        public static final String COLUMN_BRAND       = "brand";
        // Category
        public static final String COLUMN_CATEGORY    = "category";
        // Currency
        public static final String COLUMN_CURRENCY    = "currency";

        // Description
        public static final String COLUMN_DESCRIPTION = "description";

        // ID
        public static final String COLUMN_ID          = "id";

        public static final String COLUMN_AID         = "aid";

        public static final String COLUMN_NAME        = "name";

        // Pictures
        public static final String COLUMN_PICTURES    = "pictures";

        // Price
        public static final String COLUMN_PRICE       = "price";

        // Seller
        public static final String COLUMN_SELLER      = "seller";

        // Type
        public static final String COLUMN_TYPE        = "type";

        public static final String COLUMN_URL         = "url";

        public static final String COLUMN_COLOR       = "color";

        public static final String COLUMN_SIZE        = "size";

        public static final String COLUMN_WEIGHT      = "weight";

        public static Uri buildPlaceUri() {
            return CONTENT_URI;
        }
    }
}
