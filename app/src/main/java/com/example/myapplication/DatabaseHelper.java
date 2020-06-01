package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static int lastCheck = 0;
    public static final String DATABASE_NAME = "organizer.db";
    public static final String TABLE_NAME = "users";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "login";
    public static final String COL_3 = "password";
    public static final String COL_4 = "hint";

    public static final String TABLE2 = "events";
    public static final String TAB2COL_1 = "ID";
    public static final String TAB2COL_2 = "name";
    public static final String TAB2COL_3 = "descriptions";
    public static final String TAB2COL_4 = "HH";
    public static final String TAB2COL_5 = "MM";
    public static final String TAB2COL_6 = "priority";
    public static final String TAB2COL_7 = "state";
    public static final String TAB2COL_8 = "day";
    public static final String TAB2COL_9 = "month";
    public static final String TAB2COL_10 = "year";
    public static final String TAB2COL_11 = "owner";
    public static final String TAB2COL_12 = "imgPath";
    public static final String[] kolumny = {TAB2COL_1,TAB2COL_2,TAB2COL_3,TAB2COL_4,TAB2COL_5,TAB2COL_6,TAB2COL_7,TAB2COL_8,TAB2COL_9,TAB2COL_10,TAB2COL_11,TAB2COL_12};
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (ID INTEGER PRIMARY KEY AUTOINCREMENT, login TEXT, password TEXT, hint TEXT)");
        db.execSQL("CREATE TABLE "+TABLE2+" ("+TAB2COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+TAB2COL_2+" TEXT, "+TAB2COL_3+" TEXT, "+TAB2COL_4+" TEXT, "+TAB2COL_5+" TEXT, "+TAB2COL_6+" TEXT, "+TAB2COL_7+" BOOLEAN, "+TAB2COL_8+" TEXT, "+TAB2COL_9+" TEXT, "+TAB2COL_10+" TEXT, "+TAB2COL_11+" TEXT, "+TAB2COL_12+" TEXT)");
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

    public long addEvent(String name, String description, String HH,String MM,String priority,boolean state,String day,String month,String year,String owner,String imgPath){
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TAB2COL_2, name);
        contentValues.put(TAB2COL_3, description);
        contentValues.put(TAB2COL_4, HH);
        contentValues.put(TAB2COL_5, MM);
        contentValues.put(TAB2COL_6, priority);
        contentValues.put(TAB2COL_7, state);
        contentValues.put(TAB2COL_8, day);
        contentValues.put(TAB2COL_9, month);
        contentValues.put(TAB2COL_10, year);
        contentValues.put(TAB2COL_11, owner);
        contentValues.put(TAB2COL_12, imgPath);

        long res = sqlDB.insert(TABLE2, null, contentValues);
        sqlDB.close();
        return res;
    }
    public ToDo getEvent(int id){
        SQLiteDatabase sqlDB = this.getReadableDatabase();
        ToDo todo=new ToDo();
        Cursor res = sqlDB.rawQuery( "select * from "+TABLE2+" WHERE "+TAB2COL_1+"="+id, null );
        res.moveToFirst();

        while(res.isAfterLast() == false) {
            todo.setID(res.getString(res.getColumnIndex("ID")));
            todo.setName(res.getString(res.getColumnIndex("name")));
            todo.setDescription(res.getString(res.getColumnIndex("descriptions")));
            todo.setHH(res.getString(res.getColumnIndex("HH")));
            todo.setMM(res.getString(res.getColumnIndex("MM")));
            todo.setPriority(res.getString(res.getColumnIndex("priority")));
            todo.setDay(res.getString(res.getColumnIndex("day")));
            todo.setMonth(res.getString(res.getColumnIndex("month")));
            todo.setYear(res.getString(res.getColumnIndex("year")));
            todo.setOwner(res.getString(res.getColumnIndex("owner")));
            todo.setImgPath(res.getString(res.getColumnIndex("imgPath")));
            res.moveToNext();

        }
        sqlDB.close();
        return todo;
    }
    public ArrayList<ToDo> getEvents(){
        SQLiteDatabase sqlDB = this.getReadableDatabase();

        ArrayList<String> array_list = new ArrayList<String>();
        ArrayList<ToDo> array_list2 = new ArrayList<ToDo>();
        Cursor res = sqlDB.rawQuery( "select * from "+TABLE2, null );
        res.moveToFirst();
        int i=0;
        while(res.isAfterLast() == false) {
            array_list2.add(new ToDo());
            array_list2.get(i).setID(res.getString(res.getColumnIndex("ID")));
            array_list2.get(i).setName(res.getString(res.getColumnIndex("name")));
            array_list2.get(i).setDescription(res.getString(res.getColumnIndex("descriptions")));
            array_list2.get(i).setHH(res.getString(res.getColumnIndex("HH")));
            array_list2.get(i).setMM(res.getString(res.getColumnIndex("MM")));
            array_list2.get(i).setPriority(res.getString(res.getColumnIndex("priority")));
            if(res.getInt(res.getColumnIndex("state")) == 1 )
            {
                array_list2.get(i).setState(true);
            }
            else {
                array_list2.get(i).setState(false);
            }
            array_list2.get(i).setDay(res.getString(res.getColumnIndex("day")));
            array_list2.get(i).setMonth(res.getString(res.getColumnIndex("month")));
            array_list2.get(i).setYear(res.getString(res.getColumnIndex("year")));
            array_list2.get(i).setOwner(res.getString(res.getColumnIndex("owner")));
            array_list2.get(i).setImgPath(res.getString(res.getColumnIndex("imgPath")));
            res.moveToNext();
            i++;
        }

        sqlDB.close();
        return array_list2;
    }

    public void chceck(ToDo toDo) {
        SQLiteDatabase db = this.getWritableDatabase();
        showEvents();
        ContentValues cv = new ContentValues();
        cv.put(TAB2COL_7,!toDo.getState());
        long res = db.update(TABLE2,cv,TAB2COL_1+"="+toDo.getID(),null);
        showEvents();
        db.close();
        try{
            this.lastCheck = Integer.parseInt(toDo.getID());
        }catch (NumberFormatException e ){
            e.printStackTrace();
        }
    }

    public void showEvents(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor kursor = db.query(TABLE2,kolumny,null,null,null,null,null);
        kursor.moveToFirst();
        while(kursor.isAfterLast() == false) {
            for(int i = 0 ;i<kolumny.length;i++){
                System.out.print(kursor.getString(kursor.getColumnIndex(kolumny[i]))+"\n");
            }

            kursor.moveToNext();
        }

    }
    public ArrayList<ToDo> getToDoes(){
        SQLiteDatabase sqlDB = this.getReadableDatabase();

        ArrayList<String> array_list = new ArrayList<String>();
        ArrayList<ToDo> array_list2 = new ArrayList<ToDo>();
        MyDate date = new MyDate();

        String day = date.getDay();
        String month = date.getMonth();
        String year =  date.getYear();


        Cursor res = sqlDB.rawQuery( "select * from "+TABLE2+" where day='"+day+"' AND year='"+year+"' AND month='"+month+"'", null );
        res.moveToFirst();
        int i=0;
        while(res.isAfterLast() == false) {
            array_list2.add(new ToDo());
            array_list2.get(i).setID(res.getString(res.getColumnIndex("ID")));
            array_list2.get(i).setName(res.getString(res.getColumnIndex("name")));
            array_list2.get(i).setDescription(res.getString(res.getColumnIndex("descriptions")));
            array_list2.get(i).setHH(res.getString(res.getColumnIndex("HH")));
            array_list2.get(i).setMM(res.getString(res.getColumnIndex("MM")));
            array_list2.get(i).setPriority(res.getString(res.getColumnIndex("priority")));
            if(res.getInt(res.getColumnIndex("state")) == 1 )
            {
                array_list2.get(i).setState(true);
            }
            else {
                array_list2.get(i).setState(false);
            }
            array_list2.get(i).setDay(res.getString(res.getColumnIndex("day")));
            array_list2.get(i).setMonth(res.getString(res.getColumnIndex("month")));
            array_list2.get(i).setYear(res.getString(res.getColumnIndex("year")));
            array_list2.get(i).setOwner(res.getString(res.getColumnIndex("owner")));
            array_list2.get(i).setImgPath(res.getString(res.getColumnIndex("imgPath")));
            res.moveToNext();

            i++;
        }

        sqlDB.close();
        return array_list2;
    }

    public ArrayList<ToDo> getToDoes(MyDate date){
        SQLiteDatabase sqlDB = this.getReadableDatabase();

        ArrayList<String> array_list = new ArrayList<String>();
        ArrayList<ToDo> array_list2 = new ArrayList<ToDo>();

        String day = date.getDay();
        String month = date.getMonth();
        String year =  date.getYear();


        Cursor res = sqlDB.rawQuery( "select * from "+TABLE2+" where day='"+day+"' AND year='"+year+"' AND month='"+month+"'", null );
        res.moveToFirst();
        int i=0;
        while(res.isAfterLast() == false) {
            array_list2.add(new ToDo());
            array_list2.get(i).setID(res.getString(res.getColumnIndex("ID")));
            array_list2.get(i).setName(res.getString(res.getColumnIndex("name")));
            array_list2.get(i).setDescription(res.getString(res.getColumnIndex("descriptions")));
            array_list2.get(i).setHH(res.getString(res.getColumnIndex("HH")));
            array_list2.get(i).setMM(res.getString(res.getColumnIndex("MM")));
            array_list2.get(i).setPriority(res.getString(res.getColumnIndex("priority")));
            if(res.getInt(res.getColumnIndex("state")) == 1 )
            {
                array_list2.get(i).setState(true);
            }
            else {
                array_list2.get(i).setState(false);
            }
            array_list2.get(i).setDay(res.getString(res.getColumnIndex("day")));
            array_list2.get(i).setMonth(res.getString(res.getColumnIndex("month")));
            array_list2.get(i).setYear(res.getString(res.getColumnIndex("year")));
            array_list2.get(i).setOwner(res.getString(res.getColumnIndex("owner")));
            array_list2.get(i).setImgPath(res.getString(res.getColumnIndex("imgPath")));
            res.moveToNext();

            i++;
        }

        sqlDB.close();
        return array_list2;
    }

    public ArrayList<ToDo> getEventsWithName(String name){
        SQLiteDatabase sqlDB = this.getReadableDatabase();

        ArrayList<String> array_list = new ArrayList<String>();
        ArrayList<ToDo> array_list2 = new ArrayList<ToDo>();

        Cursor res = sqlDB.rawQuery( "select * from "+TABLE2+" where name like '%"+name+"%'", null );
        res.moveToFirst();
        int i=0;
        while(res.isAfterLast() == false) {
            array_list2.add(new ToDo());
            array_list2.get(i).setID(res.getString(res.getColumnIndex("ID")));
            array_list2.get(i).setName(res.getString(res.getColumnIndex("name")));
            array_list2.get(i).setDescription(res.getString(res.getColumnIndex("descriptions")));
            array_list2.get(i).setHH(res.getString(res.getColumnIndex("HH")));
            array_list2.get(i).setMM(res.getString(res.getColumnIndex("MM")));
            array_list2.get(i).setPriority(res.getString(res.getColumnIndex("priority")));
            if(res.getInt(res.getColumnIndex("state")) == 1 )
            {
                array_list2.get(i).setState(true);
            }
            else {
                array_list2.get(i).setState(false);
            }
            array_list2.get(i).setDay(res.getString(res.getColumnIndex("day")));
            array_list2.get(i).setMonth(res.getString(res.getColumnIndex("month")));
            array_list2.get(i).setYear(res.getString(res.getColumnIndex("year")));
            array_list2.get(i).setOwner(res.getString(res.getColumnIndex("owner")));
            array_list2.get(i).setImgPath(res.getString(res.getColumnIndex("imgPath")));
            res.moveToNext();

            i++;
        }

        sqlDB.close();
        System.out.println(array_list2.size());
        return array_list2;
    }
    public int lastChceck() {
        return this.lastChceck();
    }
}
