package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "organizer.db";
    private static final String CATEGORIES_TABLE_NAME = "categories";
    private static final Map<String, String> CATEGORIES_COLUMNS = new HashMap<String, String>() {{
        put("ID", "ID");
        put("name", "name");
        put("color", "color");
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
        put("duration","duration");
        put("priority", "priority");
        put("state", "state");
        put("day", "day");
        put("month", "month");
        put("year", "year");
        put("imgPath", "imgPath");
        put("notification", "notification");
    }};
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " +
                CATEGORIES_TABLE_NAME + "("
                    + CATEGORIES_COLUMNS.get("ID") + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + CATEGORIES_COLUMNS.get("name") + " TEXT, "
                    + CATEGORIES_COLUMNS.get("color") + " TEXT)");
        ContentValues values = new ContentValues();
        values.put(CATEGORIES_COLUMNS.get("name"), "praca");
        values.put(CATEGORIES_COLUMNS.get("color"), "#62BEC1");
        db.insert(CATEGORIES_TABLE_NAME, null, values);
        values.put(CATEGORIES_COLUMNS.get("name"), "relaks");
        values.put(CATEGORIES_COLUMNS.get("color"), "#843CA9");
        db.insert(CATEGORIES_TABLE_NAME, null, values);
        values.put(CATEGORIES_COLUMNS.get("name"), "sport");
        values.put(CATEGORIES_COLUMNS.get("color"), "#110000");
        db.insert(CATEGORIES_TABLE_NAME, null, values);
        db.execSQL("CREATE TABLE " +
                EVENTS_TABLE + "("
                                + COLUMNS.get("ID") + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                + COLUMNS.get("name") + " TEXT, "
                                + COLUMNS.get("descriptions") + " TEXT, "
                                + COLUMNS.get("HH") + " TEXT, "
                                + COLUMNS.get("MM") + " TEXT, "
                                + COLUMNS.get("duration") + " TEXT, "
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

    public long insertEvent(Map<String, String> data){
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
        if(!data.get("category").equals("None")){
            contentValues.clear();
            contentValues.put(EVENT_CATEGORIES_COLUMNS.get("eventID"), res);
            contentValues.put(EVENT_CATEGORIES_COLUMNS.get("categoryID"), getCategories().indexOf(data.get("category")));
            res = sqlDB.insert(EVENT_CATEGORIES, null, contentValues);
            showEventCategories();
        }
        sqlDB.close();
        return res;
    }
    public ToDo getEvent(int id){
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
                           setDurationInMinutes(res.getString(res.getColumnIndex("duration")));
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
                System.out.print(column + ":" + queryResult.getString(queryResult.getColumnIndex(column)) + "\n");
            }
            queryResult.moveToNext();
        }
    }
    public void showEventCategories(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor queryResult = db.query(EVENT_CATEGORIES,  EVENT_CATEGORIES_COLUMNS.values().toArray(new String[EVENT_CATEGORIES_COLUMNS.size()]),
                null,
                null,
                null,
                null,
                null);
        queryResult.moveToFirst();
        while(queryResult.isAfterLast() == false) {
            for (String column : EVENT_CATEGORIES_COLUMNS.keySet()) {
                System.out.print(column + ":" + queryResult.getString(queryResult.getColumnIndex(column)) + "\n");
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
    public List<ToDo> getToDoesForWeek(MyDate date){
        List<ToDo> todoes = new ArrayList<ToDo>();
        for(int i = 0; i < 7; i++){
            Calendar d = Calendar.getInstance();
            if(i > 0){
                d.set(Integer.parseInt(date.getYear()),
                        Integer.parseInt(date.getMonth())-1,
                        Integer.parseInt(date.getDay())+1);
            } else{
                d.set(Integer.parseInt(date.getYear()),
                        Integer.parseInt(date.getMonth())-1,
                        Integer.parseInt(date.getDay()));
            }
            date.setDate(Integer.toString(d.get(Calendar.DAY_OF_MONTH)),
                                         Integer.toString(d.get(Calendar.MONTH)+1),
                                         Integer.toString(d.get(Calendar.YEAR)));
            for(ToDo todo : getToDoes(date)){
                todoes.add(todo);
            }
            System.out.println(date.getDate());
        }

        return todoes;
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
                setDurationInMinutes(res.getString(res.getColumnIndex("duration")));
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
    public ArrayList<ToDo> orderTodoes(ArrayList<ToDo> data){
        ArrayList<ToDo> high = new ArrayList<ToDo>();
        ArrayList<ToDo> medium = new ArrayList<ToDo>();
        ArrayList<ToDo> low = new ArrayList<ToDo>();
        for (ToDo todo : data) {
            boolean added = false;
            switch(todo.getPriority()){
                case "high":
                    if(high.isEmpty()){
                        high.add(todo);
                    } else{
                        for(int j = 0; j < high.size(); j++){
                            if(high.get(j).getHH().equals(todo.getHH())){
                                if(Integer.parseInt(high.get(j).getMM()) > Integer.parseInt(todo.getMM())){
                                    high.add(j, todo);
                                    added = true;
                                    break;
                                }
                            } else{
                                if(Integer.parseInt(high.get(j).getHH()) > Integer.parseInt(todo.getHH())){
                                    high.add(j, todo);
                                    added = true;
                                    break;
                                }
                            }
                        }
                        if(!added){
                            high.add(todo);
                        }
                    }
                    break;
                case "medium":
                    if(medium.isEmpty()){
                        medium.add(todo);
                    } else{
                        for(int j = 0; j < medium.size(); j++){
                            if(medium.get(j).getHH().equals(todo.getHH())){
                                if(Integer.parseInt(medium.get(j).getMM()) > Integer.parseInt(todo.getMM())){
                                    medium.add(j, todo);
                                    added = true;
                                    break;
                                }
                            } else{
                                if(Integer.parseInt(medium.get(j).getHH()) > Integer.parseInt(todo.getHH())){
                                    medium.add(j, todo);
                                    added = true;
                                    break;
                                }
                            }
                        }
                        if(!added){
                            medium.add(todo);
                        }
                    }
                    break;
                default:
                    if(low.isEmpty()){
                        low.add(todo);
                    } else{
                        for(int j = 0; j < low.size(); j++){
                            if(low.get(j).getHH().equals(todo.getHH())){
                                if(Integer.parseInt(low.get(j).getMM()) > Integer.parseInt(todo.getMM())){
                                    low.add(j, todo);
                                    added = true;
                                    break;
                                }
                            } else{
                                if(Integer.parseInt(low.get(j).getHH()) > Integer.parseInt(todo.getHH())){
                                    low.add(j, todo);
                                    added = true;
                                    break;
                                }
                            }
                        }
                        if(!added){
                            low.add(todo);
                        }
                    }
                    break;
            }
        }
        for (int i = 0; i < data.size(); i++){
            if(i < high.size()){
                data.set(i, high.get(i));
            }
            if(i >= high.size() && i-high.size() < medium.size()){
                data.set(i , medium.get(i - high.size()));
            }
            if(i >= high.size() + medium.size()){
                data.set(i, low.get(i - (high.size() + medium.size())));
            }
        }
        return data;
    }

    public List<String> getCategories(){
        SQLiteDatabase sqlDB = this.getReadableDatabase();
        List<String> categories = new ArrayList<String>();
        final Cursor res = sqlDB.rawQuery("SELECT " + CATEGORIES_COLUMNS.get("name") + " FROM " + CATEGORIES_TABLE_NAME,null);
        res.moveToFirst();
        while(res.isAfterLast() == false) {
            categories.add(res.getString(res.getColumnIndex("name")));
            res.moveToNext();
        }
        return categories;
    }
    public String getColorForCategory(String id){
        SQLiteDatabase sqlDB = this.getReadableDatabase();
        Cursor res = sqlDB.rawQuery( "SELECT color " +
                        "FROM " + CATEGORIES_TABLE_NAME +
                        " WHERE id = (SELECT categoryID FROM " + EVENT_CATEGORIES + " WHERE eventID = " + id + ");",
                null );
        String color = null;
        res.moveToFirst();
        while(res.isAfterLast() == false) {
            color = res.getString(res.getColumnIndex("color"));
            res.moveToNext();
        }
        sqlDB.close();
        if(color == null){
            color = "#000000";
        }
        return color;
    }
}
