package com.example.howtodoit;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.howtodoit.Model.ToDoModel;
import com.example.howtodoit.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;


/**
 * BottomSheetFragment Helper.
 */
public class AddEditTaskBottomFragment extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";

    private CheckBox starCheckBox;
    private Button newTaskSaveButton;
    private DatabaseHandler db;

    public static AddEditTaskBottomFragment newInstance(){
        return new AddEditTaskBottomFragment();
    }


    /**
     * Initialize the dialog bundle when add new task or edit task.
     * @param savedInstanceState Empty if add new task, otherwise load saved task.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }


    /**
     *
     * @param inflater Graphical dialog.
     * @param container Target container.
     * @param savedInstanceState Dialog settings and data.
     * @return inflated view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull
            LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.new_task, container, false);
        Objects.requireNonNull(getDialog()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }


    /**
     * Initialize child views on the parent ViewHolder and edit them.
     * @param view View
     * @param savedInstanceState Bundle
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        EditText newTaskText = view.findViewById(R.id.newTaskText);
        EditText newProjectText = view.findViewById(R.id.newProjectText);
        newTaskSaveButton = view.findViewById(R.id.newTaskButton);
        starCheckBox = view.findViewById(R.id.newstarCheckBox); //find these views on the parent ViewHolder

        db = new DatabaseHandler(getActivity());
        db.openDataBase(); // open db

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        //update records in the db if User edited the item or added new item.
        if(bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            String project = bundle.getString("project");
            int star = bundle.getInt("star");
            newTaskText.setText(task);
            newProjectText.setText(project);
            starCheckBox.setChecked(star == 1);
            if(task.length()>0)
                newTaskSaveButton.setTextColor(ContextCompat.getColor(requireContext(),R.color.dracula_orange));

        }
        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(requireContext(),R.color.dracula_comment));
                }
                else{
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(requireContext(),R.color.dracula_foreground));

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final boolean finalIsUpdate = isUpdate;



        newTaskSaveButton.setOnClickListener(v -> {
            String text = newTaskText.getText().toString();
            String project = newProjectText.getText().toString();
            int star;
            if (starCheckBox.isChecked()){
                star = 1;
            }
            else{
                star = 0;
            }
            if(finalIsUpdate){
                db.updateTask(bundle.getInt("id"), text);
                db.updateProject(bundle.getInt("id"), project);
                db.updateStar(bundle.getInt("id"), star);

            }
            else{
                if (starCheckBox.isChecked()){
                    star = 1;
                }
                else{
                    star = 0;
                }
                ToDoModel task = new ToDoModel();
                task.setTask(text);
                task.setStatus(0);
                task.setStar(star);
                task.setProject(project);
                db.insertTask(task);
            }
            dismiss();
        });

    }


    /**
     *Handles event when diaglog is closed
     * @param dialog Dialog
     */
    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener){
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }

}
