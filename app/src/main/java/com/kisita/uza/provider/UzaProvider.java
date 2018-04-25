package com.kisita.uza.provider;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.kisita.uza.services.FirebaseUtils.columnsArray2string;

/*
 * Created by HuguesKi on 03-09-17.
 */

public class UzaProvider extends ContentProvider {
    static final String  TAG = "### UzaProvider";
    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private UzaDbHelper mOpenHelper;

    static final int ITEMS       = 100;
    static final int COMMANDS    = 101;
    static final int FAVOURITES  = 102;
    static final int SEARCH      = 103;
    static final int CHECKOUT    = 104;
    static final int CATEGORY    = 105;
    static final int SAME_AUTHOR = 106;

    @Override
    public boolean onCreate() {
        Log.i(TAG,"onCreate");
        mOpenHelper = new UzaDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            case SEARCH:{
                selectionArgs[0] = "%"+selectionArgs[0]+"%";
                String[] columns = new String[]{"name"};
                retCursor = mOpenHelper.getReadableDatabase().query(
                        UzaContract.ItemsEntry.TABLE_NAME,
                        columns, // List of columns to return
                        selection,  // A filter declaring which rows to return
                        selectionArgs, // You may include ?s in selection, which will be replaced by the values from selectionArgs
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case ITEMS: {
                Log.i(TAG,"** Items query ...");
                retCursor = mOpenHelper.getReadableDatabase().query(
                        UzaContract.ItemsEntry.TABLE_NAME,
                        projection, // List of columns to return
                        selection,  // A filter declaring which rows to return
                        selectionArgs, // You may include ?s in selection, which will be replaced by the values from selectionArgs
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case SAME_AUTHOR: {
                String sql = "SELECT " + columnsArray2string(projection)    +" FROM " + UzaContract.ItemsEntry.TABLE_NAME     +
                        " LEFT JOIN "   + UzaContract.LikesEntry.TABLE_NAME  +
                        " ON "           + UzaContract.ItemsEntry.TABLE_NAME     + "." + UzaContract.ItemsEntry._ID +
                        " =  "           + UzaContract.LikesEntry.TABLE_NAME  + "." + UzaContract.LikesEntry.COLUMN_LIKES +
                        // Command table
                        " LEFT JOIN "    + UzaContract.CommandsEntry.TABLE_NAME  +
                        " ON "           + UzaContract.LikesEntry.TABLE_NAME  + "." + UzaContract.LikesEntry.COLUMN_LIKES +
                        "="              + UzaContract.CommandsEntry.TABLE_NAME  + "." + UzaContract.CommandsEntry.COLUMN_KEY +
                        " WHERE " + UzaContract.ItemsEntry.TABLE_NAME  + "."     + UzaContract.ItemsEntry.COLUMN_AUTHOR +
                        " = "     + "'" + selection + "'" + ";";
                //Log.i(TAG,"** category query ..." + sql);
                retCursor = mOpenHelper.getWritableDatabase().rawQuery(sql, null);
                break;
            }

            case FAVOURITES: {
                String whereClause = "";
                String innerClause = "";
                if(!selection.equalsIgnoreCase("")){
                    whereClause = " WHERE " + UzaContract.LikesEntry.TABLE_NAME  + "."     + UzaContract.LikesEntry.COLUMN_LIKES +
                            "="       + "'" + selection + "'" ;
                }else { // Favourite fragment
                    innerClause = " INNER JOIN "   + UzaContract.ItemsEntry.TABLE_NAME  +
                                  " ON "           + UzaContract.ItemsEntry.TABLE_NAME  + "." + UzaContract.ItemsEntry._ID +
                                  " =  "           + UzaContract.LikesEntry.TABLE_NAME  + "." + UzaContract.LikesEntry.COLUMN_LIKES+
                                // Command table
                                  " LEFT JOIN "    + UzaContract.CommandsEntry.TABLE_NAME  +
                                  " ON "           + UzaContract.LikesEntry.TABLE_NAME  + "." + UzaContract.LikesEntry.COLUMN_LIKES +
                                  "="              + UzaContract.CommandsEntry.TABLE_NAME  + "." + UzaContract.CommandsEntry.COLUMN_KEY ;
                }
                String sql = "SELECT " + columnsArray2string(projection)    +" FROM " + UzaContract.LikesEntry.TABLE_NAME  + innerClause + whereClause + ";";
                Log.i(TAG,"** Favourites query ..." + sql);
                retCursor = mOpenHelper.getWritableDatabase().rawQuery(sql, null);
                break;
            }

            case CATEGORY: {
                String sql = "SELECT " + columnsArray2string(projection)    +" FROM " + UzaContract.ItemsEntry.TABLE_NAME     +
                             // Likes table
                             " LEFT JOIN "    + UzaContract.LikesEntry.TABLE_NAME  +
                             " ON "           + UzaContract.ItemsEntry.TABLE_NAME     + "." + UzaContract.ItemsEntry._ID +
                             "="           + UzaContract.LikesEntry.TABLE_NAME  + "." + UzaContract.LikesEntry.COLUMN_LIKES +
                             // Command table
                             " LEFT JOIN "    + UzaContract.CommandsEntry.TABLE_NAME  +
                             " ON "           + UzaContract.ItemsEntry.TABLE_NAME  + "." + UzaContract.ItemsEntry._ID +
                             "="              + UzaContract.CommandsEntry.TABLE_NAME  + "." + UzaContract.CommandsEntry.COLUMN_KEY +
                             // TODO no need of where clause here
                            /*" WHERE " + UzaContract.CommandsEntry.TABLE_NAME  + "."     + UzaContract.CommandsEntry.COLUMN_STATE +
                            " = "     + "'0'" + */";";

                Log.i(TAG,"** category query ..." + sql);
                retCursor = mOpenHelper.getWritableDatabase().rawQuery(sql, null);
                break;
            }

            case COMMANDS: {
                //Log.i(TAG,"** Command query ...");

                String sql = "SELECT "+ columnsArray2string(projection) +" FROM " + UzaContract.ItemsEntry.TABLE_NAME     +
                             " INNER JOIN "   + UzaContract.CommandsEntry.TABLE_NAME  +
                             " ON "           + UzaContract.ItemsEntry.TABLE_NAME     + "." + UzaContract.ItemsEntry._ID +
                             " =  "           + UzaContract.CommandsEntry.TABLE_NAME  + "." + UzaContract.CommandsEntry.COLUMN_KEY +
                             // Command table
                             " LEFT JOIN "    + UzaContract.LikesEntry.TABLE_NAME  +
                             " ON "           + UzaContract.CommandsEntry.TABLE_NAME  + "." + UzaContract.CommandsEntry.COLUMN_KEY +
                             "="              + UzaContract.LikesEntry.TABLE_NAME  + "." + UzaContract.LikesEntry.COLUMN_LIKES     +
                             " WHERE "        + UzaContract.CommandsEntry.TABLE_NAME  + "." + UzaContract.CommandsEntry.COLUMN_STATE + "" +
                             " > 0 ;";
                //Log.i(TAG,"** Command query : " + sql);
                retCursor = mOpenHelper.getWritableDatabase().rawQuery(sql, null);
                break;
            }
            case CHECKOUT: {
                //Log.i(TAG, "** Checkout query ...");

                String sql = "SELECT " + columnsArray2string(projection) + " FROM " + UzaContract.ItemsEntry.TABLE_NAME +
                        " INNER JOIN " + UzaContract.CommandsEntry.TABLE_NAME +
                        " ON " + UzaContract.ItemsEntry.TABLE_NAME + "." + UzaContract.ItemsEntry._ID +
                        " =  " + UzaContract.CommandsEntry.TABLE_NAME + "." + UzaContract.CommandsEntry.COLUMN_KEY +
                        " WHERE " + UzaContract.CommandsEntry.TABLE_NAME + "." + UzaContract.CommandsEntry.COLUMN_STATE + "" +
                        " = 0 ;";
                //Log.i(TAG,"** Checkout query : " + sql);
                retCursor = mOpenHelper.getWritableDatabase().rawQuery(sql, null);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case ITEMS:
                return UzaContract.ItemsEntry.CONTENT_TYPE;
            case COMMANDS:
                return UzaContract.CommandsEntry.CONTENT_TYPE_COMMANDS;
            case CHECKOUT:
                return UzaContract.CommandsEntry.CONTENT_TYPE_CHECKOUT;
            case FAVOURITES:
                return UzaContract.LikesEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case ITEMS: {
                long _id = db.insertWithOnConflict(UzaContract.ItemsEntry.TABLE_NAME, null, values,SQLiteDatabase.CONFLICT_REPLACE);
                if ( _id > 0 )
                    returnUri = UzaContract.ItemsEntry.buildPlaceUri();
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case COMMANDS: {
                long _id = db.insertWithOnConflict(UzaContract.CommandsEntry.TABLE_NAME, null, values,SQLiteDatabase.CONFLICT_REPLACE);
                if ( _id > 0 )
                    returnUri = UzaContract.CommandsEntry.buildPlaceUri();
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FAVOURITES: {
                long _id = db.insertWithOnConflict(UzaContract.LikesEntry.TABLE_NAME, null, values,SQLiteDatabase.CONFLICT_REPLACE);
                if ( _id > 0 )
                    returnUri = UzaContract.LikesEntry.buildPlaceUri();
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(UzaContract.ItemsEntry.CATEGORY_URI, null);
        getContext().getContentResolver().notifyChange(uri, null);
        //db.close();
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        Log.i(TAG,uri.toString());
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case ITEMS:
                rowsDeleted = db.delete(
                        UzaContract.ItemsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVOURITES:
                rowsDeleted = db.delete(
                        UzaContract.LikesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case COMMANDS:
                rowsDeleted = db.delete(
                        UzaContract.CommandsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(UzaContract.ItemsEntry.CATEGORY_URI, null);
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case ITEMS:
                rowsUpdated = db.update(UzaContract.ItemsEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case FAVOURITES:
                rowsUpdated = db.update(UzaContract.LikesEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case COMMANDS:
                rowsUpdated = db.update(UzaContract.CommandsEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(UzaContract.ItemsEntry.CATEGORY_URI, null);
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    /*
Students: Here is where you need to create the UriMatcher. This UriMatcher will
match each URI to the PLACE, PLACE_WITH_LOCATION,
and LOCATION integer constants defined above.  You can test this by uncommenting the
testUriMatcher test within TestUriMatcher.
*/
    static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = UzaContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, UzaContract.PATH_ITEMS, ITEMS);
        matcher.addURI(authority, UzaContract.PATH_COMMANDS, COMMANDS);
        matcher.addURI(authority, UzaContract.PATH_CHECKOUT, CHECKOUT);
        matcher.addURI(authority, UzaContract.PATH_LIKES, FAVOURITES);
        matcher.addURI(authority, UzaContract.PATH_CATEGORY, CATEGORY);
        matcher.addURI(authority, UzaContract.PATH_SAME_AUTHOR, SAME_AUTHOR);
        matcher.addURI(authority, UzaContract.PATH_ITEMS + "/" + SearchManager.SUGGEST_URI_PATH_QUERY,SEARCH);

        return matcher;
    }
}
