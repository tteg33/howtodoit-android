package com.example.howtodoit.Utils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.howtodoit.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper{

    private static final int VERSION = 1;
    private static final String NAME = "todolistDB";
    private static final String TODO_TABLE ="todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String PROJECT = "project";
    private static final String STAR = "star";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
    + TASK + " TEXT, " + STATUS + " INTEGER, " + PROJECT + " TEXT, " + STAR + " INTEGER )";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context){
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //Drop the older tables
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        //Create tables again
        onCreate(db);

    }
    public void openDataBase(){
        db = this.getWritableDatabase();
    }

    public void insertTask(ToDoModel task){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(STATUS, 0);
        cv.put(PROJECT, task.getProject());
        cv.put(STAR, task.getStar());
        db.insert(TODO_TABLE, null, cv);
    }

    public List<ToDoModel> getAllTasks(){
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        ToDoModel task = new ToDoModel();
                        int id = cur.getColumnIndexOrThrow(ID);
                        int t = cur.getColumnIndex(TASK);
                        int sta = cur.getColumnIndexOrThrow(STATUS);
                        int pro = cur.getColumnIndexOrThrow(PROJECT);
                        int star = cur.getColumnIndexOrThrow(STAR);
                        task.setId(cur.getInt(id));
                        task.setTask(cur.getString(t));
                        task.setStatus(cur.getInt(sta));
                        task.setProject(cur.getString(pro));
                        task.setStar(cur.getInt((star)));
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateStar(int id, int star){
        ContentValues cv = new ContentValues();
        cv.put(STAR, star);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateProject(int id, String project){
        ContentValues cv = new ContentValues();
        cv.put(PROJECT, project);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }
}


