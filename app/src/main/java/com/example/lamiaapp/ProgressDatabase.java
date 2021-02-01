package com.example.lamiaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class ProgressDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ProgessDB.db";
    public static final String SUDOKU_TABLE_NAME = "SudokuProgressTable";

    public static final String SUDOKU_COLUMN_ID = "id";
    public static final String SUDOKU_COLUMN_NUMBERS = "PlayedTable";
    public static final String SUDOKU_COLUMN_GAME_NUMBERS = "SolvedTable";
    public static final String SUDOKU_COLUMN_DIFFICULTY = "Difficulty";
    public static final String SUDOKU_COLUMN_RESULT = "Won";
    public static final String SUDOKU_COLUMN_TIME = "Time";

    private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+SUDOKU_TABLE_NAME;
    private static final String CREATE_TABLE = "CREATE TABLE "+SUDOKU_TABLE_NAME+  " ("+SUDOKU_COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " + SUDOKU_COLUMN_NUMBERS+" VARCHAR(81) ," + SUDOKU_COLUMN_GAME_NUMBERS + " VARCHAR(81), " + SUDOKU_COLUMN_DIFFICULTY + " VARCHAR(30), " + SUDOKU_COLUMN_RESULT + " VARCHAR(7), " + SUDOKU_COLUMN_TIME + " VARCHAR(20));";
    private Context context;

    public ProgressDatabase(Context context) {
        super(context, DATABASE_NAME , null, 1);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL(DROP_TABLE);
        onCreate(db);
        Log.d("Database: ", "Created");
    }

    public boolean insertSudokuTable (int id, String numbers, String game, String difficulty, String result, String timer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SUDOKU_COLUMN_ID, id);
        contentValues.put(SUDOKU_COLUMN_NUMBERS, numbers);
        contentValues.put(SUDOKU_COLUMN_GAME_NUMBERS, game);
        contentValues.put(SUDOKU_COLUMN_DIFFICULTY, difficulty);
        contentValues.put(SUDOKU_COLUMN_RESULT, result);
        contentValues.put(SUDOKU_COLUMN_TIME, timer);
        db.insert(SUDOKU_TABLE_NAME, null, contentValues);
        return true;
    }

    public String getNumbers(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT " + SUDOKU_COLUMN_NUMBERS + " from " + SUDOKU_TABLE_NAME + " where id="+id+"", null );
        res.moveToFirst();
        String numbers = res.getString(res.getColumnIndex(SUDOKU_COLUMN_NUMBERS));
        return numbers;
    }

    public String getNumbersGame(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT " + SUDOKU_COLUMN_GAME_NUMBERS + " from " + SUDOKU_TABLE_NAME + " where id="+id+"", null );
        res.moveToFirst();
        String numbers = res.getString(res.getColumnIndex(SUDOKU_COLUMN_GAME_NUMBERS));
        return numbers;
    }

    public String GetTimer(int id){ //prelevo il timer
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT " + SUDOKU_COLUMN_TIME + " from " + SUDOKU_TABLE_NAME + " where id="+id+"", null );
        res.moveToFirst();
        String timer = res.getString(res.getColumnIndex(SUDOKU_COLUMN_TIME));
        return timer;
    }

    public String GetMode(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT " + SUDOKU_COLUMN_DIFFICULTY + " from " + SUDOKU_TABLE_NAME + " where id="+id+"", null );
        res.moveToFirst();
        String mode = res.getString(res.getColumnIndex(SUDOKU_COLUMN_DIFFICULTY));
        return mode;
    }

    public String GetVictory(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT " + SUDOKU_COLUMN_RESULT + " from " + SUDOKU_TABLE_NAME + " where id="+id+"", null );
        res.moveToFirst();
        String result = res.getString(res.getColumnIndex(SUDOKU_COLUMN_RESULT));
        return result;
    }

    public int numberOfRows(){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            int numRows = (int) DatabaseUtils.queryNumEntries(db, SUDOKU_TABLE_NAME);
            return numRows;
        }catch (Exception e){ }
        return 0;
    }

    public ArrayList<String> getAllTables() {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + SUDOKU_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(SUDOKU_COLUMN_NUMBERS)));
            res.moveToNext();
        }

        return array_list;
    }
}
