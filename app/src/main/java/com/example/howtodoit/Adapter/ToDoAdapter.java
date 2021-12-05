package com.example.howtodoit.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.howtodoit.AddNewTask;
import com.example.howtodoit.MainActivity;
import com.example.howtodoit.Model.ToDoModel;
import com.example.howtodoit.R;
import com.example.howtodoit.Utils.DatabaseHandler;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> todoList;
    private MainActivity activity;
    private DatabaseHandler db;

    public ToDoAdapter(DatabaseHandler db, MainActivity activity){
        this.activity = activity;
        this.db = db;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position){

        db.openDataBase();
        ToDoModel item = todoList.get(position);

        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.star.setChecked(toBoolean(item.getStar()));
        holder.project.setText(item.getProject());
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    db.updateStatus(item.getId(), 1);
                }
                else{
                    db.updateStatus(item.getId(), 0);
                }

            }
        });
        holder.star.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    db.updateStar(item.getId(),1 );

                }
                else{db.updateStar(item.getId(), 0);}
            }
        });
    }

    private boolean toBoolean(int n) {
        return n!=0;
    }

    public void setTodoList(List<ToDoModel> todoList){
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }


    public void editTask(int position){
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        bundle.putString("project", item.getProject());
        bundle.putInt("star", item.getStar());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        CheckBox task;
        CheckBox star;
        TextView project;

        ViewHolder(View view){
            super(view);
            cardView = view.findViewById(R.id.cardView);
            task = view.findViewById(R.id.todoCheckBox);
            star = view.findViewById(R.id.starCheckBox);
            project = view.findViewById(R.id.project_name);
        }
    }
}
