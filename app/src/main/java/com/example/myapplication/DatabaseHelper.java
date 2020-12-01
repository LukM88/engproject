package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "organizer.db";
    private static final String CATEGORIES_TABLE_NAME = "categories";
    private static final Map<String, String> CATEGORIES_COLUMNS = new HashMap<String, String>() {{
        put("ID", "ID");
        put("name", "name");
    }};

    private static final String EVENT_CATEGORIES = "event_categories";
    private static final Map<String, String> EVENT_CATEGORIES_COLUMNS = new HashMap<String, String>() {{
        put("eventID", "eventID");
        put("categoryID", "categoryID");
    }};

    private static final String EVENTS_TABLE = "events";

    private final static Map<String, String> COLUMNS = new HashMap<String, String>() {{
        put("ID", "ID");
        put("name", "name");
        put("descriptions", "descriptions");
        put("HH", "HH");
        put("MM", "MM");
        put("priority", "priority");
        put("state", "state");
        put("day", "day");
        put("month", "month");
        put("year", "year");
        put("imgPath", "imgPath");
        put("notification", "notification");
    }};
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " +
                CATEGORIES_TABLE_NAME + "("
                    + CATEGORIES_COLUMNS.get("ID") + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + CATEGORIES_COLUMNS.get("name") +" TEXT)");
        db.execSQL("CREATE TABLE " +
                EVENTS_TABLE + "("
                                + COLUMNS.get("ID") + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                + COLUMNS.get("name") + " TEXT, "
                                + COLUMNS.get("descriptions") + " TEXT, "
                                + COLUMNS.get("HH") + " TEXT, "
                                + COLUMNS.get("MM") + " TEXT, "
                                + COLUMNS.get("priority") + " TEXT, "
                                + COLUMNS.get("state") + " BOOLEAN, "
                                + COLUMNS.get("day") + " TEXT, "
                                + COLUMNS.get("month") + " TEXT, "
                                + COLUMNS.get("year") + " TEXT, "
                                + COLUMNS.get("imgPath") + " TEXT, "
                                + COLUMNS.get("notification") + " TEXT)");
        db.execSQL("CREATE TABLE " +
                EVENT_CATEGORIES + "("
                + EVENT_CATEGORIES_COLUMNS.get("eventID") + " INTEGER , "
                + EVENT_CATEGORIES_COLUMNS.get("categoryID") +" INTEGER  )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORIES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EVENTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + EVENT_CATEGORIES);
        onCreate(db);
    }

    public long addEvent(Map<String, String> data){
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for(String column : COLUMNS.keySet()){
            if (column == "state"){
                contentValues.put(column, false);
                ;
            } else{
                contentValues.put(column, data.get(column));
            }
        }
        long res = sqlDB.insert(EVENTS_TABLE, null, contentValues);
        sqlDB.close();
        return res;
    }
    public ToDo getEvent(int id){
        System.out.println("weszło");
        SQLiteDatabase sqlDB = this.getReadableDatabase();
        final Cursor res = sqlDB.rawQuery( "SELECT * " +
                        "                       FROM " + EVENTS_TABLE +
                                              " WHERE " + COLUMNS.get("ID") + " = " + id,
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
                           setImgPath(res.getString(res.getColumnIndex("imgPath")));
                           setNotification(res.getString(res.getColumnIndex("notification")));
                           }};
    }
    public ArrayList<ToDo> getEvents(){
        SQLiteDatabase sqlDB = this.getReadableDatabase();
        ArrayList<ToDo> array_list2 = getToDoListFromCursor(sqlDB.rawQuery( "SELECT * FROM "+ EVENTS_TABLE,
                                                                    null ));
        sqlDB.close();
        return array_list2;
    }

    public void updateTodoStatus(ToDo toDo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMNS.get("state"), !toDo.getState());
        db.update(EVENTS_TABLE, cv, COLUMNS.get("ID") + " = " + toDo.getID(), null);
        db.close();
    }

    public void showEvents(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor queryResult = db.query(EVENTS_TABLE,  COLUMNS.values().toArray(new String[COLUMNS.size()]),
                       null,
                   null,
                       null,
                         null,
                       null);
        queryResult.moveToFirst();
        while(queryResult.isAfterLast() == false) {
            for (String column : COLUMNS.keySet()) {
                System.out.print(queryResult.getString(queryResult.getColumnIndex(column)) + "\n");
            }
            queryResult.moveToNext();
        }
    }

    public ArrayList<ToDo> getToDoes(){
        return getToDoes(new MyDate());
    }

    public ArrayList<ToDo> getToDoes(MyDate date){
        SQLiteDatabase sqlDB = this.getReadableDatabase();
        ArrayList<ToDo> listOfTodoes =  getToDoListFromCursor(sqlDB.rawQuery( "SELECT * " +
                                                                                  "FROM " + EVENTS_TABLE + " " +
                                                                                  "WHERE day = '" + date.getDay() + "' " +
                                                                                        "AND year = '" + date.getYear() +
                                                                                        "'AND month = '" + date.getMonth() +"'",
                                                                    null ));
        sqlDB.close();
        return listOfTodoes;
    }

    public ArrayList<ToDo> getEventsWithName(String name){
        SQLiteDatabase sqlDB = this.getReadableDatabase();
        ArrayList<ToDo> todoList = getToDoListFromCursor(sqlDB.rawQuery( "SELECT * " +
                                                                              "FROM " + EVENTS_TABLE +
                                                                              " WHERE name LIKE '%" + name + "%'",
                                                                 null ));
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
                setImgPath(res.getString(res.getColumnIndex("imgPath")));
                setNotification(res.getString(res.getColumnIndex("notification")));
            }});
            res.moveToNext();
        }
        return todoList;
    }

    public int[] getDoneEventsStatistics(MyDate date){
        int done = 0;
        int all = 0;
        SQLiteDatabase sqlDB = this.getReadableDatabase();
        Cursor res = sqlDB.rawQuery( "SELECT count() AS 'all' " +
                                          "FROM " + EVENTS_TABLE +
                                          " WHERE day = '" + date.getDay() + "' " +
                                                "AND year = '" + date.getYear() + "' " +
                                                "AND month = '" + date.getMonth() + "'",
                                    null );
        res.moveToFirst();
        all = Integer.parseInt(res.getString(res.getColumnIndex("all")));
        res = sqlDB.rawQuery( "SELECT count() AS 'done' " +
                                   "FROM " + EVENTS_TABLE + " " +
                                   "WHERE day = '" + date.getDay() + "' " +
                                        "AND year = '" + date.getYear() + "' " +
                                        "AND month = '" + date.getMonth() + "' " +
                                        "AND state = 1",
                                    null );
        res.moveToFirst();
        done = Integer.parseInt(res.getString(res.getColumnIndex("done")));

        return new int[]{all - done, done};
    }

}
