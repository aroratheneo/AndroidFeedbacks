package com.example.aroras.commentscollector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by aroras on 01-05-2016.
 */
public class MyDBHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Comments.db";
    private static final String TABLE_COMMENTS = "Comments";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_COMMENT = "CommentDetail";
    public static final String COLUMN_COMMENTBY = "CommentBy";
    public static final String COLUMN_COMMENTTIME = "CommentTime";

    private static String DB_PATH = "/data/data/com.example.aroras.commentscollector/databases/";

    public MyDBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_COMMENTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_COMMENT
                + " TEXT," + COLUMN_COMMENTBY + " TEXT " +" )";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    public void AddComments(String comment, String commentBy)
    {


        DateFormat df = new SimpleDateFormat("d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());

        ContentValues values = new ContentValues();
        values.put(COLUMN_COMMENT, comment);
        values.put(COLUMN_COMMENTBY, commentBy + " ;" + date);
        //values.put(COLUMN_COMMENTTIME, date);

        SQLiteDatabase db = this.getWritableDatabase();

        //db.delete(TABLE_COMMENTS,null,null);

        //String DROP_PRODUCTS_TABLE = "Drop  TABLE " +
        //        TABLE_COMMENTS + ")";
        //db.execSQL(DROP_PRODUCTS_TABLE);

        db.insert(TABLE_COMMENTS, null, values);
        db.close();
    }

    public void ReadFromTable()
    {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +TABLE_COMMENTS+" " ,null);

        while(cursor.moveToNext())
        {
            String comment = cursor.getString(1);
            Log.d("UserComments", comment);

        }

        db.close();


    }

    public void CopyDB()
    {
        try {
            String sDBName = "Comments.db";
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "data/com.example.aroras.commentscollector/databases/"+sDBName;
                String backupDBPath = "/.appname-external-data-cache/"+sDBName; //"{database name}";
                File dir = new File(sd,backupDBPath.replace(sDBName,""));
                if(dir.mkdir()) {

                }
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);
                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }

        } catch (Exception e)
        {

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion)
    {
        if (oldVersion < 2) { // do stuff for migrating to version 2

        }
        if (oldVersion < 3) { // do stuff for migrating to version 3

        }
    }
}
