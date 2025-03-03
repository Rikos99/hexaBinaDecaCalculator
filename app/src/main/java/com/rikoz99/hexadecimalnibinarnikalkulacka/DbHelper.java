package com.rikoz99.hexadecimalnibinarnikalkulacka;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "conversions.db";

    private static final String SQL_CREATE_CONVERSIONS =
            "CREATE TABLE " + DbContract.Conversions.TABLE_NAME + " (" +
                    DbContract.Conversions._ID + " INTEGER PRIMARY KEY," +
                    DbContract.Conversions.COLUMN_NAME_CONVERSION_NUMBER + " INTEGER," +
                    DbContract.Conversions.COLUMN_NAME_CONVERSION_FROM + " INTEGER," +
                    DbContract.Conversions.COLUMN_NAME_CONVERSION_TO + " INTEGER," +
                    DbContract.Conversions.COLUMN_NAME_CONVERSION_RESULT + " TEXT);";

    private static final String SQL_DELETE_CONVERSIONS =
            "DROP TABLE IF EXISTS " + DbContract.Conversions.TABLE_NAME;

    public DbHelper(Context context) {super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {db.execSQL(SQL_CREATE_CONVERSIONS);}

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_CONVERSIONS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public ConversionEntryModel getConversion(int conversionID)
    {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                DbContract.Conversions.COLUMN_NAME_CONVERSION_NUMBER,
                DbContract.Conversions.COLUMN_NAME_CONVERSION_FROM,
                DbContract.Conversions.COLUMN_NAME_CONVERSION_TO,
                DbContract.Conversions.COLUMN_NAME_CONVERSION_RESULT
        };
        String selection = DbContract.Conversions._ID + " = ?";
        String[] selectionArgs = {Integer.toString(conversionID)};

        Cursor cursor = db.query(
                DbContract.Conversions.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        ConversionEntryModel conversion = null;
        if(cursor.moveToFirst())
        {
            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Conversions._ID)));
            int number = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Conversions.COLUMN_NAME_CONVERSION_NUMBER)));
            int from = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Conversions.COLUMN_NAME_CONVERSION_FROM)));
            int to = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Conversions.COLUMN_NAME_CONVERSION_TO)));

            conversion = new ConversionEntryModel(id, number, from, to);
        }
        cursor.close();

        return conversion;
    }

    public ArrayList<ConversionEntryModel> getAllConversions()
    {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                DbContract.Conversions.COLUMN_NAME_CONVERSION_NUMBER,
                DbContract.Conversions.COLUMN_NAME_CONVERSION_FROM,
                DbContract.Conversions.COLUMN_NAME_CONVERSION_TO,
                DbContract.Conversions.COLUMN_NAME_CONVERSION_RESULT
        };

        String sortOrder = DbContract.Conversions._ID + " ASC";

        Cursor cursor = db.query(
                DbContract.Conversions.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        ArrayList<ConversionEntryModel> conversions = new ArrayList<>();
        ConversionEntryModel conversion;
        while(cursor.moveToNext())
        {
            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Conversions._ID)));
            int number = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Conversions.COLUMN_NAME_CONVERSION_NUMBER)));
            int from = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Conversions.COLUMN_NAME_CONVERSION_FROM)));
            int to = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Conversions.COLUMN_NAME_CONVERSION_TO)));

            conversion = new ConversionEntryModel(id, number, from, to);

            conversions.add(conversion);
        }
        cursor.close();

        return conversions;
    }

    public long insertConversion(ConversionEntryModel conversion)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.Conversions.COLUMN_NAME_CONVERSION_NUMBER, conversion.getNumber());
        values.put(DbContract.Conversions.COLUMN_NAME_CONVERSION_FROM, conversion.getFrom());
        values.put(DbContract.Conversions.COLUMN_NAME_CONVERSION_TO, conversion.getTo());
        values.put(DbContract.Conversions.COLUMN_NAME_CONVERSION_RESULT, conversion.getResult());

        long newRowId = db.insert(DbContract.Conversions.TABLE_NAME, null, values);

        return newRowId;
    }

    public boolean deleteConversion(int conversionId)
    {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(DbContract.Conversions.TABLE_NAME, DbContract.Conversions._ID + " = ?", new String[]{Integer.toString(conversionId)}) > 0;
    }

    public boolean deleteAllConversations()
    {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(DbContract.Conversions.TABLE_NAME, true + " = ?", new String[]{"true"}) > 0;
    }

    public boolean updateConversion(ConversionEntryModel conversion)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.Conversions.COLUMN_NAME_CONVERSION_NUMBER, conversion.getNumber());
        values.put(DbContract.Conversions.COLUMN_NAME_CONVERSION_FROM, conversion.getFrom());
        values.put(DbContract.Conversions.COLUMN_NAME_CONVERSION_TO, conversion.getTo());
        values.put(DbContract.Conversions.COLUMN_NAME_CONVERSION_RESULT, conversion.getResult());

        return db.update(DbContract.Conversions.TABLE_NAME, values, DbContract.Conversions._ID + " = ?", new String[]{Integer.toString(conversion.getId())}) > 0;
    }
}
