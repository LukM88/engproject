package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "organizer.db";
    public static final String TABLE_NAME = "users";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "login";
    public static final String COL_3 = "password";
    public static final String COL_4 = "hint";

    private static final String TABLE2 = "events";
    private final String[] kolumny = { "ID",
                                       "name",
                                       "descriptions",
                                       "HH",
                                       "MM",
                                       "priority",
                                       "state",
                                       "day",
                                       "month",
                                       "year",
                                       "owner",
                                       "imgPath",
                                       "notification"};
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (ID INTEGER PRIMARY KEY AUTOINCREMENT, login TEXT, password TEXT, hint TEXT)");
        db.execSQL("CREATE TABLE "+
                    TABLE2 + " ("
                                + kolumny[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                + kolumny[1] + " TEXT, "
                                + kolumny[2] + " TEXT, "
                                + kolumny[3] + " TEXT, "
                                + kolumny[4] + " TEXT, "
                                + kolumny[5] + " TEXT, "
                                + kolumny[6] + " BOOLEAN, "
                                + kolumny[7] + " TEXT, "
                                + kolumny[8] + " TEXT, "
                                + kolumny[9] + " TEXT, "
                                + kolumny[10] + " TEXT, "
                                + kolumny[11] + " TEXT, "
                                + kolumny[12] + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE2);
        onCreate(db);
    }

    public long addUser(String login, String password, String hint){
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("login", login);
        contentValues.put("password", password);
        contentValues.put("hint", hint);
        long res = sqlDB.insert("users", null, contentValues);
        sqlDB.close();
        return res;
    }

    public boolean doesUserExist(String login, String password){
        String[] columns = {COL_1};
        SQLiteDatabase sqlDB = getReadableDatabase();
        String selection = COL_2 + "=?" + " and " + COL_3 + "=?";
        String[] selectionArgs = {login, password};
        Cursor cursor = sqlDB.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        sqlDB.close();

        if(count>0)
            return true;
        else
            return false;
    }

    public Cursor getHint(String login, SQLiteDatabase sqLiteDatabase)
    {
        String[] projections = {COL_4};
        String selection = COL_2 + " LIKE ?";
        String[] selection_args = {login};
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, projections, selection, selection_args, null,null,null);
        return cursor;
    }

    public long addEvent(String[] data){
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for(int i=0; i < data.length; i++){
            if (i != 5){
                contentValues.put(kolumny[i+1], data[i]);
            } else{
                contentValues.put(kolumny[i+1], false);
            }
        }
        long res = sqlDB.insert(TABLE2, null, contentValues);
        sqlDB.close();
        return res;
    }
    public ToDo getEvent(int id){
        SQLiteDatabase sqlDB = this.getReadableDatabase();
        final Cursor res = sqlDB.rawQuery( "SELECT * " +
                        "                       FROM " + TABLE2 +
                                              " WHERE " + kolumny[0] + " = " + id,
                                   null );
        res.moveToFirst();
        sqlDB.close();
        return new ToDo(){{
                           setID(res.getString(res.getColumnIndex("ID")));
                           setName(res.getString(res.getColumnIndex("name")));
                           setDescription(res.getString(res.getColumnIndex("descriptions")));
                           setHH(res.getString(res.getColumnIndex("HH")));
                           setMM(res.getString(res.getColumnIndex("MM")));
                           setPriority(res.getString(res.getColumnIndex("priority")));
                           setDay(res.getString(res.getColumnIndex("day")));
                           setMonth(res.getString(res.getColumnIndex("month")));
                           setYear(res.getString(res.getColumnIndex("year")));
                           setOwner(res.getString(res.getColumnIndex("owner")));
                           setImgPath(res.getString(res.getColumnIndex("imgPath")));
                           setNotification(res.getString(res.getColumnIndex("notification")));
                           }};
    }
    public ArrayList<ToDo> getEvents(){
        SQLiteDatabase sqlDB = this.getReadableDatabase();
        ArrayList<ToDo> array_list2 = getToDoListFromCursor(sqlDB.rawQuery( "SELECT * FROM "+TABLE2, null ));
        sqlDB.close();
        return array_list2;
    }

    public void updateTodoStatus(ToDo toDo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(kolumny[6], !toDo.getState());
        db.update(TABLE2, cv, kolumny[0] + " = " + toDo.getID(), null);
        db.close();
    }

    public void showEvents(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor kursor = db.query(TABLE2, kolumny,null,null,null,null,null);
        kursor.moveToFirst();
        while(kursor.isAfterLast() == false) {
            for(int i = 0 ; i < kolumny.length; i++){
                System.out.print(kursor.getString(kursor.getColumnIndex(kolumny[i])) + "\n");
            }
            kursor.moveToNext();
        }
    }

    public ArrayList<ToDo> getToDoes(){
        return getToDoes(new MyDate());
    }

    public ArrayList<ToDo> getToDoes(MyDate date){
        SQLiteDatabase sqlDB = this.getReadableDatabase();
        ArrayList<ToDo> listOfTodos =  getToDoListFromCursor(sqlDB.rawQuery( "SELECT * " +
                                                                                  "FROM " + TABLE2 + " " +
                                                                                  "WHERE day = '" + date.getDay() + "' " +
                                                                                        "AND year = '" + date.getYear() +
                                                                                        "'AND month = '" + date.getMonth() +"'",
                                                                    null ));
        sqlDB.close();
        return listOfTodos;
    }

    public ArrayList<ToDo> getEventsWithName(String name){
        SQLiteDatabase sqlDB = this.getReadableDatabase();
        ArrayList<ToDo> todoList = getToDoListFromCursor(sqlDB.rawQuery( "select * from "+TABLE2+" where name like '%"+name+"%'", null ));
        sqlDB.close();
        return todoList;
    }
    private ArrayList<ToDo> getToDoListFromCursor(final Cursor res){
        ArrayList<ToDo> todoList = new ArrayList<ToDo>();
        res.moveToFirst();
        while(res.isAfterLast() == false) {
            todoList.add(new ToDo(){{
                setID(res.getString(res.getColumnIndex("ID")));
                setName(res.getString(res.getColumnIndex("name")));
                setDescription(res.getString(res.getColumnIndex("descriptions")));
                setHH(res.getString(res.getColumnIndex("HH")));
                setMM(res.getString(res.getColumnIndex("MM")));
                setPriority(res.getString(res.getColumnIndex("priority")));
                setState(res.getInt(res.getColumnIndex("state")) == 1);
                setDay(res.getString(res.getColumnIndex("day")));
                setMonth(res.getString(res.getColumnIndex("month")));
                setYear(res.getString(res.getColumnIndex("year")));
                setOwner(res.getString(res.getColumnIndex("owner")));
                setImgPath(res.getString(res.getColumnIndex("imgPath")));
                setNotification(res.getString(res.getColumnIndex("notification")));
            }});
            res.moveToNext();
        }
        return todoList;
    }

    public int[] getDoneEventsStsts(MyDate date){
        int done = 0;
        int all = 0;
        SQLiteDatabase sqlDB = this.getReadableDatabase();
        Cursor res = sqlDB.rawQuery( "SELECT count() AS 'all' FROM "+TABLE2+" WHERE day='"+date.getDay()+"' AND year='"+date.getYear()+"' AND month='"+date.getMonth()+"'", null );
        res.moveToFirst();
        all = Integer.parseInt(res.getString(res.getColumnIndex("all")));
        res = sqlDB.rawQuery( "SELECT count() AS 'done' FROM "+TABLE2+" WHERE day='"+date.getDay()+"' AND year='"+date.getYear()+"' AND month='"+date.getMonth()+"' AND state = 1", null );
        res.moveToFirst();
        done = Integer.parseInt(res.getString(res.getColumnIndex("done")));

        int[] data = {all-done,done};
        return data;
    }

}
