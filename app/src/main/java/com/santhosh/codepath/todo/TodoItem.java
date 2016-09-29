package com.santhosh.codepath.todo;

import android.os.Parcel;
import android.os.Parcelable;

public class TodoItem implements Parcelable {
    public static final Creator<TodoItem> CREATOR = new Creator<TodoItem>() {
        @Override
        public TodoItem createFromParcel(Parcel in) {
            return new TodoItem(in);
        }

        @Override
        public TodoItem[] newArray(int size) {
            return new TodoItem[size];
        }
    };
    private String mTaskName;
    private String mDueDate;
    private String mTaskNote;
    private String mPriority;
    private String mStatus;

    public TodoItem(String taskName, String dueDate, String taskNote, String priority, String status) {
        this.mTaskName = taskName;
        this.mDueDate = dueDate;
        this.mTaskNote = taskNote;
        this.mPriority = priority;
        this.mStatus = status;
    }

    protected TodoItem(Parcel in) {
        mTaskName = in.readString();
        mDueDate = in.readString();
        mTaskNote = in.readString();
        mPriority = in.readString();
        mStatus = in.readString();
    }

    public String getTaskName() {
        return mTaskName;
    }

    public String getDueDate() {
        return mDueDate;
    }

    public String getTaskNote() {
        return mTaskNote;
    }

    public String getPriority() {
        return mPriority;
    }

    public String getStatus() {
        return mStatus;
    }

    @Override
    public String toString() {
        return "TodoItem{" +
                "TaskName='" + mTaskName + '\'' +
                ", DueDate='" + mDueDate + '\'' +
                ", TaskNote='" + mTaskNote + '\'' +
                ", Priority=" + mPriority +
                ", Status=" + mStatus +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTaskName);
        dest.writeString(mDueDate);
        dest.writeString(mTaskNote);
        dest.writeString(mPriority);
        dest.writeString(mStatus);
    }
}
