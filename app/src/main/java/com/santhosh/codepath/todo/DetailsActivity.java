package com.santhosh.codepath.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.santhosh.codepath.todo.data.ToDoContract;

public class DetailsActivity extends AppCompatActivity {
    private TodoItem mTodoItem;

    private TextView mTitleView;
    private TextView mDueDate;
    private TextView mNotes;
    private TextView mPriority;
    private TextView mStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        setTitle(getString(R.string.details_view));

        mTodoItem = getIntent().getParcelableExtra("PARCELABLE");

        mTitleView = (TextView) findViewById(R.id.detail_title);
        mDueDate = (TextView) findViewById(R.id.detail_date);
        mNotes = (TextView) findViewById(R.id.detail_notes);
        mPriority = (TextView) findViewById(R.id.detail_priority);
        mStatus = (TextView) findViewById(R.id.detail_status);

        mTitleView.setText(mTodoItem.getTaskName());
        mDueDate.setText(mTodoItem.getDueDate());
        mNotes.setText(mTodoItem.getTaskNote());
        mPriority.setText(mTodoItem.getPriority());
        mStatus.setText(mTodoItem.getStatus());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_task:
                editItem();
                return true;
            case R.id.delete_task:
                deleteItem();
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void editItem() {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("PARCELABLE", mTodoItem);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                TodoItem updatedItem = data.getParcelableExtra("PARCELABLE");
                mTitleView.setText(updatedItem.getTaskName());
                mDueDate.setText(updatedItem.getDueDate());
                mNotes.setText(updatedItem.getTaskNote());
                mPriority.setText(updatedItem.getPriority());
                mStatus.setText(updatedItem.getStatus());
            }
        }
    }

    private void deleteItem() {
        String selection = ToDoContract.ListEntry.COLUMN_LIST_TITLE + " =? AND "
                + ToDoContract.ListEntry.COLUMN_LIST_DATE + " =? AND "
                + ToDoContract.ListEntry.COLUMN_LIST_NOTE + " =? AND "
                + ToDoContract.ListEntry.COLUMN_LIST_PRIORITY + " =? AND "
                + ToDoContract.ListEntry.COLUMN_LIST_STATUS + " =?";
        String[] selectionArgs = new String[]{mTodoItem.getTaskName(), mTodoItem.getDueDate(),
                mTodoItem.getTaskNote(), mTodoItem.getPriority(), mTodoItem.getStatus()};
        getContentResolver().delete(ToDoContract.ListEntry.CONTENT_URI, selection, selectionArgs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_delete, menu);
        return true;
    }
}
