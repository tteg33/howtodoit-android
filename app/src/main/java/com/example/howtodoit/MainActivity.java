package com.example.howtodoit;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.howtodoit.Adapter.ToDoAdapter;
import com.example.howtodoit.Model.ToDoModel;
import com.example.howtodoit.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private ToDoAdapter todolistAdapter;

    private List<ToDoModel> todoList;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        db = new DatabaseHandler(this);
        db.openDataBase();

        todoList = new ArrayList<>();
        FloatingActionButton addTask = findViewById(R.id.taskFAB);

        RecyclerView todolistRecyclerView = findViewById(R.id.todolistRecyclerView);
        todolistRecyclerView.setLayoutManager((new LinearLayoutManager(this)));
        todolistAdapter = new ToDoAdapter(db, this);
        todolistRecyclerView.setAdapter(todolistAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new EditDeleteHelper(todolistAdapter));
        itemTouchHelper.attachToRecyclerView(todolistRecyclerView);

        todoList = db.getAllTasks();
        Collections.reverse(todoList);
        todolistAdapter.setTodoList(todoList);

        addTask.setOnClickListener(v -> AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG));

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void handleDialogClose(DialogInterface dialog){
        todoList = db.getAllTasks();
        Collections.reverse(todoList);
        todolistAdapter.setTodoList(todoList);
        todolistAdapter.notifyDataSetChanged();

    }

}