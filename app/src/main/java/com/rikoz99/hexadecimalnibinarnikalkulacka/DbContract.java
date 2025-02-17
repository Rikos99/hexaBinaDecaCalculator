package com.rikoz99.hexadecimalnibinarnikalkulacka;

import android.provider.BaseColumns;

public class DbContract
{
    private DbContract() {}

    public static class Conversions implements BaseColumns
    {
        public static final String TABLE_NAME = "conversions";
        public static final String COLUMN_NAME_CONVERSION_NUMBER = "number";
        public static final String COLUMN_NAME_CONVERSION_FROM = "convFrom";
        public static final String COLUMN_NAME_CONVERSION_TO = "convTo";
        public static final String COLUMN_NAME_CONVERSION_RESULT = "result";
    }
}
