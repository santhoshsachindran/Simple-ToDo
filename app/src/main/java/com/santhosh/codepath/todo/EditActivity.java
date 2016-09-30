package com.santhosh.codepath.todo;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
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

import static com.santhosh.codepath.todo.DetailsActivity.TODO_DETAIL_COLUMNS;

public class EditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private EditText mTitle;
    private DatePicker mDate;
    private EditText mNotes;
    private Spinner mPriority;
    private Spinner mStatus;

    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mTitle = (EditText) findViewById(R.id.title_input);
        mDate = (DatePicker) findViewById(R.id.date_input);
        mNotes = (EditText) findViewById(R.id.notes_input);
        mPriority = (Spinner) findViewById(R.id.pri_input);
        mStatus = (Spinner) findViewById(R.id.status_input);

        setTitle(getString(R.string.edit_task));
        mUri = Uri.parse(getIntent().getStringExtra("URI"));

        getLoaderManager().initLoader(0, null, this);
    }

    private int getDate(String setDate) {
        return Integer.parseInt(setDate.split("/")[0]);
    }

    private int getMonth(String setDate) {
        return Integer.parseInt(setDate.split("/")[1]) - 1;
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
                updateEntry();
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

        String selection = ToDoContract.ListEntry._ID + " =?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(mUri))};

        getContentResolver().update(ToDoContract.ListEntry.CONTENT_URI, contentValues, selection, selectionArgs);

        Intent intent = new Intent();
        intent.putExtra("URI", mUri.toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean isTitleEmpty() {
        if (mTitle.getText().toString() == null || mTitle.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.cannot_insert_with_null_title), Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
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
            mTitle.setText(data.getString(data.getColumnIndex(ToDoContract.ListEntry.COLUMN_LIST_TITLE)));
            String date = data.getString(data.getColumnIndex(ToDoContract.ListEntry.COLUMN_LIST_DATE));
            mDate.updateDate(getYear(date), getMonth(date), getDate(date));
            mNotes.setText(data.getString(data.getColumnIndex(ToDoContract.ListEntry.COLUMN_LIST_NOTE)));
            mPriority.setSelection(((ArrayAdapter) mPriority.getAdapter()).getPosition(data.
                    getString(data.getColumnIndex(ToDoContract.ListEntry.COLUMN_LIST_PRIORITY))));
            mStatus.setSelection(((ArrayAdapter) mPriority.getAdapter()).getPosition(data.
                    getString(data.getColumnIndex(ToDoContract.ListEntry.COLUMN_LIST_STATUS))));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
