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

    private static final String TABLE02_NAME = "user_passwords";
    private static final String COLUMN_USER_ID = "user";
    private static final String COLUMN_DESC = "description";

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
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID+ " TEXT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DESC + " TEXT, " +
                COLUMN_PASS + " TEXT);";

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

    public void insertNewPass(String name, String desc, String pass, String user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_ID, user);
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_DESC, desc);
        cv.put(COLUMN_PASS, pass);

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

    public void removeEntry(Entry entry)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE02_NAME, "name=?", new String[]{entry.getName()});
        db.close();
    }

    @SuppressLint("Range")
    public ArrayList<Entry> getRecipes(String userId)
    {
        ArrayList<Entry> data = new ArrayList<Entry>();
        String query = "SELECT * FROM " + TABLE02_NAME + " WHERE user = '"+ userId+ "';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        cursor.moveToFirst();
        for (int i = 1 ; i <= cursor.getCount(); i++)
        {
            cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            data.add(new Entry(
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)).toString(),
                    cursor.getString(cursor.getColumnIndex(COLUMN_DESC)).toString(),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PASS)).toString(),
                    cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)).toString()

            ));
            cursor.moveToNext();
        }
        return data;
    }
}
