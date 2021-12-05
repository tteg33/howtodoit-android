package com.example.howtodoit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.howtodoit.Adapter.ToDoAdapter;

/**
 * Helper for determine whether delete or edit item based on user swipe action.
 */
public class EditDeleteHelper extends ItemTouchHelper.SimpleCallback {

    private final ToDoAdapter adapter;

    public EditDeleteHelper(ToDoAdapter adapter){
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT); // get user input left and right
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false; //Return true if the current ViewHolder can be dropped over the the target ViewHolder.
    }

    /**
     * Helper for determine user action
     * @param viewHolder viewHolder
     * @param direction direction
     */
    @Override
    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAbsoluteAdapterPosition();
        if (direction == ItemTouchHelper.LEFT) {
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Delete Item");
            builder.setMessage("Are you sure you want to delete this item?");
            builder.setPositiveButton("Confirm",
                    (dialog, which) -> adapter.deleteTask(position));
            builder.setNegativeButton("Cancel",
                    (dialog, which) -> adapter.notifyItemChanged(viewHolder.getAbsoluteAdapterPosition()));
            builder.setOnCancelListener((which) -> adapter.notifyItemChanged(viewHolder.getAbsoluteAdapterPosition()));
            AlertDialog dialog = builder.create();

            dialog.show(); //Set button text color
            Button buttonPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            buttonPositive.setTextColor(ContextCompat.getColor(adapter.getContext(), R.color.dracula_red));
            Button buttonNegative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            buttonNegative.setTextColor(ContextCompat.getColor(adapter.getContext(), R.color.dracula_orange));

        }
            else{
                adapter.editTask(position);

            }
        }

    /**
     * Swipe animation and behaviour helper.
     * @param c canvas
     * @param recyclerView recyclerview
     * @param viewHolder viewHolder
     * @param dX x axis
     * @param dY y axis
     * @param actionState action state
     * @param isCurrentlyActive check if is currently active
     */

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        Drawable icon;
        ColorDrawable background;

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        if (dX > 0) {
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_edit_24);
            background = new ColorDrawable(ContextCompat.getColor(adapter.getContext(), R.color.dracula_comment));
        } else {
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_delete_24);
            background = new ColorDrawable(ContextCompat.getColor(adapter.getContext(), R.color.dracula_red));
        }

        //draw icon in the center
        assert icon != null;
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();


        // User swipe to the right
        if (dX > 0) {
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
        }

        //User swipe to the left
        else if (dX < 0) {
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        }


        // No action
        else {
            background.setBounds(0, 0, 0, 0);
        }

        background.draw(c);
        icon.draw(c);


    }
}