package com.santhosh.codepath.todo;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.santhosh.codepath.todo.data.ToDoContract;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String[] TODO_DETAIL_COLUMNS = {
            ToDoContract.ListEntry._ID,
            ToDoContract.ListEntry.COLUMN_LIST_TITLE,
            ToDoContract.ListEntry.COLUMN_LIST_DATE,
            ToDoContract.ListEntry.COLUMN_LIST_NOTE,
            ToDoContract.ListEntry.COLUMN_LIST_PRIORITY,
            ToDoContract.ListEntry.COLUMN_LIST_STATUS
    };
    private TextView mTitleView;
    private TextView mDueDate;
    private TextView mNotes;
    private TextView mPriority;
    private TextView mStatus;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        setTitle(getString(R.string.details_view));

        mUri = Uri.parse(getIntent().getStringExtra("URI"));

        mTitleView = (TextView) findViewById(R.id.detail_title);
        mDueDate = (TextView) findViewById(R.id.detail_date);
        mNotes = (TextView) findViewById(R.id.detail_notes);
        mPriority = (TextView) findViewById(R.id.detail_priority);
        mStatus = (TextView) findViewById(R.id.detail_status);

        getLoaderManager().initLoader(0, null, this);
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
        intent.putExtra("URI", mUri.toString());
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                mUri = Uri.parse(getIntent().getStringExtra("URI"));
                getLoaderManager().initLoader(0, null, this);
            }
        }
    }

    private void deleteItem() {
        String selection = ToDoContract.ListEntry._ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(mUri))};
        getContentResolver().delete(ToDoContract.ListEntry.CONTENT_URI, selection, selectionArgs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_delete, menu);
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mUri != null) {
            return new CursorLoader(this, mUri, TODO_DETAIL_COLUMNS, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            mTitleView.setText(data.getString(data.getColumnIndex(ToDoContract.ListEntry.COLUMN_LIST_TITLE)));
            mDueDate.setText(data.getString(data.getColumnIndex(ToDoContract.ListEntry.COLUMN_LIST_DATE)));
            mNotes.setText(data.getString(data.getColumnIndex(ToDoContract.ListEntry.COLUMN_LIST_NOTE)));
            mPriority.setText(data.getString(data.getColumnIndex(ToDoContract.ListEntry.COLUMN_LIST_PRIORITY)));
            mStatus.setText(data.getString(data.getColumnIndex(ToDoContract.ListEntry.COLUMN_LIST_STATUS)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
