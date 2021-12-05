package com.example.howtodoit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.howtodoit.Adapter.ToDoAdapter;
import com.example.howtodoit.Model.ToDoModel;
import com.example.howtodoit.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private RecyclerView todolistRecyclerView;
    private ToDoAdapter todolistAdapter;
    private FloatingActionButton addTask;

    private List<ToDoModel> todoList;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);
        db.openDataBase();

        todoList = new ArrayList<>();
        addTask = findViewById(R.id.taskFAB);




        todolistRecyclerView = findViewById(R.id.todolistRecyclerView);
        todolistRecyclerView.setLayoutManager((new LinearLayoutManager(this)));
        todolistAdapter = new ToDoAdapter(db, this);
        todolistRecyclerView.setAdapter(todolistAdapter);

        todoList = db.getAllTasks();
        Collections.reverse(todoList);
        todolistAdapter.setTodoList(todoList);

        addTask.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }

        });







    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        todoList = db.getAllTasks();
        Collections.reverse(todoList);
        todolistAdapter.setTodoList(todoList);
        todolistAdapter.notifyDataSetChanged();

    }

}