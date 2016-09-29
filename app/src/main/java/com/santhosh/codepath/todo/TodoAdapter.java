package com.santhosh.codepath.todo;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TodoAdapter extends ArrayAdapter<TodoItem> {

    public TodoAdapter(Context context, ArrayList<TodoItem> todoItems) {
        super(context, 0, todoItems);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.single_todo_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TodoItem item = getItem(position);
        viewHolder.taskTitle.setText(item.getTaskName());
        viewHolder.taskDate.setText(getContext().getResources().getString(R.string.due, item.getDueDate()));
        viewHolder.taskPriority.setText(item.getPriority());
        viewHolder.taskPriority.setTextColor(getColor(item.getPriority()));

        return convertView;
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

    static class ViewHolder {
        TextView taskTitle;
        TextView taskDate;
        TextView taskPriority;

        public ViewHolder(View toDoItem) {
            taskTitle = (TextView) toDoItem.findViewById(R.id.task_title);
            taskDate = (TextView) toDoItem.findViewById(R.id.task_date);
            taskPriority = (TextView) toDoItem.findViewById(R.id.task_priority);
        }
    }
}
