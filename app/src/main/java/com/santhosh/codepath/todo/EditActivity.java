package com.santhosh.codepath.todo;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.santhosh.codepath.todo.data.ToDoContract;

public class EditActivity extends AppCompatActivity {
    private EditText mTitle;
    private DatePicker mDate;
    private EditText mNotes;
    private Spinner mPriority;
    private Spinner mStatus;

    private TodoItem mTodoItem;

    private boolean mHasExtra = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mTitle = (EditText) findViewById(R.id.title_input);
        mDate = (DatePicker) findViewById(R.id.date_input);
        mNotes = (EditText) findViewById(R.id.notes_input);
        mPriority = (Spinner) findViewById(R.id.pri_input);
        mStatus = (Spinner) findViewById(R.id.status_input);

        if (getIntent().hasExtra("PARCELABLE")) {
            setTitle(getString(R.string.edit_task));
            mHasExtra = true;
            mTodoItem = getIntent().getParcelableExtra("PARCELABLE");
            mTitle.setText(mTodoItem.getTaskName());
            mNotes.setText(mTodoItem.getTaskNote());
            mPriority.setSelection(((ArrayAdapter) mPriority.getAdapter()).getPosition(mTodoItem.getPriority()));
            mStatus.setSelection(((ArrayAdapter) mStatus.getAdapter()).getPosition(mTodoItem.getStatus()));
            String setDate = mTodoItem.getDueDate();
            mDate.updateDate(getYear(setDate), getMonth(setDate), getDate(setDate));
        } else {
            setTitle(getString(R.string.add_new_task));
        }
    }

    private int getDate(String setDate) {
        return Integer.parseInt(setDate.split("/")[0]);
    }

    private int getMonth(String setDate) {
        return Integer.parseInt(setDate.split("/")[1]);
    }

    private int getYear(String setDate) {
        return Integer.parseInt(setDate.split("/")[2]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_discard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_task:
                if (mHasExtra) {
                    updateEntry();
                } else {
                    saveEntry();
                }
                return true;
            case R.id.discard_task:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateEntry() {
        if (isTitleEmpty()) {
            return;
        }

        ContentValues contentValues = getContentValues();
        String selection = ToDoContract.ListEntry.COLUMN_LIST_TITLE + " =? AND "
                + ToDoContract.ListEntry.COLUMN_LIST_DATE + " =? AND "
                + ToDoContract.ListEntry.COLUMN_LIST_NOTE + " =? AND "
                + ToDoContract.ListEntry.COLUMN_LIST_PRIORITY + " =? AND "
                + ToDoContract.ListEntry.COLUMN_LIST_STATUS + " =?";
        String[] selectionArgs = new String[]{mTodoItem.getTaskName(), mTodoItem.getDueDate(),
                mTodoItem.getTaskNote(), mTodoItem.getPriority(), mTodoItem.getStatus()};

        getContentResolver().update(ToDoContract.ListEntry.CONTENT_URI, contentValues, selection, selectionArgs);

        Intent intent = new Intent();
        intent.putExtra("PARCELABLE", new TodoItem(mTitle.getText().toString(),
                String.valueOf(mDate.getDayOfMonth()) + "/" + String.valueOf(mDate.getMonth() + 1)
                        + "/" + String.valueOf(mDate.getYear()),
                mNotes.getText().toString(),
                (String) mPriority.getSelectedItem(),
                (String) mStatus.getSelectedItem()));
        setResult(RESULT_OK, intent);
        finish();
    }

    private void saveEntry() {
        if (isTitleEmpty()) {
            return;
        }

        ContentValues contentValues = getContentValues();

        getContentResolver().insert(ToDoContract.ListEntry.CONTENT_URI, contentValues);
        finish();
    }

    private ContentValues getContentValues() {
        String title = mTitle.getText().toString();
        String date = String.valueOf(mDate.getDayOfMonth()) + "/" + String.valueOf(mDate.getMonth() + 1)
                + "/" + String.valueOf(mDate.getYear());
        String notes = mNotes.getText().toString();
        String pri = (String) mPriority.getSelectedItem();
        String status = (String) mStatus.getSelectedItem();

        ContentValues contentValues = new ContentValues();

        contentValues.put(ToDoContract.ListEntry.COLUMN_LIST_TITLE, title);
        contentValues.put(ToDoContract.ListEntry.COLUMN_LIST_DATE, date);
        contentValues.put(ToDoContract.ListEntry.COLUMN_LIST_NOTE, notes);
        contentValues.put(ToDoContract.ListEntry.COLUMN_LIST_PRIORITY, pri);
        contentValues.put(ToDoContract.ListEntry.COLUMN_LIST_STATUS, status);

        return contentValues;
    }

    private boolean isTitleEmpty() {
        if (mTitle.getText().toString() == null || mTitle.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.cannot_insert_with_null_title), Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }
}
