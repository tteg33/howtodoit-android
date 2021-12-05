package com.example.howtodoit.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.howtodoit.AddEditTaskBottomFragment;
import com.example.howtodoit.MainActivity;
import com.example.howtodoit.Model.ToDoModel;
import com.example.howtodoit.R;
import com.example.howtodoit.Utils.DatabaseHandler;

import java.util.List;


/**
 * Modified implementation of RecyclerView.Adapter.
 */
public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> todoList;
    private final MainActivity activity;
    private final DatabaseHandler db;

    public ToDoAdapter(DatabaseHandler db, MainActivity activity){
        this.activity = activity;
        this.db = db;
    }


    /**
     * Initialize Viewholder object.
     * @param parent parent ViewGroup.
     * @param viewType viewType.
     * @return ViewHolder.
     */
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }


    /**
     * Binds values to Views.
     * @param holder target holder to be bind.
     * @param position the position to bind value.
     */
    public void onBindViewHolder(ViewHolder holder, int position){

        db.openDataBase();
        ToDoModel item = todoList.get(position); //get item from database

        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.star.setChecked(toBoolean(item.getStar()));
        holder.project.setText(item.getProject()); //set values to holder object
        holder.task.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                db.updateStatus(item.getId(), 1);
            }
            else{
                db.updateStatus(item.getId(), 0);
            } //Listen if the checked status changes on holder, update database if changed.

        });
        holder.star.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                db.updateStar(item.getId(),1 );

            }
            else{db.updateStar(item.getId(), 0);}
        }); //Listen if the starred status changes on holder, update database if changed.
    }

    private boolean toBoolean(int n) {
        return n!=0; // Convert int data to boolean from database.
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }


    /**
     * Helper function for editing item.
     * @param position position of the item.
     */
    public void editTask(int position){
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        bundle.putString("project", item.getProject());
        bundle.putInt("star", item.getStar());
        AddEditTaskBottomFragment fragment = new AddEditTaskBottomFragment();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddEditTaskBottomFragment.TAG);

    }


    /**
     * Helper function for deleting task.
     * @param position position of the item.
     */
    public void deleteTask(int position){
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(item);
        notifyItemRemoved(position);
    }

    /**
     * Helper function for getting context values.
     * @return activity
     */
    public Context getContext() {
        return activity;
    }

    public void setTodoList(List<ToDoModel> todoList) {
        this.todoList = todoList;
    }


    /**
     * Internal class of child viewHolder and its attributes. In this case CardView template.
     */
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
