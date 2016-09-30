package com.santhosh.codepath.todo;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.santhosh.codepath.todo.data.ToDoContract;

public class ToDoCursorAdapter extends CursorAdapter {
    public ToDoCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_todo_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.taskTitle.setText(cursor.getString(cursor.getColumnIndex(ToDoContract.ListEntry.COLUMN_LIST_TITLE)));
        viewHolder.taskDate.setText(cursor.getString(cursor.getColumnIndex(ToDoContract.ListEntry.COLUMN_LIST_DATE)));

        String priority = cursor.getString(cursor.getColumnIndex(ToDoContract.ListEntry.COLUMN_LIST_PRIORITY));
        viewHolder.taskPriority.setText(priority);
        viewHolder.taskPriority.setTextColor(getColor(priority));
    }

    private int getColor(String priority) {
        if (priority.equalsIgnoreCase("high")) {
            return Color.RED;
        } else if (priority.equalsIgnoreCase("medium")) {
            return Color.YELLOW;
        } else {
            return Color.GREEN;
        }
    }

    public static class ViewHolder {
        public final TextView taskTitle;
        public final TextView taskDate;
        public final TextView taskPriority;

        public ViewHolder(View view) {
            taskTitle = (TextView) view.findViewById(R.id.task_title);
            taskDate = (TextView) view.findViewById(R.id.task_date);
            taskPriority = (TextView) view.findViewById(R.id.task_priority);
        }
    }
}
