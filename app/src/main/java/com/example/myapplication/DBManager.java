package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class DBManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "APP37.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE01_NAME = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PASS = "password";

    private static final String TABLE02_NAME = "passwords";
    private static final String COLUMN_USER_ID = "user";
    private static final String IS_PUBLIC = "public";
    private static final String COLUMN_DESC = "description";
    private static final String COLUMN_INSTRUCT = "instructions";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_CAL = "cal";
    private static final String COLUMN_TIME = "time";

    private final Context context;

    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    private String md5_calc(String password) {
        try {
            // Create a message digest object
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");

            // Update the message digest object with the data to hash
            messageDigest.update(password.getBytes());

            // Generate the hash
            byte[] hash = messageDigest.digest();

            // Convert the hash to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            String md5 = hexString.toString();

            // Print the MD5 hash
            //System.out.println(md5);
            return md5;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String queryUserTable = "CREATE TABLE IF NOT EXISTS " + TABLE01_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PASS + " TEXT);";
        String queryRecipeTable = "CREATE TABLE IF NOT EXISTS " + TABLE02_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY, " +
                IS_PUBLIC + " INTEGER, " +
                COLUMN_USER_ID+ " INTEGER, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DESC + " TEXT, " +
                COLUMN_INSTRUCT + " TEXT, " +
                COLUMN_TIME + " INTEGER, " +
                COLUMN_CAL + " INTEGER);";

        sqLiteDatabase.execSQL(queryUserTable);

        sqLiteDatabase.execSQL(queryRecipeTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE01_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE02_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertUser(String name, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_PASS, md5_calc(password));
        long result = db.insert(TABLE01_NAME,null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }

    }

    public void insertRecipe(int id, String name, String category, String desc, String instructions, int time, int cal, int isPublic)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USER_ID, id);
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_CATEGORY, category);
        cv.put(COLUMN_DESC, desc);
        cv.put(COLUMN_INSTRUCT, instructions);
        cv.put(COLUMN_TIME, time);
        cv.put(COLUMN_CAL, cal);
        cv.put(IS_PUBLIC, isPublic);

        long result = db.insert(TABLE02_NAME,null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public int checkForUser(String name, String userPassword)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT id FROM " + TABLE01_NAME +" WHERE name = '"+name+"' AND password = '"+md5_calc(userPassword)+"'";

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }

        return cursor.getCount();
    }

    public int doesExist(String name)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT id FROM " + TABLE01_NAME +" WHERE name = '"+name+"'";

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }

        return cursor.getCount();
    }

    @SuppressLint("Range")
    public String getId(String name, String userPassword)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT id FROM " + TABLE01_NAME +" WHERE name = '"+name+"' AND password = '"+md5_calc(userPassword)+"'";

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }


        cursor.moveToFirst();
        int a = cursor.getInt(cursor.getColumnIndex("id"));

        return String.valueOf(a);
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE01_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }


    /*public ArrayList<recipe> getRecipes(String category, int userId)
    {
        ArrayList<recipe> data = new ArrayList<recipe>();
        String query = "SELECT * FROM " + TABLE02_NAME + " WHERE category = '"+category+"' AND user = "+ userId + " OR public = 1 AND category = '"+category+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        cursor.moveToFirst();
        for (int i = 1 ; i <= cursor.getCount(); i++)
        {
            cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            data.add(new recipe(cursor.getString(
                    cursor.getColumnIndex(COLUMN_NAME)).toString(),
                    cursor.getString(cursor.getColumnIndex(COLUMN_DESC)).toString(),
                    cursor.getString(cursor.getColumnIndex(COLUMN_INSTRUCT)).toString(),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_TIME)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_CAL)),
                    cursor.getInt(cursor.getColumnIndex(IS_PUBLIC))
            ));
            cursor.moveToNext();
        }
        return data;
    }*/
}
